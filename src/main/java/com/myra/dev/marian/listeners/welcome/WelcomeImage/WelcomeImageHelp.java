package com.myra.dev.marian.listeners.welcome.WelcomeImage;

import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandSubscribe(
        name = "welcome image"
)
public class WelcomeImageHelp implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Missing permissions
        if (!Permissions.isAdministrator(ctx.getMember())) return;
        // Check for no arguments
        if (ctx.getArguments().length != 0) return;
        // Usage
        EmbedBuilder usage = new EmbedBuilder()
                .setAuthor("welcome image", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().gray)
                .addField("`" + ctx.getPrefix() + "welcome image toggle`", "\uD83D\uDD11 │ Toggle welcome images on and off", false)
                .addField("`" + ctx.getPrefix() + "welcome image background <url>`", "\uD83D\uDDBC │ Change the background of the welcome images", false)
                .addField("`" + ctx.getPrefix() + "welcome image font`", "\uD83D\uDDDB │ Change the font of the text", false)
                .addField("`" + ctx.getPrefix() + "welcome image preview`", "\uD83D\uDCF8 │ Displays the current welcome image", false);
        ctx.getChannel().sendMessage(usage.build()).queue();
        return;
    }
}
