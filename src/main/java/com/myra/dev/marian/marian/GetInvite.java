package com.myra.dev.marian.marian;

import com.myra.dev.marian.Bot;
import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;

@CommandSubscribe(
        name = "get invite"
)
public class GetInvite implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Check for marian
        if (!ctx.getAuthor().getId().equals(Bot.marian)) return;
        // Get invite link to default channel
        String invite = ctx.getEvent().getJDA().getGuildById(ctx.getArguments()[0]).getDefaultChannel().createInvite().setMaxUses(1).complete().getUrl();
        // Send link
        ctx.getChannel().sendMessage(invite).queue();
    }
}
