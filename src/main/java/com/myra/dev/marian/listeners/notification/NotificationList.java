package com.myra.dev.marian.listeners.notification;

import com.myra.dev.marian.database.allMethods.Database;

import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;

@CommandSubscribe(
        name = "notification list",
        aliases = {"notifications list"},
        requires = "administrator"
)
public class NotificationList implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Check for no arguments
        if (ctx.getArguments().length != 0) return;
        // Create embed
        EmbedBuilder streamers = new EmbedBuilder()
                .setAuthor("notifications list", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().blue);

        // Get streamers
        List<String> streamersList = new Database(ctx.getGuild()).getNotificationManager().getStreamers();
        //if there are no streamers
        if (streamersList.isEmpty()) {
            streamers.addField("\uD83D\uDD14 │ Streamers:", "No streamers have been set up yet", false);
            ctx.getChannel().sendMessage(streamers.build()).queue();
            return;
        }
        String streamersString = "";
        //streamer names
        for (String streamer : streamersList) {
            streamersString += "• " + streamer + "\n";
        }
        streamers.addField("\uD83D\uDD14 │ Streamers:", streamersString, false);
        ctx.getChannel().sendMessage(streamers.build()).queue();
    }
}
