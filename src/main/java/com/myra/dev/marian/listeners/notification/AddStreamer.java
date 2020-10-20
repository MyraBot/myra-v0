package com.myra.dev.marian.listeners.notification;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.Permissions;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
@CommandSubscribe(
        name = "notification twitch",
        aliases = {"notification live", "notifications twitch", "notifications live"}
)
public class AddStreamer implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        Database db = new Database(event.getGuild());

        //missing permission
        if (!Permissions.isAdministrator(event.getMember())) return;
        //send Typing
        event.getChannel().sendTyping().queue();
        //add streamer
        db.getNotificationManager().addStreamer(event);
    }
}