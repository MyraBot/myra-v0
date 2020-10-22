package com.myra.dev.marian.listeners.welcome.welcomeImage;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

@CommandSubscribe(
        name = "welcome image background",
        aliases = {"welcome image image"}
)
public class WelcomeImageBackground implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Missing permissions
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Usage
        if (arguments.length != 1) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("welcome image background", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome image background <url>`", "\uD83D\uDDBC │ Change the background of the welcome images", false);
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * set background image
         */
        //invalid url
        try {
            ImageIO.read(new URL(arguments[0]));
        } catch (IOException e) {
           utilities.error(event.getChannel(),
                    "welcome image background",
                    "\uD83D\uDDBC",
                    "Invalid background URL",
                    "Please try another image",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //save in database
        new Database(event.getGuild()).getNested("welcome").set("welcomeImageBackground", arguments[0]);
        //success
      utilities.success(event.getChannel(),
                "welcome image background",
                "\uD83D\uDDBC",
                "Changed welcome image background",
                "The background has been changed to:",
                event.getAuthor().getEffectiveAvatarUrl(),
                false, arguments[0]);
    }
}
