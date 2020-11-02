package com.myra.dev.marian.commands.moderation;

import com.myra.dev.marian.utilities.CommandEmbeds;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;

@CommandSubscribe(
        name = "moderation",
        aliases = {"mod"}
)
public class ModerationHelp implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Missing permissions
        if (!Permissions.isModerator(ctx.getMember())) return;
        // Send message
        ctx.getChannel().sendMessage(new CommandEmbeds(ctx.getGuild(), ctx.getEvent().getJDA(), ctx.getAuthor(), ctx.getPrefix()).moderation().build()).queue();
    }
}