package com.myra.dev.marian.listeners.welcome.welcomeEmbed;

import com.myra.dev.marian.database.allMethods.Database;

import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandSubscribe(
        name = "welcome embed message",
        requires = "administrator"
)
public class WelcomeEmbedMessage implements Command {

    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Utilities
        Utilities utilities = Manager.getUtilities();
        // Usage
        if (ctx.getArguments().length == 0) {
            EmbedBuilder welcomeEmbedMessage = new EmbedBuilder()
                    .setAuthor("welcome embed message", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + ctx.getPrefix() + "welcome embed message <message>`", "\uD83D\uDCAC │ Set the text of the embed message", false)
                    .setFooter("{user} = mention the user │ {server} = server name │ {count} = user count");
            ctx.getChannel().sendMessage(welcomeEmbedMessage.build()).queue();
            return;
        }
        // Get message
        String message = "";
        for (int i = 1; i < ctx.getArguments().length; i++) {
            message += ctx.getArguments()[i] + " ";
        }
        //remove last space
        message = message.substring(0, message.length() - 1);
        // Get database
        Database db = new Database(ctx.getGuild());
        // Update database
        db.getNested("welcome").set("welcomeEmbedMessage", message);
        // Success
        utilities.success(ctx.getChannel(), "welcome embed message", "\uD83D\uDCAC",
                "\uD83D\uDC4B │ welcome text changed to",
                message
                        .replace("{user}", ctx.getAuthor().getAsMention())
                        .replace("{server}", ctx.getGuild().getName())
                        .replace("{count}", Integer.toString(ctx.getGuild().getMemberCount())),
                ctx.getAuthor().getEffectiveAvatarUrl(), false, null
        );
    }
}