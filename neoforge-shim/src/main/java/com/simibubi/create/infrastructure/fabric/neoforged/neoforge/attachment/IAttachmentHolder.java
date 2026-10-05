package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.attachment;

import java.util.Optional;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;

/**
 * NeoForge's data attachment API on top of Fabric's ({@link AttachmentTarget}). Injected into the vanilla
 * classes Fabric supports attachments on (see neoforge-shim/injected-interfaces.json).
 * <p>
 * Create-owned re-implementation of NeoForge's {@code IAttachmentHolder} for Fabric (PORTING.md D7).
 */
public interface IAttachmentHolder {
	private AttachmentTarget target() {
		return (AttachmentTarget) this;
	}

	default boolean hasData(AttachmentType<?> type) {
		return target().hasAttached(type.fabric());
	}

	default <T> boolean hasData(Supplier<AttachmentType<T>> type) {
		return hasData(type.get());
	}

	default <T> T getData(AttachmentType<T> type) {
		AttachmentType.Slot<T> slot = target().getAttachedOrCreate(type.fabric(), () -> type.createSlot(this));
		return slot.get(type, this);
	}

	default <T> T getData(Supplier<AttachmentType<T>> type) {
		return getData(type.get());
	}

	default <T> Optional<T> getExistingData(AttachmentType<T> type) {
		AttachmentType.Slot<T> slot = target().getAttached(type.fabric());
		return slot == null ? Optional.empty() : Optional.of(slot.get(type, this));
	}

	default <T> Optional<T> getExistingData(Supplier<AttachmentType<T>> type) {
		return getExistingData(type.get());
	}

	@Nullable
	default <T> T setData(AttachmentType<T> type, T data) {
		AttachmentType.Slot<T> previous = target().setAttached(type.fabric(), AttachmentType.Slot.of(data, this));
		return previous == null ? null : previous.get(type, this);
	}

	@Nullable
	default <T> T setData(Supplier<AttachmentType<T>> type, T data) {
		return setData(type.get(), data);
	}

	@Nullable
	default <T> T removeData(AttachmentType<T> type) {
		AttachmentType.Slot<T> previous = target().removeAttached(type.fabric());
		return previous == null ? null : previous.get(type, this);
	}

	@Nullable
	default <T> T removeData(Supplier<AttachmentType<T>> type) {
		return removeData(type.get());
	}
}
