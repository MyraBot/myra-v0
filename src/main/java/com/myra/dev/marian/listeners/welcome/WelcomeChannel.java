package com.myra.dev.marian.listeners.welcome;


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
        name = "welcome channel"
)
public class WelcomeChannel implements Command {


    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Missing permissions
        if (!Permissions.isAdministrator(ctx.getMember())) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Usage
        if (ctx.getArguments().length != 1) {
            EmbedBuilder welcomeChannelUsage = new EmbedBuilder()
                    .setAuthor("welcome channel", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + ctx.getPrefix() + "welcome channel <channel>`", "\uD83D\uDCC1 │ Set the channel, the welcome message will go", true);
            ctx.getChannel().sendMessage(welcomeChannelUsage.build()).queue();
            return;
        }
        /**
         * Change welcome channel
         */
        //get channel
        TextChannel channel = utilities.getTextChannel(ctx.getEvent(), ctx.getArguments()[0], "welcome channel", "\uD83D\uDCC1");
        if (channel == null) return;
        // Get database
        Database db = new Database(ctx.getGuild());
        // Get current welcome channel
        String currentChannelId = db.getNested("welcome").get("welcomeChannel");
        //remove welcome channel
        if (currentChannelId.equals(channel.getId())) {
            //remove channel id
            db.getNested("welcome").set("welcomeChannel", "not set");
            //success
            utilities.success(ctx.getChannel(), "welcome channel", "\uD83D\uDCC1", "Welcome channel removed", "Welcome are no longer send in " + channel.getAsMention(), ctx.getAuthor().getEffectiveAvatarUrl(), false, null);
            return;
        }
        // Update database
        db.getNested("welcome").set("welcomeChannel", channel.getId());
        // Success message
        utilities.success(ctx.getChannel(), "welcome channel", "\uD83D\uDCC1", "Welcome channel changed", "Welcome messages are now send in " + channel.getAsMention(), ctx.getAuthor().getEffectiveAvatarUrl(), false, null);
        // Success message in welcome channel
        EmbedBuilder logChannelInfo = new EmbedBuilder()
                .setAuthor("welcome channel", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(utilities.blue)
                .addField("\uD83D\uDCC1 │ welcome channel changed", "welcome actions are now send in here", false);
        channel.sendMessage(logChannelInfo.build()).queue();
    }
}
