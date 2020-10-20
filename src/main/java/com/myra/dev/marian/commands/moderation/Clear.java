package com.myra.dev.marian.commands.moderation;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

@CommandSubscribe(
        name = "clear",
        aliases = {"purge", "delete"}
)
public class Clear implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // If amount isn't a number
        if (!arguments[0].matches("\\d+")) return;
        // Missing permissions
        if (!Permissions.isModerator(event.getMember())) return;
        // Get utilities
        Utilities utilities = new Utilities();
        // Usage
        if (arguments.length != 1) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("clear", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "clear <amount>`", "\uD83D\uDDD1 │ clear", true);
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        /**
         * Clear messages
         */
        // Delete messages
        try {
            // Retrieve messages
            List<Message> messages = event.getChannel().getHistory().retrievePast(Integer.parseInt(arguments[0]) + 1).complete();
            // Delete messages
            event.getChannel().deleteMessages(messages).queue();
            // Success information
            utilities.success(event.getChannel(), "clear", "\uD83D\uDDD1", "the message were deleted successfully", "`" + arguments[0] + "` messages have been deleted", event.getJDA().getSelfUser().getEffectiveAvatarUrl(), true, false, null);
        }
        // Errors
        catch (Exception exception) {
            //to many messages
            if (arguments[0].equals("0") || exception.toString().startsWith("java.lang.IllegalArgumentException: Message retrieval")) {
                utilities.error(event.getChannel(), "clear", "\uD83D\uDDD1", "Invalid amount of messages", "An amount between 1 and 100 messages can be deleted", event.getAuthor().getEffectiveAvatarUrl());
            }
            //message too late
            else {
                utilities.error(event.getChannel(), "clear", "\uD83D\uDDD1", "You selected too old messages", "I can´t delete messages older than 2 weeks", event.getAuthor().getEffectiveAvatarUrl());
            }
        }
    }
}
