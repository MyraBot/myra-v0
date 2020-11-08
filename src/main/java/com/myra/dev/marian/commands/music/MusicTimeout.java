package com.myra.dev.marian.commands.music;

import com.myra.dev.marian.management.Events;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;

import java.util.concurrent.TimeUnit;

public class MusicTimeout extends Events {
    @Override
    public void voiceChannelLeave(GuildVoiceLeaveEvent event) throws Exception {
        // User left same voice channel as bot
        if (event.getChannelLeft().equals(event.getGuild().getAudioManager().getConnectedChannel())) {
            // The bot is lonely or only bots are in the voice channel
            if (event.getChannelLeft().getMembers().size() == 1) return;
            // If there is still a user in the channel
            for (Member member : event.getChannelLeft().getMembers()) {
                if (!member.getUser().isBot()) return;
            }
            TimeUnit.SECONDS.sleep(30);


            // User left same voice channel as bot
            if (event.getChannelLeft().equals(event.getGuild().getAudioManager().getConnectedChannel())) {
                // The bot is lonely or only bots are in the voice channel
                if (event.getChannelLeft().getMembers().size() == 1) return;
                // If there is still a user in the channel
                for (Member member : event.getChannelLeft().getMembers()) {
                    if (!member.getUser().isBot()) return;
                }
                event.getGuild().getAudioManager().closeAudioConnection();
            }
        }
    }

    @Override
    public void voiceChannelMove(GuildVoiceMoveEvent event) throws Exception {
        try {
            //user left same voice channel as bot and only bot is in the channel
            if (event.getChannelLeft().equals(event.getGuild().getAudioManager().getConnectedChannel()) && event.getChannelLeft().getMembers().size() == 1) {
                TimeUnit.SECONDS.sleep(30);
                if (event.getChannelLeft().equals(event.getGuild().getAudioManager().getConnectedChannel()) && event.getChannelLeft().getMembers().size() == 1) {
                    event.getGuild().getAudioManager().closeAudioConnection();
                }
            }
            //bot joins lonely channel
            if (event.getChannelJoined().getMembers().size() == 1) {
                TimeUnit.SECONDS.sleep(30);
                if (event.getChannelJoined().getMembers().size() == 1) {
                    event.getGuild().getAudioManager().closeAudioConnection();
                }
            }
        } catch (Exception e) {
        }
    }
}
