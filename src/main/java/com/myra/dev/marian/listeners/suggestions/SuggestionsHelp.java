package com.myra.dev.marian.listeners.suggestions;

import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandSubscribe(
        name = "suggestions"
)
public class SuggestionsHelp implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Missing permission
        if (!Permissions.isAdministrator(ctx.getMember())) return;
        //usage
        if (ctx.getArguments().length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("suggestions", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + ctx.getPrefix() + "suggestions toggle`", "\uD83D\uDD11 │ Toggle suggestions on and off", false)
                    .addField("`" + ctx.getPrefix() + "suggestions channel <channel>`", "\uD83D\uDCC1 │ Set the channel in which the suggestions should go", false)
                    .addField("`" + ctx.getPrefix() + "suggest <suggestion>`", "\uD83D\uDDF3 │ Suggest something", false);
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
    }
}
