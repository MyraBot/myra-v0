package com.myra.dev.marian.listeners.suggestions;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Return;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "suggestions channel"
)
public class SuggestionsChannel implements Command {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        // get utilities
        Utilities utilities = Manager.getUtilities();
        // Usage
        if (arguments.length != 1) {
            // Usage
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("suggestions", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "suggestions channel <channel>`", "\uD83D\uDCC1 │ Set the channel in which the suggestions should go", false);
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }

        //connect to database
        Database db = new Database(event.getGuild());
        // Get given channel
        TextChannel channel = utilities.getTextChannel(event, arguments[0], "suggestions", "\uD83D\uDDF3");
        if (channel == null) return;
        //remove suggestions channel
        if (db.get("suggestionsChannel").equals(channel.getId())) {
            // Success
            utilities.success(event.getChannel(),
                    "suggestions", "\uD83D\uDDF3",
                    "removed suggestions channel",
                    "Suggestions are no longer sent in " + event.getGuild().getTextChannelById(db.get("suggestionsChannel")).getAsMention(),
                    event.getAuthor().getEffectiveAvatarUrl(),
                    false, null);
            // Update database
            db.set("suggestionsChannel", "not set");
            return;
        }
        // Update database
        db.set("suggestionsChannel", channel.getId());
        //success message
        Manager.getUtilities().success(event.getChannel(),
                "suggestions", "\uD83D\uDDF3",
                "Suggestions channel changed",
                "Suggestions are now sent in " + channel.getAsMention(),
                event.getAuthor().getEffectiveAvatarUrl(),
                false, null);
        EmbedBuilder success = new EmbedBuilder()
                .setAuthor("suggestions channel", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().blue)
                .addField("\uD83D\uDDF3 │ Notification channel changed", "Suggestions are now send in here", false);
        channel.sendMessage(success.build()).queue();
    }
}
