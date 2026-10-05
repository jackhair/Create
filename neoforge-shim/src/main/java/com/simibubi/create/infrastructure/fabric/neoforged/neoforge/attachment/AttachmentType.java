package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.attachment;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * NeoForge attachment types backed by Fabric attachments. Each NeoForge type becomes a Fabric attachment
 * holding a {@link Slot}, created when the type is registered (see {@code NeoForgeRegistries.ATTACHMENT_TYPES}).
 * <p>
 * NeoForge serializers receive the holder when reading, which Fabric's codecs don't. Loaded data therefore
 * stays as NBT in the slot until the first {@code getData} call, which deserializes it with the holder.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code AttachmentType} for Fabric (PORTING.md D7).
 */
public final class AttachmentType<T> {
	private final Function<IAttachmentHolder, T> defaultValueSupplier;
	@Nullable
	private final IAttachmentSerializer<Tag, T> serializer;
	private final boolean copyOnDeath;
	@Nullable
	private net.fabricmc.fabric.api.attachment.v1.AttachmentType<Slot<T>> fabric;

	@SuppressWarnings("unchecked")
	private AttachmentType(Builder<T> builder) {
		this.defaultValueSupplier = builder.defaultValueSupplier;
		this.serializer = (IAttachmentSerializer<Tag, T>) builder.serializer;
		this.copyOnDeath = builder.copyOnDeath;
	}

	public static <T> Builder<T> builder(Supplier<T> defaultValueSupplier) {
		return new Builder<>(holder -> defaultValueSupplier.get());
	}

	public static <T> Builder<T> builder(Function<IAttachmentHolder, T> defaultValueConstructor) {
		return new Builder<>(defaultValueConstructor);
	}

	/** Called when this type is registered; creates the backing Fabric attachment under the same id. */
	public void bind(ResourceLocation id) {
		if (fabric != null)
			return;
		AttachmentRegistry.Builder<Slot<T>> builder = AttachmentRegistry.builder();
		if (serializer != null)
			builder.persistent(slotCodec());
		if (copyOnDeath)
			builder.copyOnDeath();
		fabric = builder.buildAndRegister(id);
	}

	net.fabricmc.fabric.api.attachment.v1.AttachmentType<Slot<T>> fabric() {
		return Objects.requireNonNull(fabric, "Attachment type used before registration");
	}

	Slot<T> createSlot(IAttachmentHolder holder) {
		return Slot.of(defaultValueSupplier.apply(holder), holder);
	}

	private Codec<Slot<T>> slotCodec() {
		return new Codec<>() {
			@Override
			public <O> DataResult<com.mojang.datafixers.util.Pair<Slot<T>, O>> decode(DynamicOps<O> ops, O input) {
				Tag tag = ops.convertTo(NbtOps.INSTANCE, input);
				return DataResult.success(com.mojang.datafixers.util.Pair.of(Slot.pending(tag), ops.empty()));
			}

			@Override
			public <O> DataResult<O> encode(Slot<T> slot, DynamicOps<O> ops, O prefix) {
				Tag tag = slot.write(AttachmentType.this);
				return tag == null ? DataResult.error(() -> "Attachment serializer wrote nothing") : DataResult.success(NbtOps.INSTANCE.convertTo(ops, tag));
			}
		};
	}

	/** Holds either a value or NBT waiting to be read once the holder is known. */
	public static final class Slot<T> {
		@Nullable
		private T value;
		@Nullable
		private Tag pending;
		@Nullable
		private IAttachmentHolder holder;

		static <T> Slot<T> of(T value, IAttachmentHolder holder) {
			Slot<T> slot = new Slot<>();
			slot.value = value;
			slot.holder = holder;
			return slot;
		}

		static <T> Slot<T> pending(Tag tag) {
			Slot<T> slot = new Slot<>();
			slot.pending = tag;
			return slot;
		}

		T get(AttachmentType<T> type, IAttachmentHolder holder) {
			if (pending != null) {
				this.holder = holder;
				value = Objects.requireNonNull(type.serializer).read(holder, pending, registries(holder));
				pending = null;
			}
			return value;
		}

		@Nullable
		Tag write(AttachmentType<T> type) {
			if (pending != null)
				return pending;
			return Objects.requireNonNull(type.serializer).write(value, registries(holder));
		}

		private static HolderLookup.Provider registries(@Nullable IAttachmentHolder holder) {
			if (holder instanceof Entity entity)
				return entity.registryAccess();
			if (holder instanceof BlockEntity be && be.getLevel() != null)
				return be.getLevel().registryAccess();
			return RegistryAccess.EMPTY;
		}
	}

	public static final class Builder<T> {
		private final Function<IAttachmentHolder, T> defaultValueSupplier;
		@Nullable
		private IAttachmentSerializer<?, T> serializer;
		private boolean copyOnDeath;

		private Builder(Function<IAttachmentHolder, T> defaultValueSupplier) {
			this.defaultValueSupplier = defaultValueSupplier;
		}

		public <S extends Tag> Builder<T> serialize(IAttachmentSerializer<S, T> serializer) {
			this.serializer = serializer;
			return this;
		}

		public Builder<T> serialize(Codec<T> codec) {
			this.serializer = new IAttachmentSerializer<Tag, T>() {
				@Override
				public T read(IAttachmentHolder holder, Tag tag, HolderLookup.Provider provider) {
					return codec.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag).getOrThrow();
				}

				@Override
				public Tag write(T attachment, HolderLookup.Provider provider) {
					return codec.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), attachment).getOrThrow();
				}
			};
			return this;
		}

		public Builder<T> copyOnDeath() {
			this.copyOnDeath = true;
			return this;
		}

		public AttachmentType<T> build() {
			return new AttachmentType<>(this);
		}
	}
}
