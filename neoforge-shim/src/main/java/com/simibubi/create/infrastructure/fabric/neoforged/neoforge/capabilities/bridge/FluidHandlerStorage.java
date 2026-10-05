package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.bridge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.FluidStack;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.capability.IFluidHandler;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.capability.templates.FluidTank;

/**
 * Exposes a NeoForge {@link IFluidHandler} as a Fabric fluid {@code Storage}. Same transaction strategy as
 * {@link ItemHandlerStorage}: {@link FluidTank}s are snapshotted and restored on abort; other handlers are
 * simulated and changed when the outer transaction commits.
 * <p>
 * Create code (PORTING.md D7), not a NeoForge class.
 */
public final class FluidHandlerStorage implements SlottedStorage<FluidVariant> {
	private static final Map<IFluidHandler, FluidHandlerStorage> CACHE = Collections.synchronizedMap(new WeakHashMap<>());

	private final IFluidHandler handler;
	private final List<Tank> tanks = new ArrayList<>();
	private final SnapshotParticipant<FluidStack> tankSnapshot;

	private FluidHandlerStorage(IFluidHandler handler) {
		this.handler = handler;
		this.tankSnapshot = handler instanceof FluidTank tank ? new SnapshotParticipant<>() {
			@Override
			protected FluidStack createSnapshot() {
				return tank.getFluid().copy();
			}

			@Override
			protected void readSnapshot(FluidStack snapshot) {
				tank.setFluid(snapshot);
			}
		} : null;
	}

	public static SlottedStorage<FluidVariant> of(IFluidHandler handler) {
		if (handler instanceof StorageFluidHandler wrapped && wrapped.storage() instanceof SlottedStorage<FluidVariant> slotted)
			return slotted;
		return CACHE.computeIfAbsent(handler, FluidHandlerStorage::new);
	}

	public IFluidHandler handler() {
		return handler;
	}

	@Override
	public int getSlotCount() {
		return handler.getTanks();
	}

	@Override
	public SingleSlotStorage<FluidVariant> getSlot(int index) {
		while (tanks.size() <= index)
			tanks.add(new Tank(tanks.size()));
		return tanks.get(index);
	}

	@Override
	public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
		StoragePreconditions.notBlankNotNegative(resource, maxAmount);
		FluidStack stack = FluidVariants.toStack(resource, maxAmount);
		long accepted = handler.fill(stack, FluidAction.SIMULATE);
		if (accepted <= 0)
			return 0;
		apply(transaction, () -> handler.fill(FluidVariants.toStack(resource, accepted), FluidAction.EXECUTE));
		return accepted;
	}

	@Override
	public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
		StoragePreconditions.notBlankNotNegative(resource, maxAmount);
		long available = handler.drain(FluidVariants.toStack(resource, maxAmount), FluidAction.SIMULATE).getAmount();
		if (available <= 0)
			return 0;
		apply(transaction, () -> handler.drain(FluidVariants.toStack(resource, available), FluidAction.EXECUTE));
		return available;
	}

	private void apply(TransactionContext transaction, Runnable change) {
		if (tankSnapshot != null) {
			tankSnapshot.updateSnapshots(transaction);
			change.run();
		} else {
			transaction.addOuterCloseCallback(result -> {
				if (result.wasCommitted())
					change.run();
			});
		}
	}

	@Override
	public Iterator<StorageView<FluidVariant>> iterator() {
		List<StorageView<FluidVariant>> views = new ArrayList<>();
		for (int i = 0; i < handler.getTanks(); i++)
			views.add(getSlot(i));
		return views.iterator();
	}

	/** A tank view; inserting or extracting goes through the handler as a whole, as NeoForge handlers aren't per-tank. */
	private final class Tank implements SingleSlotStorage<FluidVariant> {
		private final int index;

		private Tank(int index) {
			this.index = index;
		}

		private FluidStack fluid() {
			return index < handler.getTanks() ? handler.getFluidInTank(index) : FluidStack.EMPTY;
		}

		@Override
		public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
			return FluidHandlerStorage.this.insert(resource, maxAmount, transaction);
		}

		@Override
		public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
			if (!resource.equals(getResource()))
				return 0;
			return FluidHandlerStorage.this.extract(resource, Math.min(maxAmount, getAmount()), transaction);
		}

		@Override
		public boolean isResourceBlank() {
			return fluid().isEmpty();
		}

		@Override
		public FluidVariant getResource() {
			return FluidVariants.of(fluid());
		}

		@Override
		public long getAmount() {
			return fluid().getAmount();
		}

		@Override
		public long getCapacity() {
			return index < handler.getTanks() ? handler.getTankCapacity(index) : 0;
		}
	}
}
