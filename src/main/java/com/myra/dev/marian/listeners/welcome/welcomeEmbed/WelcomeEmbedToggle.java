package com.myra.dev.marian.listeners.welcome.welcomeEmbed;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "welcome embed toggle"
)
public class WelcomeEmbedToggle implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //missing permissions
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) return;
        // Check for no arguments
        if (arguments.length != 0) return;
        //toggle feature
        new Database(event.getGuild()).getListenerManager().toggle("welcomeEmbed", "\uD83D\uDCC7", event);
    }
}
