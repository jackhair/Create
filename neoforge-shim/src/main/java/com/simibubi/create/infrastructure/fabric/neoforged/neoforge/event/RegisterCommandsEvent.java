// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.ReloadableServerResources;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.NeoForge;

/**
 * Commands are rebuilt whenever {@link ReloadableServerResources} is recreated.
 * You can use this event to register your commands whenever the {@link Commands} class in constructed.
 *
 * The event is fired on the {@link NeoForge#EVENT_BUS}
 */
public class RegisterCommandsEvent extends Event {
    private final CommandDispatcher<CommandSourceStack> dispatcher;
    private final Commands.CommandSelection environment;
    private final CommandBuildContext context;

    public RegisterCommandsEvent(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection environment, CommandBuildContext context) {
        this.dispatcher = dispatcher;
        this.environment = environment;
        this.context = context;
    }

    /**
     * {@return the command dispatcher for registering commands to be executed on the client}
     */
    public CommandDispatcher<CommandSourceStack> getDispatcher() {
        return dispatcher;
    }

    /**
     * {@return the environment the command is being registered for}
     */
    public Commands.CommandSelection getCommandSelection() {
        return environment;
    }

    /**
     * {@return the context to build the commands for}
     */
    public CommandBuildContext getBuildContext() {
        return context;
    }
}
