package com.myra.dev.marian.listeners.welcome.welcomeDirectMessage;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "welcome direct message",
        aliases = {"welcome dm"}
)
public class WelcomeDirectMessageHelp implements Command {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        EmbedBuilder welcomeDirectMessage = new EmbedBuilder()
                .setAuthor("│ welcome direct message", null, event.getAuthor().getEffectiveAvatarUrl())
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome direct message toggle`", "\uD83D\uDD11 │ Toggle welcome images on and off", false)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome direct message message <message>`", "\uD83D\uDCAC │ change the text of the direct messages", false)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome direct message preview`", "\uD83D\uDCF8 │ Displays the current direct message", false);
        event.getChannel().sendMessage(welcomeDirectMessage.build()).queue();
    }
}
