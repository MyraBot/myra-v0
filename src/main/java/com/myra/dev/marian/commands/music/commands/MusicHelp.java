package com.myra.dev.marian.commands.music.commands;

import com.myra.dev.marian.utilities.CommandEmbeds;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;

@CommandSubscribe(
        name = "music",
        aliases = {"radio"}
)
public class MusicHelp implements Command {

    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Check for no arguments
        if (ctx.getArguments().length != 0) return;
        // Run command
        ctx.getChannel().sendMessage(new CommandEmbeds().music(ctx.getGuild(), ctx.getAuthor().getEffectiveAvatarUrl()).build()).queue();
    }
}
