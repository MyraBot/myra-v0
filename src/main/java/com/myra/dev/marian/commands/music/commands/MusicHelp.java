package com.myra.dev.marian.commands.music.commands;

import com.myra.dev.marian.utilities.CommandEmbeds;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "music",
        aliases = {"radio"}
)
public class MusicHelp implements Command {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        event.getChannel().sendMessage(new CommandEmbeds().music(event.getGuild(), event.getAuthor().getEffectiveAvatarUrl()).build()).queue();
    }
}
