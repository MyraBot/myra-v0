package com.myra.dev.marian.listeners.notification;

import com.myra.dev.marian.database.allMethods.Database;

import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandSubscribe(
        name = "notification twitch",
        aliases = {"notification live", "notifications twitch", "notifications live"},
        requires = "administrator"
)
public class AddStreamer implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Usage
        if (ctx.getArguments().length == 0) {
            EmbedBuilder notificationUsage = new EmbedBuilder()
                    .setAuthor("notification Twitch", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + ctx.getPrefix() + "notification twitch <streamer>`", "\uD83D\uDCE1 │ Add and remove auto notifications for a twitch streamer", false);
            ctx.getChannel().sendMessage(notificationUsage.build()).queue();
            return;
        }
        /**
         * Add / remove streamer
         */
        // Add / remove streamer
        new Database(ctx.getGuild()).getNotificationManager().addStreamer(ctx.getEvent());
    }
}