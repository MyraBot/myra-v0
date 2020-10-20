package com.myra.dev.marian.listeners.logging;

import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Return;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;

public class LogChannel extends Events {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) throws Exception{
        Database db = new Database(event.getGuild());

        //missing permission
        if (!Permissions.isAdministrator(event.getMember())) return;
        //split message
        String[] sentMessage = event.getMessage().getContentRaw().split("\\s+");
        //alias
        String[] arg1 = {Prefix.getPrefix(event.getGuild()) + "log", Prefix.getPrefix(event.getGuild()) + "logs", Prefix.getPrefix(event.getGuild()) + "logging"};
        String[] arg2 = {"channel"};

        if (Arrays.stream(arg1).anyMatch(sentMessage[0]::equalsIgnoreCase) && Arrays.stream(arg2).anyMatch(sentMessage[1]::equalsIgnoreCase)) {
            if (sentMessage.length != 3) {
                EmbedBuilder usage = new EmbedBuilder()
                        .setAuthor("│ log channel", null, event.getAuthor().getEffectiveAvatarUrl())
                        .setColor(Manager.getUtilities().gray)
                        .addField("`" + Prefix.getPrefix(event.getGuild()) + "log channel <channel>`", "\uD83E\uDDFE │ Set the channel where all logging actions should go", false);
                event.getChannel().sendMessage(usage.build()).queue();
                return;
            }
            //get channel
            TextChannel channel = new Return().textChannel(event, sentMessage, 2, "log channel", "\uD83E\uDDFE");
            if (channel == null) return;
            //if given channel is the same as in database
            if (channel.getId().equals(db.get("logChannel"))) {
                //database
                db.set("logChannel", "not set");
                //success
                Manager.getUtilities().success(event.getChannel(), "log channel", "\uD83E\uDDFE", "Log channel removed", "Log are no longer send in " + channel.getAsMention(), event.getAuthor().getEffectiveAvatarUrl(), false, false, null);
                return;
            }
            //Database
            db.set("logChannel", channel.getId());
            //message
            EmbedBuilder logChannel = new EmbedBuilder()
                    .setAuthor("│ log channel", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().blue)
                    .addField("\uD83E\uDDFE │ Log channel changed", "Log channel changed to **" + channel.getName() + "**", false);
            event.getChannel().sendMessage(logChannel.build()).queue();
            //message in log channel
            EmbedBuilder logChannelInfo = new EmbedBuilder()
                    .setAuthor("│ log channel", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().blue)
                    .addField("\uD83E\uDDFE │ Log channel changed", "Logging actions are now send in here", false);
            channel.sendMessage(logChannelInfo.build()).queue();
        }
    }
}