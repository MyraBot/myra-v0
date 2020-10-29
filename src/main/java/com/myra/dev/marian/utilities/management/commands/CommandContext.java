package com.myra.dev.marian.utilities.management.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandContext {
    // Variables
    private final String prefix;
    private final GuildMessageReceivedEvent event;
    private final String[] arguments;

    // Constructor
    public CommandContext(String prefix, GuildMessageReceivedEvent event, String[] arguments) {
        this.prefix = prefix;
        this.event = event;
        this.arguments = arguments;
    }

    // Get prefix
    public String getPrefix() {
        return prefix;
    }

    // Get event
    public GuildMessageReceivedEvent getEvent() {
        return event;
    }

    // Get Guild
    public Guild getGuild() {
        return event.getGuild();
    }

    // Get channel
    public TextChannel getChannel() {
        return event.getChannel();
    }

    // Get author
    public User getAuthor() {
        return event.getAuthor();
    }

    // Get member
    public Member getMember() {
        return event.getMember();
    }

    // Get arguments
    public String[] getArguments() {
        return arguments;
    }
}
