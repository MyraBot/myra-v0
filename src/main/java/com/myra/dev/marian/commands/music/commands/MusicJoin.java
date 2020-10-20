package com.myra.dev.marian.commands.music.commands;

import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
@CommandSubscribe(
        name = "join"
)
public class MusicJoin implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //already joined voice call
        if (event.getGuild().getAudioManager().isConnected()) {
            Manager.getUtilities().error(
                    event.getChannel(),
                    "join", "\uD83D\uDCE5",
                    "I can only be in one channel at a time",
                    "I´m already connected to **" + event.getGuild().getAudioManager().getConnectedChannel().getName() + "**",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //member didn´t joined voice call yet
        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            Manager.getUtilities().error(
                    event.getChannel(),
                    "join", "\uD83D\uDCE5",
                    "Please join a voice channel first",
                    "In order for me to join a voice channel, you must already be connected to a voice channel",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //bot is missing permissions to connect
        if (!event.getGuild().getSelfMember().hasPermission(event.getMember().getVoiceState().getChannel(), Permission.VOICE_CONNECT)) {
            Manager.getUtilities().error(
                    event.getChannel(),
                    "join", "\uD83D\uDCE5",
                    "I´m missing permissions to join your voice channel",
                    "please give me the permission `Connect` under the `VOICE PERMISSIONS` category",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //joined voice channel
        event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
        Manager.getUtilities().success(event.getChannel(),
                "join", "\uD83D\uDCE5",
                "Joined voice channel",
                "I joined **" + event.getMember().getVoiceState().getChannel().getName() + "**",
                event.getAuthor().getEffectiveAvatarUrl(),
                false, false, null);
    }
}
