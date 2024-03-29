package com.myra.dev.marian.commands.leveling.administrator.levelingRoles;


import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandSubscribe(
        name = "leveling roles",
        aliases = {"leveling role"},
        requires = Permissions.ADMINISTRATOR
)
public class LevelingRolesHelp implements Command {

    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Check for no arguments
        if (ctx.getArguments().length != 0) return;
        // Usage
        EmbedBuilder usage = new EmbedBuilder()
                .setAuthor("leveling roles", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(Utilities.getUtils().gray)
                .addField("`" + ctx.getPrefix() + "leveling roles add <level> <role> [remove]`", "\uD83D\uDD17 │ Link a role to a level", false)
                .addField("`" + ctx.getPrefix() + "leveling roles remove <role>`", "\uD83D\uDDD1 │ Delete the linking between a level and a role", false)
                .addField("`" + ctx.getPrefix() + "leveling roles list`", "\uD83D\uDCC3 │ Shows you all linked up roles", false);
        ctx.getChannel().sendMessage(usage.build()).queue();
    }
}
