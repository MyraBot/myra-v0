package com.myra.dev.marian.listeners.welcome.welcomeImage;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "welcome image"
)
public class WelcomeImageHelp implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        // Check for no arguments
        if (arguments.length != 0) return;
        // Usage
        EmbedBuilder usage = new EmbedBuilder()
                .setAuthor("welcome image", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().gray)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome image toggle`", "\uD83D\uDD11 │ Toggle welcome images on and off", false)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome image background <url>`", "\uD83D\uDDBC │ Change the background of the welcome images", false)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome image font`", "\uD83D\uDDDB │ Change the font of the text", false)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome image preview`", "\uD83D\uDCF8 │ Displays the current welcome image", false);
        event.getChannel().sendMessage(usage.build()).queue();
        return;
    }
}
