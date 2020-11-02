package com.myra.dev.marian.commands.help;

import com.myra.dev.marian.APIs.TopGG;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandSubscribe(
        name = "vote",
        aliases = {"v", "top.gg"}
)
public class Vote implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Check for no arguments
        if (ctx.getArguments().length != 0) return;
        EmbedBuilder vote = new EmbedBuilder()
                .setAuthor("vote", "https://top.gg/bot/718444709445632122/vote", ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().blue)
                .setDescription("You want to " + Manager.getUtilities().hyperlink("vote", "https://top.gg/bot/718444709445632122/vote") + " for me? That would be awesome!\nCurrent votes: `" + TopGG.getUpVotes() + "`");
        ctx.getChannel().sendMessage(vote.build()).queue();
    }
}
