package com.myra.dev.marian.listeners.welcome;

import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.database.Prefix;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
@CommandSubscribe(
        name = "welcome"
)
public class WelcomeHelp implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //missing permissions
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            return;
        }
        String[] sentMessage = event.getMessage().getContentRaw().split("\\s+");
        //arguments
        String[] channel = {"channel"};
        String[] image = {"image"};
        String[] embed = {"embed"};
        String[] direct = {"direct"};
        String[] message = {"message", "text"};
        String[] colour = {"colour"};
        String[] background = {"background"};
        /**
         * usage
         */
        //welcome usage
        if (sentMessage.length == 1) {
            EmbedBuilder welcomeUsage = new EmbedBuilder()
                    .setAuthor("│ welcome", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome image`", "\uD83D\uDDBC │ Change the settings for the welcome image", false)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome embed`", "\uD83D\uDCC7 │ Change the settings for the welcome embed", false)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome direct message`", "\u2709\uFE0F │ Change the settings for the welcome direct message", false)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome channel <channel>`", "\uD83D\uDCC1 │ Set the channel, the welcome message will go", false)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome colour <hex colour>`", "\uD83C\uDFA8 │ Set the colour of the embeds", false);
            event.getChannel().sendMessage(welcomeUsage.build()).queue();
            return;
        }
        //welcome channel usage
        if (sentMessage.length == 2 && Arrays.stream(channel).anyMatch(sentMessage[1]::equalsIgnoreCase)) {
            EmbedBuilder welcomeChannelUsage = new EmbedBuilder()
                    .setAuthor("│ welcome channel", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome channel <channel>`", "\uD83D\uDCC1 │ Set the channel, the welcome message will go", true);
            event.getChannel().sendMessage(welcomeChannelUsage.build()).queue();
            return;
        }
        //welcome colour usage
        if (sentMessage.length == 2 && Arrays.stream(colour).anyMatch(sentMessage[1]::equalsIgnoreCase) || sentMessage.length < 3 && Arrays.stream(colour).anyMatch(sentMessage[1]::equalsIgnoreCase)) {
            EmbedBuilder welcomeChannelUsage = new EmbedBuilder()
                    .setAuthor("│ welcome colour", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome colour <hex colour>`", "\uD83C\uDFA8 │ Set the colour of the embeds", false);
            event.getChannel().sendMessage(welcomeChannelUsage.build()).queue();
            return;
        }

        /**
         * welcome image usage
         */
        if (Arrays.stream(image).anyMatch(sentMessage[1]::equalsIgnoreCase)) {
            //welcome usage
            if (sentMessage.length == 2) {
                EmbedBuilder usage = new EmbedBuilder()
                        .setAuthor("│ welcome image", null, event.getAuthor().getEffectiveAvatarUrl())
                        .setColor(Manager.getUtilities().gray)
                        .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome image toggle`", "\uD83D\uDD11 │ Toggle welcome images on and off", false)
                        .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome image background <url>`", "\uD83D\uDDBC │ Change the background of the welcome images", false)
                        .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome image font`", "\uD83D\uDDDB │ Change the font of the text", false)
                        .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome image preview`", "\uD83D\uDCF8 │ Displays the current welcome image", false);
                event.getChannel().sendMessage(usage.build()).queue();
                return;
            }
        }
        /**
         * welcome embed usage
         */
        if (Arrays.stream(embed).anyMatch(sentMessage[1]::equals)) {
            if (sentMessage.length == 2) {
                EmbedBuilder welcomeEmbed = new EmbedBuilder()
                        .setAuthor("│ welcome embed", null, event.getAuthor().getEffectiveAvatarUrl())
                        .setColor(Manager.getUtilities().gray)
                        .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome embed toggle`", "\uD83D\uDD11 │ Toggle welcome embeds on and off", false)
                        .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome embed message <message>`", "\uD83D\uDCAC │ Set the text of the embed message", false)
                        .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome embed preview`", "\uD83D\uDCF8 │ Displays the current embed", false);
                event.getChannel().sendMessage(welcomeEmbed.build()).queue();
                return;
            }
            //welcome embed message usage
            if (sentMessage.length == 3 && Arrays.stream(message).anyMatch(sentMessage[2]::equalsIgnoreCase)) {
                EmbedBuilder welcomeEmbedMessage = new EmbedBuilder()
                        .setAuthor("│ welcome embed message", null, event.getAuthor().getEffectiveAvatarUrl())
                        .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome embed message <message>`", "\uD83D\uDCAC │ Set the text of the embed message", false)
                        .setFooter("{user} = mention the user │ {server} = server name │ {count} = user count");
                event.getChannel().sendMessage(welcomeEmbedMessage.build()).queue();
                return;
            }
        }
    }
}
