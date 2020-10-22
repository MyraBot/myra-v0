package com.myra.dev.marian.listeners.welcome.welcomeEmbed;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "welcome embed"
)
public class WelcomeEmbedHelp implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Check for no arguments
        if (arguments.length != 0) return;
        // Usage
        EmbedBuilder welcomeEmbed = new EmbedBuilder()
                .setAuthor("welcome embed", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().gray)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome embed toggle`", "\uD83D\uDD11 │ Toggle welcome embeds on and off", false)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome embed message <message>`", "\uD83D\uDCAC │ Set the text of the embed message", false)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome embed preview`", "\uD83D\uDCF8 │ Displays the current embed", false);
        event.getChannel().sendMessage(welcomeEmbed.build()).queue();
        return;
    }
}
