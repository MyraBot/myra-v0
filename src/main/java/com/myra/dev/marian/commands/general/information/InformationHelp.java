package com.myra.dev.marian.commands.general.information;

import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.database.Prefix;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
@CommandSubscribe(
        name = "information",
        aliases = {"info"}
)
public class InformationHelp implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //check for no arguments
        if (arguments.length != 0) return;
        EmbedBuilder usage = new EmbedBuilder()
                .setAuthor("information", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().gray)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "information user <user>`", "\uD83D\uDC64 │ Gives you information about a specific user", false)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "information server`", "\uD83D\uDDFA │ Gives you information about the server", false)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "information bot`", "\uD83D\uDD0C │ Gives you information about the bot", false);
        event.getChannel().sendMessage(usage.build()).queue();
    }
}
