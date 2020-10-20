package com.myra.dev.marian.commands.help;

import com.myra.dev.marian.utilities.CommandEmbeds;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "support",
        aliases = {"support server", "bugs"}
)
public class Support implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //check for no arguments
        if (arguments.length != 0) return;
        CommandEmbeds commandEmbeds = new CommandEmbeds();
        event.getChannel().sendMessage(commandEmbeds.supportServer(event.getJDA(), event.getAuthor().getEffectiveAvatarUrl()).build()).queue();
    }
}
