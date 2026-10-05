package com.simibubi.create.infrastructure.fabric.neoforged.fml;

import java.util.Optional;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;

import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.IEventBus;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforgespi.language.IModInfo;

/**
 * Wraps a Fabric mod container with the parts of NeoForge's {@code ModContainer} API Create uses
 * (PORTING.md D7). Configs register through Forge Config API Port.
 */
public class ModContainer implements IModInfo {
	private final net.fabricmc.loader.api.ModContainer fabric;
	@Nullable
	private final IEventBus eventBus;

	public ModContainer(net.fabricmc.loader.api.ModContainer fabric, @Nullable IEventBus eventBus) {
		this.fabric = fabric;
		this.eventBus = eventBus;
	}

	public net.fabricmc.loader.api.ModContainer getFabricContainer() {
		return fabric;
	}

	@Override
	public String getModId() {
		return fabric.getMetadata().getId();
	}

	@Override
	public String getDisplayName() {
		return fabric.getMetadata().getName();
	}

	@Override
	public Object getVersion() {
		return fabric.getMetadata().getVersion();
	}

	public IModInfo getModInfo() {
		return this;
	}

	/** Only Create's own container has a mod bus on Fabric. */
	@Nullable
	public IEventBus getEventBus() {
		return eventBus;
	}

	public void registerConfig(ModConfig.Type type, IConfigSpec spec) {
		NeoForgeConfigRegistry.INSTANCE.register(getModId(), type, spec);
	}

	public void registerConfig(ModConfig.Type type, IConfigSpec spec, String fileName) {
		NeoForgeConfigRegistry.INSTANCE.register(getModId(), type, spec, fileName);
	}

	/** Extension points (such as config screens) are wired up by the Fabric integrations that need them. */
	public <T> void registerExtensionPoint(Class<T> point, Supplier<? extends T> extension) {
		ExtensionPoints.register(getModId(), point, extension);
	}

	public <T> Optional<T> getCustomExtension(Class<T> point) {
		return ExtensionPoints.get(getModId(), point);
	}
}
