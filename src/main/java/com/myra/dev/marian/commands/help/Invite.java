package com.myra.dev.marian.commands.help;

import com.myra.dev.marian.utilities.CommandEmbeds;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "invite"
)
public class Invite implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        CommandEmbeds commandEmbeds = new CommandEmbeds();
        event.getChannel().sendMessage(commandEmbeds.inviteJda(event.getJDA(), event.getAuthor().getEffectiveAvatarUrl()).build()).queue();
    }
}
