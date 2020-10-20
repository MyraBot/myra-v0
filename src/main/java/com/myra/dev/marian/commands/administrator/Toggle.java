package com.myra.dev.marian.commands.administrator;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "toggle"
)
public class Toggle implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        //command usage
        if (arguments.length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("│ toggle", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "toggle <command>`", "\uD83D\uDD11 │ Toggle commands on and off", false);
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * toggle commands of and on
         */
        //get command without prefix
        String command;
        if (arguments[0].startsWith(Prefix.getPrefix(event.getGuild()))) {
            command = arguments[0].substring(Prefix.getPrefix(event.getGuild()).length());
        } else command = arguments[0];
        //update database
        new Database(event.getGuild()).getCommandManager().toggle(command, event);
    }
}

