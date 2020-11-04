package com.myra.dev.marian.listeners.welcome.WelcomeImage;

import com.myra.dev.marian.database.allMethods.Database;

import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

@CommandSubscribe(
        name = "welcome image background",
        aliases = {"welcome image image"},
        requires = "administrator"
)
public class WelcomeImageBackground implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Usage
        if (ctx.getArguments().length != 1) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("welcome image background", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + ctx.getPrefix() + "welcome image background <url>`", "\uD83D\uDDBC │ Change the background of the welcome images", false);
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * set background image
         */
        //invalid url
        try {
            ImageIO.read(new URL(ctx.getArguments()[0]));
        } catch (IOException e) {
            utilities.error(ctx.getChannel(),
                    "welcome image background",
                    "\uD83D\uDDBC",
                    "Invalid background URL",
                    "Please try another image",
                    ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //save in database
        new Database(ctx.getGuild()).getNested("welcome").set("welcomeImageBackground", ctx.getArguments()[0], Manager.type.STRING);
        //success
        utilities.success(ctx.getChannel(),
                "welcome image background",
                "\uD83D\uDDBC",
                "Changed welcome image background",
                "The background has been changed to:",
                ctx.getAuthor().getEffectiveAvatarUrl(),
                false, ctx.getArguments()[0]);
    }
}
