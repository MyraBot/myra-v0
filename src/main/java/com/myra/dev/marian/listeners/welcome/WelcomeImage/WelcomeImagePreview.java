package com.myra.dev.marian.listeners.welcome.WelcomeImage;

import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.Permission;

@CommandSubscribe(
        name = "welcome image preview"
)
public class WelcomeImagePreview implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        //missing permissions
        if (!ctx.getEvent().getMember().hasPermission(Permission.ADMINISTRATOR)) return;
        // Check for no arguments
        if (ctx.getArguments().length != 0) return;
        // Send 'is typing'
        ctx.getChannel().sendTyping().queue();
        // Send image
        new WelcomeImageRender().welcomeImage(ctx.getGuild(), ctx.getAuthor(), ctx.getChannel());
    }
}
