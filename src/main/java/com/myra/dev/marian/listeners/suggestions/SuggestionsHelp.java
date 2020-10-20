package com.myra.dev.marian.listeners.suggestions;

import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.Permissions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
@CommandSubscribe(
        name = "suggestions"
)
public class SuggestionsHelp implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //split message
        String[] sentMessage = event.getMessage().getContentRaw().split("\\s+");
        //arguments
        String[] arg1 = {Prefix.getPrefix(event.getGuild()) + "suggestions", Prefix.getPrefix(event.getGuild()) + "suggest"};
        String[] arg2 = {"channel"};
        //check for arguments
        //usage
        if (sentMessage.length == 1) {
            //admin
            if (Permissions.isAdministrator(event.getMember())) {
                EmbedBuilder usage = new EmbedBuilder()
                        .setAuthor("│ suggestions", null, event.getAuthor().getEffectiveAvatarUrl())
                        .setColor(Manager.getUtilities().gray)
                        .addField("`" + Prefix.getPrefix(event.getGuild()) + "suggestions toggle`", "\uD83D\uDD11 │ Toggle suggestions on and off", false)
                        .addField("`" + Prefix.getPrefix(event.getGuild()) + "suggestions channel <channel>`", "\uD83D\uDCC1 │ Set the channel in which the suggestions should go", false)
                        .addField("`" + Prefix.getPrefix(event.getGuild()) + "suggest <suggestion>`", "\uD83D\uDDF3 │ Suggest something", false);
                event.getChannel().sendMessage(usage.build()).queue();
                return;
            }
            //user
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("│ suggestions", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "suggest <suggestion>`", "\uD83D\uDDF3 │ Suggest something", false);
            event.getChannel().sendMessage(usage.build()).queue();
        }
        if (Arrays.stream(arg2).anyMatch(sentMessage[1]::equals) && sentMessage.length == 2 || Arrays.stream(arg2).anyMatch(sentMessage[1]::equals) && sentMessage.length > 3) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("│ suggestions", null, event.getAuthor().getEffectiveAvatarUrl())
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "suggestions channel <channel>`", "\uD83D\uDCC1 │ Set the channel in which the suggestions should go", false);
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
    }
}
