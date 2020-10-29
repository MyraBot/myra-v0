package com.myra.dev.marian.commands.moderation;

import com.myra.dev.marian.utilities.CommandEmbeds;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.commands.CommandContext;

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
        ctx.getChannel().sendMessage(
                CommandEmbeds.moderation(
                        ctx.getGuild(),
                        ctx.getAuthor().getEffectiveAvatarUrl())
                        .build())
                .queue();
    }
}