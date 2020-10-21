package com.myra.dev.marian.listeners.notification;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.database.MongoDb;
import com.myra.dev.marian.utilities.Permissions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.xml.crypto.Data;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
@CommandSubscribe(
        name = "notification list",
        aliases = {"notifications list"}
)
public class NotificationList implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //missing permission
        if (!Permissions.isAdministrator(event.getMember())) return;
        // Check for no arguments
        if (arguments.length != 0) return;
        // Create embed
        EmbedBuilder streamers = new EmbedBuilder()
                .setAuthor("notifications list", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().blue);

        // Get streamers
        List<String> streamersList = new Database(event.getGuild()).getNotificationManager().getStreamers();
        //if there are no streamers
        if (streamersList.isEmpty()) {
            streamers.addField("\uD83D\uDD14 │ Streamers:", "No streamers have been set up yet", false);
            event.getChannel().sendMessage(streamers.build()).queue();
            return;
        }
        String streamersString = "";
        //streamer names
        for (String streamer : streamersList) {
            streamersString += "• " + streamer + "\n";
        }
        streamers.addField("\uD83D\uDD14 │ Streamers:", streamersString, false);
        event.getChannel().sendMessage(streamers.build()).queue();
    }
}
