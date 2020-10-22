package com.myra.dev.marian.listeners.suggestions;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;

@CommandSubscribe(
        name = "suggest"
)
public class SubmitSuggestion implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Get database
        Database db = new Database(event.getGuild());
        //check if feature is disabled
        if (!db.getListenerManager().check("suggestions")) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Usage
        if (arguments.length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("suggest", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "suggest <suggestion>`", "\uD83D\uDDF3 │ Suggest something", false);
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * Submit suggestion
         */
        //if no channel is set
        if (db.get("suggestionsChannel").equals("not set")) {
            utilities.error(event.getChannel(), "suggestions", "\uD83D\uDCA1", "No suggestion channel specified", "To set a suggestion channel type in `" + Prefix.getPrefix(event.getGuild()) + "suggestions channel <channel>`", event.getGuild().getIconUrl());
            return;
        }
        // Get suggestion
        String suggestion = "";
        for (int i = 0; i < arguments.length; i++) {
            suggestion += arguments[i] + " ";
        }
        //remove last space
        suggestion = suggestion.substring(0, suggestion.length() - 1);
        //send suggestion
        event.getGuild().getTextChannelById(db.get("suggestionsChannel")).sendMessage(
                new EmbedBuilder()
                        .setAuthor("suggestion by " + event.getAuthor().getAsTag(), event.getMessage().getJumpUrl(), event.getGuild().getIconUrl())
                        .setColor(utilities.getMemberRoleColour(event.getMember()))
                        .setThumbnail(event.getAuthor().getEffectiveAvatarUrl())
                        .setDescription(suggestion)
                        .setTimestamp(Instant.now())
                        .build()
        ).queue((message) -> {
            //add reactions
            message.addReaction("\uD83D\uDC4D").queue();
            message.addReaction("\uD83D\uDC4E").queue();
        });
    }
}
