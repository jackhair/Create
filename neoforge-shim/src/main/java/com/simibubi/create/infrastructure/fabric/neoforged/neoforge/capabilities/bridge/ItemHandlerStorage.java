package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.bridge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.world.item.ItemStack;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.items.IItemHandler;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.items.IItemHandlerModifiable;

/**
 * Exposes a NeoForge {@link IItemHandler} as a Fabric item {@code Storage}.
 * <p>
 * Changes apply immediately. For {@link IItemHandlerModifiable} handlers (all of Create's), each touched slot is
 * snapshotted and restored if the transaction aborts. Other handlers are simulated during the transaction and
 * changed when the outer transaction commits, so a later operation in the same transaction can't see an
 * earlier one's effect.
 * <p>
 * Create code (PORTING.md D7), not a NeoForge class.
 */
public final class ItemHandlerStorage implements SlottedStorage<ItemVariant> {
	private static final Map<IItemHandler, ItemHandlerStorage> CACHE = Collections.synchronizedMap(new WeakHashMap<>());

	private final IItemHandler handler;
	private final List<Slot> slots = new ArrayList<>();

	private ItemHandlerStorage(IItemHandler handler) {
		this.handler = handler;
	}

	/** One wrapper per handler, so slot snapshots are shared within a transaction. */
	public static SlottedStorage<ItemVariant> of(IItemHandler handler) {
		if (handler instanceof StorageItemHandler wrapped)
			return wrapped.storage() instanceof SlottedStorage<ItemVariant> slotted ? slotted : new ItemHandlerStorage(handler);
		return CACHE.computeIfAbsent(handler, ItemHandlerStorage::new);
	}

	public IItemHandler handler() {
		return handler;
	}

	@Override
	public int getSlotCount() {
		return handler.getSlots();
	}

	@Override
	public SingleSlotStorage<ItemVariant> getSlot(int index) {
		while (slots.size() <= index)
			slots.add(new Slot(slots.size()));
		return slots.get(index);
	}

	@Override
	public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
		StoragePreconditions.notBlankNotNegative(resource, maxAmount);
		long inserted = 0;
		for (int i = 0; i < handler.getSlots() && inserted < maxAmount; i++)
			inserted += getSlot(i).insert(resource, maxAmount - inserted, transaction);
		return inserted;
	}

	@Override
	public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
		StoragePreconditions.notBlankNotNegative(resource, maxAmount);
		long extracted = 0;
		for (int i = 0; i < handler.getSlots() && extracted < maxAmount; i++)
			extracted += getSlot(i).extract(resource, maxAmount - extracted, transaction);
		return extracted;
	}

	@Override
	public Iterator<StorageView<ItemVariant>> iterator() {
		List<StorageView<ItemVariant>> views = new ArrayList<>();
		for (int i = 0; i < handler.getSlots(); i++)
			views.add(getSlot(i));
		return views.iterator();
	}

	private final class Slot extends SnapshotParticipant<ItemStack> implements SingleSlotStorage<ItemVariant> {
		private final int index;

		private Slot(int index) {
			this.index = index;
		}

		@Override
		public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
			StoragePreconditions.notBlankNotNegative(resource, maxAmount);
			if (index >= handler.getSlots())
				return 0;
			int count = (int) Math.min(maxAmount, resource.getItem().getDefaultMaxStackSize());
			ItemStack remainder = handler.insertItem(index, resource.toStack(count), true);
			int accepted = count - remainder.getCount();
			if (accepted <= 0)
				return 0;
			apply(transaction, () -> handler.insertItem(index, resource.toStack(accepted), false));
			return accepted;
		}

		@Override
		public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
			StoragePreconditions.notBlankNotNegative(resource, maxAmount);
			if (index >= handler.getSlots() || !resource.matches(handler.getStackInSlot(index)))
				return 0;
			int count = (int) Math.min(maxAmount, Integer.MAX_VALUE);
			int available = handler.extractItem(index, count, true).getCount();
			if (available <= 0)
				return 0;
			apply(transaction, () -> handler.extractItem(index, available, false));
			return available;
		}

		private void apply(TransactionContext transaction, Runnable change) {
			if (handler instanceof IItemHandlerModifiable) {
				updateSnapshots(transaction);
				change.run();
			} else {
				transaction.addOuterCloseCallback(result -> {
					if (result.wasCommitted())
						change.run();
				});
			}
		}

		@Override
		public boolean isResourceBlank() {
			return getResource().isBlank();
		}

		@Override
		public ItemVariant getResource() {
			return index < handler.getSlots() ? ItemVariant.of(handler.getStackInSlot(index)) : ItemVariant.blank();
		}

		@Override
		public long getAmount() {
			return index < handler.getSlots() ? handler.getStackInSlot(index).getCount() : 0;
		}

		@Override
		public long getCapacity() {
			if (index >= handler.getSlots())
				return 0;
			ItemStack stack = handler.getStackInSlot(index);
			return stack.isEmpty() ? handler.getSlotLimit(index) : Math.min(handler.getSlotLimit(index), stack.getMaxStackSize());
		}

		@Override
		protected ItemStack createSnapshot() {
			return handler.getStackInSlot(index).copy();
		}

		@Override
		protected void readSnapshot(ItemStack snapshot) {
			((IItemHandlerModifiable) handler).setStackInSlot(index, snapshot);
		}
	}
}
