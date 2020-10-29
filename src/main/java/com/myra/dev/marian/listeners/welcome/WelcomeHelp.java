package com.myra.dev.marian.listeners.welcome;

import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandSubscribe(
        name = "welcome"
)
public class WelcomeHelp implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        //missing permissions
        if (!Permissions.isAdministrator(ctx.getMember())) return;
        // Check for no arguments
        if (ctx.getArguments().length != 0) return;
        // Usage
        EmbedBuilder welcomeUsage = new EmbedBuilder()
                .setAuthor("welcome", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().gray)
                .addField("`" + ctx.getPrefix() + "welcome image`", "\uD83D\uDDBC │ Change the settings for the welcome image", false)
                .addField("`" + ctx.getPrefix() + "welcome embed`", "\uD83D\uDCC7 │ Change the settings for the welcome embed", false)
                .addField("`" + ctx.getPrefix() + "welcome direct message`", "\u2709\uFE0F │ Change the settings for the welcome direct message", false)
                .addField("`" + ctx.getPrefix() + "welcome channel <channel>`", "\uD83D\uDCC1 │ Set the channel, the welcome message will go", false)
                .addField("`" + ctx.getPrefix() + "welcome colour <hex colour>`", "\uD83C\uDFA8 │ Set the colour of the embeds", false);
        ctx.getChannel().sendMessage(welcomeUsage.build()).queue();
    }
}
