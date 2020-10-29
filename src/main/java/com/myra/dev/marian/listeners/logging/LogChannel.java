package com.myra.dev.marian.listeners.logging;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

@CommandSubscribe(
        name = "log channel",
        aliases = {"logging channel", "logs channel"}
)
public class LogChannel implements Command {

    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Missing permission
        if (!Permissions.isAdministrator(ctx.getMember())) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Usage
        if (ctx.getArguments().length != 1) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("log channel", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + ctx.getPrefix() + "log channel <channel>`", "\uD83E\uDDFE │ Set the channel where all logging actions should go", false);
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * Change log channel
         */
        // Get channel
        TextChannel channel = utilities.getTextChannel(ctx.getEvent(), ctx.getArguments()[0], "log channel", "\uD83E\uDDFE");
        if (channel == null) return;
        // Get database
        Database db = new Database(ctx.getGuild());
        // Remove logs channel
        if (channel.getId().equals(db.get("logChannel"))) {
            // Update database
            db.set("logChannel", "not set");
            // Send success message
            utilities.success(ctx.getChannel(), "log channel", "\uD83E\uDDFE", "Log channel removed", "Log are no longer send in " + channel.getAsMention(), ctx.getAuthor().getEffectiveAvatarUrl(), false, null);
            return;
        }
        // Change log channel
        else
            // Update database
            db.set("logChannel", channel.getId());
        // Success message
        EmbedBuilder logChannel = new EmbedBuilder()
                .setAuthor("log channel", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(utilities.blue)
                .addField("\uD83E\uDDFE │ Log channel changed", "Log channel changed to **" + channel.getName() + "**", false);
        ctx.getChannel().sendMessage(logChannel.build()).queue();
        // Success message in the new log channel
        EmbedBuilder logChannelInfo = new EmbedBuilder()
                .setAuthor("log channel", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(utilities.blue)
                .addField("\uD83E\uDDFE │ Log channel changed", "Logging actions are now send in here", false);
        channel.sendMessage(logChannelInfo.build()).queue();
    }
}