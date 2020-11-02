package com.myra.dev.marian.listeners.welcome.welcomeDirectMessage;


import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;

@CommandSubscribe(
        name = "welcome direct message preview",
        aliases = {"welcome dm preview"},
        requires = "administrator"
)
public class WelcomeDirectMessagePreview implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Check for no arguments
        if (ctx.getArguments().length != 0) return;
        // Send message
        new WelcomeDirectMessageRender().embed(ctx.getGuild(), null, ctx.getChannel(), ctx.getAuthor());
    }
}