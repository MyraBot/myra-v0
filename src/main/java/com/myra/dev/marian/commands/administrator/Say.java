package com.myra.dev.marian.commands.administrator;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "say",
        aliases = {"write", "sag mal bitte"}
)
public class Say implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        //command usage
        if (arguments.length == 0) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("say", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "say <message>`", "\uD83D\uDCAC │ Let the bot say something", true);
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        /**
         * write message
         */
        //get arguments
        String message = "";
        for (int i = 0; i < arguments.length; i++) {
            message += arguments[i] + " ";
        }
        //remove last space
        message = message.substring(0, message.length() - 1);
        //delete command
        event.getMessage().delete().queue();
        //send message
        event.getChannel().sendMessage(message).queue();
    }
}