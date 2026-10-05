package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.bridge;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.items.IItemHandler;

/**
 * Exposes a Fabric item {@code Storage} as a NeoForge {@link IItemHandler}. Slotted storages map slot for
 * slot; other storages show their non-empty views as slots plus one empty slot, and insert into the storage
 * as a whole regardless of the slot index.
 * <p>
 * Create code (PORTING.md D7), not a NeoForge class.
 */
public final class StorageItemHandler implements IItemHandler {
	private final Storage<ItemVariant> storage;

	private StorageItemHandler(Storage<ItemVariant> storage) {
		this.storage = storage;
	}

	@Nullable
	public static IItemHandler of(@Nullable Storage<ItemVariant> storage) {
		if (storage == null)
			return null;
		if (storage instanceof ItemHandlerStorage wrapped)
			return wrapped.handler();
		return new StorageItemHandler(storage);
	}

	public Storage<ItemVariant> storage() {
		return storage;
	}

	@Nullable
	private SlottedStorage<ItemVariant> slotted() {
		return storage instanceof SlottedStorage<ItemVariant> slotted ? slotted : null;
	}

	private List<StorageView<ItemVariant>> views() {
		List<StorageView<ItemVariant>> views = new ArrayList<>();
		for (StorageView<ItemVariant> view : storage.nonEmptyViews())
			views.add(view);
		return views;
	}

	@Nullable
	private StorageView<ItemVariant> view(int slot) {
		SlottedStorage<ItemVariant> slotted = slotted();
		if (slotted != null)
			return slot < slotted.getSlotCount() ? slotted.getSlot(slot) : null;
		List<StorageView<ItemVariant>> views = views();
		return slot < views.size() ? views.get(slot) : null;
	}

	@Override
	public int getSlots() {
		SlottedStorage<ItemVariant> slotted = slotted();
		return slotted != null ? slotted.getSlotCount() : views().size() + 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		StorageView<ItemVariant> view = view(slot);
		if (view == null || view.isResourceBlank())
			return ItemStack.EMPTY;
		return view.getResource().toStack((int) Math.min(view.getAmount(), Integer.MAX_VALUE));
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (stack.isEmpty())
			return ItemStack.EMPTY;
		ItemVariant variant = ItemVariant.of(stack);
		SlottedStorage<ItemVariant> slotted = slotted();
		try (Transaction transaction = TransferUtil.open()) {
			long inserted = slotted != null
				? (slot < slotted.getSlotCount() ? slotted.getSlot(slot).insert(variant, stack.getCount(), transaction) : 0)
				: storage.insert(variant, stack.getCount(), transaction);
			if (!simulate)
				transaction.commit();
			return inserted >= stack.getCount() ? ItemStack.EMPTY : stack.copyWithCount(stack.getCount() - (int) inserted);
		}
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (amount <= 0)
			return ItemStack.EMPTY;
		StorageView<ItemVariant> view = view(slot);
		if (view == null || view.isResourceBlank())
			return ItemStack.EMPTY;
		ItemVariant variant = view.getResource();
		try (Transaction transaction = TransferUtil.open()) {
			long extracted = view.extract(variant, amount, transaction);
			if (!simulate)
				transaction.commit();
			return extracted <= 0 ? ItemStack.EMPTY : variant.toStack((int) extracted);
		}
	}

	@Override
	public int getSlotLimit(int slot) {
		StorageView<ItemVariant> view = view(slot);
		return view == null ? 64 : (int) Math.min(view.getCapacity(), Integer.MAX_VALUE);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return insertItem(slot, stack.copyWithCount(1), true).isEmpty();
	}
}
