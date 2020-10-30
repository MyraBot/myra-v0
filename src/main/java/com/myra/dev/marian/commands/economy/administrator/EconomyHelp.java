package com.myra.dev.marian.commands.economy.administrator;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandSubscribe(
        name = "economy"
)
public class EconomyHelp implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Missing permissions
        if (!Permissions.isAdministrator(ctx.getMember())) return;
        // Usage
        if (ctx.getArguments().length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("economy", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + ctx.getPrefix() + "economy set <user> <balance>`", "\uD83D\uDC5B │ Change a users balance", false)
                    .addField("`" + ctx.getPrefix() + "economy currency <currency>`", new Database(ctx.getGuild()).getNested("economy").get("currency") + " │ Set a custom currency", false);
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
    }
}
