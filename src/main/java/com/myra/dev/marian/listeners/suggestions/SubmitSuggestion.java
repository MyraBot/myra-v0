package com.myra.dev.marian.listeners.suggestions;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.database.allMethods.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
@CommandSubscribe(
        name = "suggest"
)
public class SubmitSuggestion implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        Database db = new Database(event.getGuild());
        //check if feature is disabled
        if (!db.getListenerManager().check("suggestions")) return;
        //split message
        String[] sentMessage = event.getMessage().getContentRaw().split("\\s+", 2);
        //if no channel is set
        if (db.get("suggestionsChannel").equals("not set")) {
            Manager.getUtilities().error(event.getChannel(), "suggestions", "\uD83D\uDCA1", "No suggestion channel specified", "To set a suggestion channel type in `" + Prefix.getPrefix(event.getGuild()) + "suggestions channel <channel>`", event.getGuild().getIconUrl());
            return;
        }
        //send suggestion
        event.getGuild().getTextChannelById(db.get("suggestionsChannel")).sendMessage(
                new EmbedBuilder()
                        .setAuthor("suggestion by " + event.getAuthor().getAsTag(), event.getMessage().getJumpUrl(), event.getGuild().getIconUrl())
                        .setColor(Manager.getUtilities().getMemberRoleColour(event.getMember()))
                        .setThumbnail(event.getAuthor().getEffectiveAvatarUrl())
                        .setDescription(sentMessage[1])
                        .setTimestamp(Instant.now())
                        .build()
        ).queue((message) -> {
            //add reactions
            message.addReaction("\uD83D\uDC4D").queue();
            message.addReaction("\uD83D\uDC4E").queue();
        });
    }
}
