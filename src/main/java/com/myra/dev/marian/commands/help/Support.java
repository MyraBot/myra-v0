package com.myra.dev.marian.commands.help;

import com.myra.dev.marian.utilities.CommandEmbeds;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.commands.CommandContext;

@CommandSubscribe(
        name = "support",
        aliases = {"support server", "bugs"}
)
public class Support implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        //check for no arguments
        if (ctx.getArguments().length != 0) return;
        CommandEmbeds commandEmbeds = new CommandEmbeds();
        ctx.getChannel().sendMessage(commandEmbeds.supportServer(ctx.getEvent().getJDA(), ctx.getAuthor().getEffectiveAvatarUrl()).build()).queue();
    }
}
