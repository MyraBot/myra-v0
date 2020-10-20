package com.myra.dev.marian.commands.music.commands;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
@CommandSubscribe(
        name = "leave",
        aliases = {"disconnect"}
)
public class MusicLeave implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //not connected to a voice channel
        if (!event.getGuild().getAudioManager().isConnected()) {
            Manager.getUtilities().error(event.getChannel(),
                    "disconnect", "\uD83D\uDCE4",
                    "I´m not connected to a voice channel",
                    "Use `" + Prefix.getPrefix(event.getGuild()) + "join` to connect me to your voice channel",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //author isn´t in the same voice channel as the bot
        if (!event.getGuild().getAudioManager().getConnectedChannel().getMembers().contains(event.getMember())) {
            Manager.getUtilities().error(event.getChannel(),
                    "disconnect", "\uD83D\uDCE4",
                    "You have to be in the same voice channel as me to use this command",
                    "To kick me you have to be in **" + event.getGuild().getAudioManager().getConnectedChannel().getName() + "**",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //left voice channel
        event.getGuild().getAudioManager().closeAudioConnection();

        Manager.getUtilities().success(event.getChannel(),
                "disconnect", "\uD83D\uDCE4",
                "Left voice channel",
                "I left **" + event.getGuild().getAudioManager().getConnectedChannel().getName() + "**",
                event.getAuthor().getEffectiveAvatarUrl(),
                false, false, null);
    }
}
