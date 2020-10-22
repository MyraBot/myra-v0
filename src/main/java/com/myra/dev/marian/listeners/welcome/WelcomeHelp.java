package com.myra.dev.marian.listeners.welcome;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "welcome"
)
public class WelcomeHelp implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        // Check for no arguments
        if (arguments.length != 0) return;
        // Usage
        EmbedBuilder welcomeUsage = new EmbedBuilder()
                .setAuthor("welcome", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().gray)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome image`", "\uD83D\uDDBC │ Change the settings for the welcome image", false)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome embed`", "\uD83D\uDCC7 │ Change the settings for the welcome embed", false)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome direct message`", "\u2709\uFE0F │ Change the settings for the welcome direct message", false)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome channel <channel>`", "\uD83D\uDCC1 │ Set the channel, the welcome message will go", false)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome colour <hex colour>`", "\uD83C\uDFA8 │ Set the colour of the embeds", false);
        event.getChannel().sendMessage(welcomeUsage.build()).queue();
    }
}
