// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.event;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.ICancellableEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.LogicalSide;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.event.IModBusEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fired for registering particle providers at the appropriate time.
 *
 * <p>{@link ParticleType}s must be registered during {@link RegisterEvent} as usual;
 * this event is only for the {@link ParticleProvider}s.</p>
 *
 * <p>This event is not {@linkplain ICancellableEvent cancellable}, and does not {@linkplain HasResult have a result}.</p>
 *
 * <p>This event is fired on the mod-specific event bus, only on the {@linkplain LogicalSide#CLIENT logical client}.</p>
 */
public class RegisterParticleProvidersEvent extends Event implements IModBusEvent {
    private final ParticleEngine particleEngine;

    @ApiStatus.Internal
    public RegisterParticleProvidersEvent(ParticleEngine particleEngine) {
        this.particleEngine = particleEngine;
    }

    /**
     * <p>Registers a ParticleProvider for a non-json-based ParticleType.
     * These particles do not receive a list of texture sprites to use for rendering themselves.</p>
     *
     * <p>There must be <strong>no</strong> particle json with an ID matching the ParticleType,
     * or a redundant texture list error will occur when particle jsons load.</p>
     *
     * @param <T>      ParticleOptions used by the ParticleType and ParticleProvider.
     * @param type     ParticleType to register a ParticleProvider for.
     * @param provider ParticleProvider function responsible for providing that ParticleType's particles.
     */
    @SuppressWarnings("deprecation")
    public <T extends ParticleOptions> void registerSpecial(ParticleType<T> type, ParticleProvider<T> provider) {
        net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry.getInstance().register(type, provider); // fabric
    }

    /**
     * <p>Registers a ParticleProvider for a json-based ParticleType with a single texture;
     * the resulting {@link TextureSheetParticle}s will use that texture when created.</p>
     *
     * <p>A particle json with an ID matching the ParticleType <strong>must exist</strong> in the <code>particles</code> asset folder,
     * or a missing texture list error will occur when particle jsons load.</p>
     *
     * @param <T>    ParticleOptions used by the ParticleType and Sprite function.
     * @param type   ParticleType to register a ParticleProvider for.
     * @param sprite Sprite function responsible for providing that ParticleType's particles.
     */
    @SuppressWarnings("deprecation")
    public <T extends ParticleOptions> void registerSprite(ParticleType<T> type, ParticleProvider.Sprite<T> sprite) {
        // fabric: Fabric has no single-sprite registration; pick a sprite from the type's sprite set like vanilla does
        net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry.getInstance().register(type, sprites -> (options, level, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            net.minecraft.client.particle.TextureSheetParticle particle = sprite.createParticle(options, level, x, y, z, xSpeed, ySpeed, zSpeed);
            if (particle != null)
                particle.pickSprite(sprites);
            return particle;
        });
    }

    /**
     * <p>Registers a ParticleProvider for a json-based ParticleType.
     * Particle jsons define a list of texture sprites which the particle can use to render itself.</p>
     *
     * <p>A particle json with an ID matching the ParticleType <strong>must exist</strong> in the <code>particles</code> asset folder,
     * or a missing texture list error will occur when particle jsons load.</p>
     *
     * @param <T>          ParticleOptions used by the ParticleType and SpriteParticleRegistration function.
     * @param type         ParticleType to register a particle provider for.
     * @param registration SpriteParticleRegistration function responsible for providing that ParticleType's particles.
     */
    @SuppressWarnings("deprecation")
    public <T extends ParticleOptions> void registerSpriteSet(ParticleType<T> type, ParticleEngine.SpriteParticleRegistration<T> registration) {
        net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry.getInstance().register(type, registration::create); // fabric
    }
}
