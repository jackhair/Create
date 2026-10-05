// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) NeoForged and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

/**
 * Identifiers for the vanilla {@link LayeredDraw.Layer}, in the order that they render.
 *
 * <p>The corresponding rendering code can be found in the source code of {@link Gui}.
 */
public final class VanillaGuiLayers {
    public static final ResourceLocation CAMERA_OVERLAYS = ResourceLocation.withDefaultNamespace("camera_overlays");
    public static final ResourceLocation CROSSHAIR = ResourceLocation.withDefaultNamespace("crosshair");
    public static final ResourceLocation HOTBAR = ResourceLocation.withDefaultNamespace("hotbar");
    public static final ResourceLocation JUMP_METER = ResourceLocation.withDefaultNamespace("jump_meter");
    public static final ResourceLocation EXPERIENCE_BAR = ResourceLocation.withDefaultNamespace("experience_bar");
    public static final ResourceLocation PLAYER_HEALTH = ResourceLocation.withDefaultNamespace("player_health");
    public static final ResourceLocation ARMOR_LEVEL = ResourceLocation.withDefaultNamespace("armor_level");
    public static final ResourceLocation FOOD_LEVEL = ResourceLocation.withDefaultNamespace("food_level");
    public static final ResourceLocation VEHICLE_HEALTH = ResourceLocation.withDefaultNamespace("vehicle_health");
    public static final ResourceLocation AIR_LEVEL = ResourceLocation.withDefaultNamespace("air_level");
    public static final ResourceLocation SELECTED_ITEM_NAME = ResourceLocation.withDefaultNamespace("selected_item_name");
    public static final ResourceLocation SPECTATOR_TOOLTIP = ResourceLocation.withDefaultNamespace("spectator_tooltip");
    public static final ResourceLocation EXPERIENCE_LEVEL = ResourceLocation.withDefaultNamespace("experience_level");
    public static final ResourceLocation EFFECTS = ResourceLocation.withDefaultNamespace("effects");
    public static final ResourceLocation BOSS_OVERLAY = ResourceLocation.withDefaultNamespace("boss_overlay");
    public static final ResourceLocation SLEEP_OVERLAY = ResourceLocation.withDefaultNamespace("sleep_overlay");
    public static final ResourceLocation DEMO_OVERLAY = ResourceLocation.withDefaultNamespace("demo_overlay");
    public static final ResourceLocation DEBUG_OVERLAY = ResourceLocation.withDefaultNamespace("debug_overlay");
    public static final ResourceLocation SCOREBOARD_SIDEBAR = ResourceLocation.withDefaultNamespace("scoreboard_sidebar");
    public static final ResourceLocation OVERLAY_MESSAGE = ResourceLocation.withDefaultNamespace("overlay_message");
    public static final ResourceLocation TITLE = ResourceLocation.withDefaultNamespace("title");
    public static final ResourceLocation CHAT = ResourceLocation.withDefaultNamespace("chat");
    public static final ResourceLocation TAB_LIST = ResourceLocation.withDefaultNamespace("tab_list");
    public static final ResourceLocation SUBTITLE_OVERLAY = ResourceLocation.withDefaultNamespace("subtitle_overlay");
    public static final ResourceLocation SAVING_INDICATOR = ResourceLocation.withDefaultNamespace("saving_indicator");

    // fabric: vanilla layers in render order, as placeholders for GuiLayerManager
    public static final java.util.List<ResourceLocation> ALL = java.util.List.of(CAMERA_OVERLAYS, CROSSHAIR, HOTBAR, JUMP_METER, EXPERIENCE_BAR, PLAYER_HEALTH, ARMOR_LEVEL, FOOD_LEVEL, VEHICLE_HEALTH, AIR_LEVEL, SELECTED_ITEM_NAME, SPECTATOR_TOOLTIP, EXPERIENCE_LEVEL, EFFECTS, BOSS_OVERLAY, SLEEP_OVERLAY, DEMO_OVERLAY, DEBUG_OVERLAY, SCOREBOARD_SIDEBAR, OVERLAY_MESSAGE, TITLE, CHAT, TAB_LIST, SUBTITLE_OVERLAY, SAVING_INDICATOR);
}
