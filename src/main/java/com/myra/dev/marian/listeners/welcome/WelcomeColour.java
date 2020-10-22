package com.myra.dev.marian.listeners.welcome;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

@CommandSubscribe(
        name = "welcome colour",
        aliases = {"welcome color"}
)
public class WelcomeColour implements Command {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Usage
        if (arguments.length != 1) {
            EmbedBuilder welcomeChannelUsage = new EmbedBuilder()
                    .setAuthor("welcome colour", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome colour <hex colour>`", "\uD83C\uDFA8 │ Set the colour of the embeds", false);
            event.getChannel().sendMessage(welcomeChannelUsage.build()).queue();
            return;
        }
        String hex = null;
        //remove #
        if (arguments[0].startsWith("#")) {
            StringBuilder raw = new StringBuilder(arguments[0]);
            raw.deleteCharAt(0);
            hex = "0x" + raw.toString();
        }
        //add 0x
        else {
            hex = "0x" + arguments[0];
        }
        //if colour doesn't exist
        try {
            Color.decode(hex);
        } catch (Exception e) {
            utilities.error(event.getChannel(), "welcome embed colour", "\uD83C\uDFA8", "Invalid colour", "The given colour doesn't exist", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //save in database
        new Database(event.getGuild()).getNested("welcome").set("welcomeColour", hex);
        //success
        utilities.success(event.getChannel(), "welcome embed colour", "\uD83C\uDFA8", "Updated Colour", "Colour changed to `" + hex.replace("0x", "#") + "`", event.getAuthor().getEffectiveAvatarUrl(), false, null);
    }
}