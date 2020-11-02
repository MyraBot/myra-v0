package com.myra.dev.marian.listeners.welcome.welcomeDirectMessage;


import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandSubscribe(
        name = "welcome direct message",
        aliases = {"welcome dm"},
        requires = "administrator"
)
public class WelcomeDirectMessageHelp implements Command {

    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Check for no arguments
        if (ctx.getArguments().length != 0) return;
        // Usage
        EmbedBuilder welcomeDirectMessage = new EmbedBuilder()
                .setAuthor("welcome direct message", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .addField("`" + ctx.getPrefix() + "welcome direct message toggle`", "\uD83D\uDD11 │ Toggle welcome images on and off", false)
                .addField("`" + ctx.getPrefix() + "welcome direct message message <message>`", "\uD83D\uDCAC │ change the text of the direct messages", false)
                .addField("`" + ctx.getPrefix() + "welcome direct message preview`", "\uD83D\uDCF8 │ Displays the current direct message", false);
        ctx.getChannel().sendMessage(welcomeDirectMessage.build()).queue();
    }
}
