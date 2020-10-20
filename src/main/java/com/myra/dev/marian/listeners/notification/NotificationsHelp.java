package com.myra.dev.marian.listeners.notification;

import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.Permissions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
@CommandSubscribe(
        name = "notification",
        aliases = {"notifications"}
)
public class NotificationsHelp implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        if (!Permissions.isAdministrator(event.getMember())) return;
        //split message
        String[] sentMessage = event.getMessage().getContentRaw().split("\\s+");
        //arguments
        String[] twitch = {"twitch", "streamer", "live"};
        String[] channel = {"channel"};
        //check for arguments
        /**
         * notifications
         */
        if (sentMessage.length == 1) {
            EmbedBuilder notificationUsage = new EmbedBuilder()
                    .setAuthor("notification", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "notification twitch <streamer>`", "\uD83D\uDCE1 │ Add and remove auto notifications for a twitch streamer", false)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "notification list`", "\uD83D\uDD14 │ Displays all users you get notification from", false)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "notification channel <channel>`", "\uD83D\uDCC1 │ Set the channel, the notifications will go", false);
            event.getChannel().sendMessage(notificationUsage.build()).queue();
        }
        if (sentMessage.length == 2 && Arrays.stream(twitch).anyMatch(sentMessage[1]::equalsIgnoreCase)) {
            EmbedBuilder notificationUsage = new EmbedBuilder()
                    .setAuthor("notification Twitch", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "notification twitch <streamer>`", "\uD83D\uDCE1 │ Add and remove auto notifications for a twitch streamer", false);
            event.getChannel().sendMessage(notificationUsage.build()).queue();
        }
        if (sentMessage.length == 2 && Arrays.stream(channel).anyMatch(sentMessage[1]::equalsIgnoreCase)) {
            EmbedBuilder notificationUsage = new EmbedBuilder()
                    .setAuthor("notification Twitch", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "notification channel <channel>`", "\uD83D\uDCC1 │ Set the channel, the notifications will go", false);
            event.getChannel().sendMessage(notificationUsage.build()).queue();
        }
    }
}
