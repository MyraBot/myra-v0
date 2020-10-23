package com.myra.dev.marian.commands.leveling.administrator;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
@CommandSubscribe(
        name = "leveling"
)
public class LevelingHelp implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Check for no arguments
        if (arguments.length != 0) return;
        // Send message
        EmbedBuilder help = new EmbedBuilder()
                .setAuthor("leveling", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().gray)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "leveling set <user> <level>`", "\uD83C\uDF96 │ Change the level of a user", false)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "leveling roles add <level> <role> [remove]`", "\uD83C\uDFC5 │ Link a role to a level", false);
        event.getChannel().sendMessage(help.build()).queue();
    }
}
