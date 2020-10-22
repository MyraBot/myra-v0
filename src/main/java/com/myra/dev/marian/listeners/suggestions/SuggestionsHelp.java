package com.myra.dev.marian.listeners.suggestions;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "suggestions"
)
public class SuggestionsHelp implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Missing permission
        if (!Permissions.isAdministrator(event.getMember())) return;
        //usage
        if (arguments.length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("suggestions", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "suggestions toggle`", "\uD83D\uDD11 │ Toggle suggestions on and off", false)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "suggestions channel <channel>`", "\uD83D\uDCC1 │ Set the channel in which the suggestions should go", false)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "suggest <suggestion>`", "\uD83D\uDDF3 │ Suggest something", false);
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
    }
}
