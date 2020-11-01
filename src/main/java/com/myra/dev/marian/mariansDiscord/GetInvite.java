package com.myra.dev.marian.mariansDiscord;

import com.myra.dev.marian.Main;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;

@CommandSubscribe(
        name = "get invite"
)
public class GetInvite implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Check for marian
        if (!ctx.getAuthor().getId().equals(Main.marian)) return;
        // Get invite link to default channel
        String invite = ctx.getEvent().getJDA().getGuildById(ctx.getArguments()[0]).getDefaultChannel().createInvite().setMaxUses(1).complete().getUrl();
        // Send link
        ctx.getChannel().sendMessage(invite).queue();
    }
}
