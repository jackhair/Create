// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event;

import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.KnownPack;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.ModList;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.event.IModBusEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforgespi.language.IModInfo;

/**
 * Fired on {@link PackRepository} creation to allow mods to add new pack finders.
 */
public class AddPackFindersEvent extends Event implements IModBusEvent {
    private final PackType packType;
    private final Consumer<RepositorySource> sources;
    private final boolean trusted;

    public AddPackFindersEvent(PackType packType, Consumer<RepositorySource> sources, boolean trusted) {
        this.packType = packType;
        this.sources = sources;
        this.trusted = trusted;
    }

    /**
     * Adds a new source to the list of pack finders.
     *
     * <p>Sources are processed in the order that they are added to the event.
     * Use {@link Pack.Position#TOP} to add high priority packs,
     * and {@link Pack.Position#BOTTOM} to add low priority packs.
     * 
     * @param source the pack finder
     */
    public void addRepositorySource(RepositorySource source) {
        sources.accept(source);
    }

    /**
     * @return the {@link PackType} of the pack repository being constructed.
     */
    public PackType getPackType() {
        return packType;
    }

    /**
     * Helper method to register a pack found under the `resources/` folder.
     *
     * @param packLocation    Location of the pack to load. Namespace should be the modid of the pack owner and path is the location under `resources/` folder
     * @param packType        Whether pack is a resourcepack or datapack
     * @param packNameDisplay The text that shows for the pack on the pack selection screen
     * @param packSource      Controls whether the datapack is enabled or disabled by default. Resourcepacks are always disabled by default unless you set alwaysActive to true.
     * @param alwaysActive    Whether the pack is forced active always. If false, players have to manually activate the pack themselves
     * @param packPosition    Where the pack goes for determining pack applying order
     */
    public void addPackFinders(ResourceLocation packLocation, PackType packType, Component packNameDisplay, PackSource packSource, boolean alwaysActive, Pack.Position packPosition) {
        if (getPackType() == packType) {
            // fabric: the pack lives in the mod's Fabric Loader container
            var container = net.fabricmc.loader.api.FabricLoader.getInstance().getModContainer(packLocation.getNamespace()).orElseThrow(() -> new IllegalArgumentException("Mod not found: " + packLocation.getNamespace()));
            var resourcePath = container.findPath(packLocation.getPath()).orElseThrow(() -> new IllegalArgumentException("Pack not found: " + packLocation));
            var version = container.getMetadata().getVersion().getFriendlyString();
            Pack.ResourcesSupplier resources = new Pack.ResourcesSupplier() {
                @Override
                public net.minecraft.server.packs.PackResources openPrimary(PackLocationInfo info) {
                    return new PathPackResources(info, resourcePath);
                }

                @Override
                public net.minecraft.server.packs.PackResources openFull(PackLocationInfo info, Pack.Metadata metadata) {
                    return new PathPackResources(info, resourcePath);
                }
            };

            var pack = Pack.readMetaAndCreate(
                    new PackLocationInfo("mod/" + packLocation, packNameDisplay, packSource, Optional.of(new KnownPack("neoforge", "mod/" + packLocation, version))),
                    resources,
                    packType,
                    new PackSelectionConfig(alwaysActive, packPosition, false));

            addRepositorySource((packConsumer) -> packConsumer.accept(pack));
        }
    }

    /**
     * {@return whether or not the pack repository being assembled is the one used to provide known packs to the client to avoid syncing from the server}
     */
    public boolean isTrusted() {
        return trusted;
    }
}
