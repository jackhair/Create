package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.bridge;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.FluidStack;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.capability.IFluidHandler;

/**
 * Exposes a Fabric fluid {@code Storage} as a NeoForge {@link IFluidHandler}. Non-slotted storages show their
 * views as tanks. Create code (PORTING.md D7), not a NeoForge class.
 */
public class StorageFluidHandler implements IFluidHandler {
	private final Storage<FluidVariant> storage;

	protected StorageFluidHandler(Storage<FluidVariant> storage) {
		this.storage = storage;
	}

	@Nullable
	public static IFluidHandler of(@Nullable Storage<FluidVariant> storage) {
		if (storage == null)
			return null;
		if (storage instanceof FluidHandlerStorage wrapped)
			return wrapped.handler();
		return new StorageFluidHandler(storage);
	}

	public Storage<FluidVariant> storage() {
		return storage;
	}

	private List<StorageView<FluidVariant>> views() {
		List<StorageView<FluidVariant>> views = new ArrayList<>();
		if (storage instanceof SlottedStorage<FluidVariant> slotted) {
			for (int i = 0; i < slotted.getSlotCount(); i++)
				views.add(slotted.getSlot(i));
		} else {
			for (StorageView<FluidVariant> view : storage)
				views.add(view);
		}
		return views;
	}

	@Override
	public int getTanks() {
		return Math.max(1, views().size());
	}

	@Override
	public FluidStack getFluidInTank(int tank) {
		List<StorageView<FluidVariant>> views = views();
		if (tank >= views.size())
			return FluidStack.EMPTY;
		StorageView<FluidVariant> view = views.get(tank);
		return view.isResourceBlank() ? FluidStack.EMPTY : FluidVariants.toStack(view.getResource(), view.getAmount());
	}

	@Override
	public long getTankCapacity(int tank) {
		List<StorageView<FluidVariant>> views = views();
		return tank < views.size() ? views.get(tank).getCapacity() : 0;
	}

	@Override
	public boolean isFluidValid(int tank, FluidStack stack) {
		return fill(stack, FluidAction.SIMULATE) > 0;
	}

	@Override
	public long fill(FluidStack resource, FluidAction action) {
		if (resource.isEmpty() || !storage.supportsInsertion())
			return 0;
		try (Transaction transaction = TransferUtil.open()) {
			long filled = storage.insert(FluidVariants.of(resource), resource.getAmount(), transaction);
			if (action.execute())
				transaction.commit();
			return filled;
		}
	}

	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		if (resource.isEmpty() || !storage.supportsExtraction())
			return FluidStack.EMPTY;
		FluidVariant variant = FluidVariants.of(resource);
		try (Transaction transaction = TransferUtil.open()) {
			long drained = storage.extract(variant, resource.getAmount(), transaction);
			if (action.execute())
				transaction.commit();
			return FluidVariants.toStack(variant, drained);
		}
	}

	@Override
	public FluidStack drain(long maxDrain, FluidAction action) {
		if (maxDrain <= 0 || !storage.supportsExtraction())
			return FluidStack.EMPTY;
		for (StorageView<FluidVariant> view : storage.nonEmptyViews())
			return drain(FluidVariants.toStack(view.getResource(), maxDrain), action);
		return FluidStack.EMPTY;
	}
}
