package com.myra.dev.marian.commands.moderation;

import com.myra.dev.marian.utilities.CommandEmbeds;

import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;

@CommandSubscribe(
        name = "moderation",
        aliases = {"mod"},
        requires = Permissions.MODERATOR
)
public class ModerationHelp implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Send message
        ctx.getChannel().sendMessage(new CommandEmbeds(ctx.getGuild(), ctx.getEvent().getJDA(), ctx.getAuthor(), ctx.getPrefix()).moderation().build()).queue();
    }
}