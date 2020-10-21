package com.myra.dev.marian.listeners.notification;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "notification twitch",
        aliases = {"notification live", "notifications twitch", "notifications live"}
)
public class AddStreamer implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Missing permission
        if (!Permissions.isAdministrator(event.getMember())) return;
        // Usage
        if (arguments.length == 0) {
            EmbedBuilder notificationUsage = new EmbedBuilder()
                    .setAuthor("notification Twitch", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "notification twitch <streamer>`", "\uD83D\uDCE1 │ Add and remove auto notifications for a twitch streamer", false);
            event.getChannel().sendMessage(notificationUsage.build()).queue();
            return;
        }
        /**
         * Add / remove streamer
         */
        // Add / remove streamer
        new Database(event.getGuild()).getNotificationManager().addStreamer(event);
    }
}