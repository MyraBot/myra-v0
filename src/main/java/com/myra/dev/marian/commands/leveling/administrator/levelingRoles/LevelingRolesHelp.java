package com.myra.dev.marian.commands.leveling.administrator.levelingRoles;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "leveling roles",
        aliases = {"leveling role"}
)
public class LevelingRolesHelp implements Command {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        // Check for no arguments
        if (arguments.length != 0) return;
        // Usage
        EmbedBuilder usage = new EmbedBuilder()
                .setAuthor("leveling roles", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().gray)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "leveling roles add <level> <role> [remove]`", "\uD83D\uDD17 │ Link a role to a level", false)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "leveling roles remove <role>`", "\uD83D\uDDD1 │ Delete the linking between a level and a role", false)
                .addField("`" + Prefix.getPrefix(event.getGuild()) + "leveling roles list`", "\uD83D\uDCC3 │ Shows you all linked up roles", false);
        event.getChannel().sendMessage(usage.build()).queue();
    }
}
