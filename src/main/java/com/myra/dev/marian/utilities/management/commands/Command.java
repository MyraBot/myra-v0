package com.myra.dev.marian.utilities.management.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * Represents a command.
 */
public interface Command {
    /**
     * Executes the command when the implementation if called.
     */
    void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception;
}