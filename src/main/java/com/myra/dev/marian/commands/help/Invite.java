package com.myra.dev.marian.commands.help;

import com.myra.dev.marian.utilities.CommandEmbeds;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.commands.CommandContext;

@CommandSubscribe(
        name = "invite"
)
public class Invite implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        CommandEmbeds commandEmbeds = new CommandEmbeds();
        ctx.getChannel().sendMessage(commandEmbeds.inviteJda(ctx.getEvent().getJDA(), ctx.getAuthor().getEffectiveAvatarUrl()).build()).queue();
    }
}
