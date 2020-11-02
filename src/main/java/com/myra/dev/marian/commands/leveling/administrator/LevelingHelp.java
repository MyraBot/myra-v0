package com.myra.dev.marian.commands.leveling.administrator;

import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandSubscribe(
        name = "leveling"
)
public class LevelingHelp implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Check for no arguments
        if (ctx.getArguments().length != 0) return;
        // Missing permissions
        if (!Permissions.isAdministrator(ctx.getMember())) return;
        // Send message
        EmbedBuilder help = new EmbedBuilder()
                .setAuthor("leveling", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().gray)
                .addField("`" + ctx.getPrefix() + "leveling set <user> <level>`", "\uD83C\uDFC6 │ Change the level of a user", false)
                .addField("`" + ctx.getPrefix() + "leveling roles`", "\uD83D\uDD17 │ Link a role to a level", false);
        ctx.getChannel().sendMessage(help.build()).queue();
    }
}
