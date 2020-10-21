package com.myra.dev.marian.listeners.notification;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "notification channel",
        aliases = {"notifications channel"}
)
public class NotificationChannel implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Usage
        if (arguments.length != 1) {
            EmbedBuilder notificationUsage = new EmbedBuilder()
                    .setAuthor("notification Twitch", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "notification channel <channel>`", "\uD83D\uDCC1 │ Set the channel, the notifications will go", false);
            event.getChannel().sendMessage(notificationUsage.build()).queue();
            return;
        }
        /**
         * Change notification channel
         */
        //Get database
        Database db = new Database(event.getGuild());
        //get channel
        TextChannel channel = utilities.getTextChannel(event, arguments[0], "notification channel", "\uD83D\uDD14");
        if (channel == null) return;
        //get current notification channel
        String currentChannelId = db.getNotificationManager().getChannel();
        //remove notification channel
        if (currentChannelId.equals(channel.getId())) {
            //remove channel id
            db.set("notificationChannel", "not set");
            //success
            utilities.success(event.getChannel(), "notification channel", "\uD83D\uDD14", "Notification channel removed", "Notifications are no longer send in " + channel.getAsMention(), event.getAuthor().getEffectiveAvatarUrl(), false, null);
            return;
        }
        //change notification channel
        db.set("notificationChannel", channel.getId());
        //success
        utilities.success(event.getChannel(), "notification channel", "\uD83D\uDD14", "Notification channel changed", "Notifications are now send in " + channel.getAsMention(), event.getAuthor().getEffectiveAvatarUrl(), false, null);
        EmbedBuilder success = new EmbedBuilder()
                .setAuthor("notification channel", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().blue)
                .addField("\uD83D\uDCC1 │ Notification channel changed", "Media notifications are now send in here", false);
        channel.sendMessage(success.build()).queue();
    }
}
