package com.myra.dev.marian.listeners.welcome.welcomeDirectMessage;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

@CommandSubscribe(
        name = "welcome direct message preview",
        aliases = {"welcome dm preview"}
)
public class WelcomeDirectMessagePreview implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        // Check for no arguments
        if (arguments.length != 0) return;
        // Send message
        new WelcomeDirectMessageRender().embed(event.getGuild(), null, event.getChannel(), event.getAuthor());
    }
}