package com.myra.dev.marian.commands.administrator;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandSubscribe(
        name = "toggle",
        requires = "administrator"

)
public class Toggle implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        //command usage
        if (ctx.getArguments().length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("toggle", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + ctx.getPrefix() + "toggle <command>`", "\uD83D\uDD11 │ Toggle commands on and off", false);
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
// Toggle commands on or off
        //get command without prefix
        String command;
        if (ctx.getArguments()[0].startsWith(ctx.getPrefix())) {
            command = ctx.getArguments()[0].substring(ctx.getPrefix().length());
        } else command = ctx.getArguments()[0];
        //update database
        new Database(ctx.getGuild()).getCommandManager().toggle(command, ctx.getEvent());
    }
}

