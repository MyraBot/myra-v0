package com.myra.dev.marian.commands.administrator;

import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandSubscribe(
        name = "say",
        aliases = {"write", "sag mal bitte"}
)
public class Say implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        //missing permissions
        if (!Permissions.isAdministrator(ctx.getMember())) return;
        //command usage
        if (ctx.getArguments().length == 0) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("say", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + ctx.getPrefix() + "say <message>`", "\uD83D\uDCAC │ Let the bot say something", true);
            ctx.getChannel().sendMessage(embed.build()).queue();
            return;
        }
// write message
        //get arguments
        String message = "";
        for (int i = 0; i < ctx.getArguments().length; i++) {
            message += ctx.getArguments()[i] + " ";
        }
        //remove last space
        message = message.substring(0, message.length() - 1);
        //delete command
        ctx.getEvent().getMessage().delete().queue();
        //send message
        ctx.getChannel().sendMessage(message).queue();
    }
}