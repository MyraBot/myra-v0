package com.myra.dev.marian.listeners.suggestions;

import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Return;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
@CommandSubscribe(
        name = "suggestions channel"
)
public class SuggestionsChannel implements Command {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //connect to database
        Database db = new Database(event.getGuild());
        //split message
        String[] sentMessage = event.getMessage().getContentRaw().split("\\s+");

        if (new Return().textChannel(event, sentMessage, 2, "suggestions", "\uD83D\uDDF3") == null) return;
        TextChannel channel = new Return().textChannel(event, sentMessage, 2, "suggestions", "\uD83D\uDDF3");

        //remove suggestions channel
        if (db.get("suggestionsChannel").equals(channel.getId())) {
            //error
            Manager.getUtilities().success(event.getChannel(),
                    "suggestions", "\uD83D\uDDF3",
                    "removed suggestions channel",
                    "Suggestions are no longer sent in " + event.getGuild().getTextChannelById(db.get("suggestionsChannel")).getAsMention(),
                    event.getAuthor().getEffectiveAvatarUrl(),
                    false, false, null);
            //database
            db.set("suggestionsChannel", "not set");
            return;
        }
        //Database
        db.set("suggestionsChannel", channel.getId());
        //success message
        Manager.getUtilities().success(event.getChannel(),
                "suggestions", "\uD83D\uDDF3",
                "Suggestions channel changed",
                "Suggestions are now sent in " + channel.getAsMention(),
                event.getAuthor().getEffectiveAvatarUrl(),
                false, false, null);
        EmbedBuilder success = new EmbedBuilder()
                .setAuthor("suggestions channel", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().blue)
                .addField("\uD83D\uDDF3 │ Notification channel changed", "Suggestions are now send in here", false);
        channel.sendMessage(success.build()).queue();
    }
}
