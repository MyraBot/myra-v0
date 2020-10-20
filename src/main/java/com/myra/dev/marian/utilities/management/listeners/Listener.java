package com.myra.dev.marian.utilities.management.listeners;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface Listener {
    /**
     * Executes the command when the implementation if called.
     */
    void execute(GuildMessageReceivedEvent event) throws Exception;
}
