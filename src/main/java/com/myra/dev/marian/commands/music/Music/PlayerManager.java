package com.myra.dev.marian.commands.music.Music;

import com.myra.dev.marian.utilities.management.Manager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final AudioPlayerManager TrackManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    private PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.TrackManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(TrackManager);
        AudioSourceManagers.registerRemoteSources(TrackManager);
    }

    public synchronized GuildMusicManager getGuildMusicManger(Guild guild) {
        Long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(TrackManager);
            musicManagers.put(guildId, musicManager);
        }
        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(TextChannel channel, String trackUrl, String authorAvatar, String thumbnailUrl) throws Exception {

        GuildMusicManager musicManager = getGuildMusicManger(channel.getGuild());

        TrackManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                Manager.getUtilities().success(channel,
                        "play", "\uD83D\uDCBF",
                        "Added track to queue", "**" + Manager.getUtilities().hyperlink(track.getInfo().title, trackUrl) + "** has been added to the queue",
                        authorAvatar,
                        false, true, thumbnailUrl);
                play(musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().remove(0);
                }
                Manager.getUtilities().success(channel,
                        "play", "\uD83D\uDCBF",
                        "Added playlist to queue",
                        "The playlist **" + Manager.getUtilities().hyperlink(playlist.getName(), trackUrl) + "** has been added to the queue",
                        authorAvatar,
                        false, false, null);
                play(musicManager, firstTrack);
                playlist.getTracks().forEach(musicManager.scheduler::queue);
            }

            @Override
            public void noMatches() {
                Manager.getUtilities().error(
                        channel,
                        "play", "\uD83D\uDCBF",
                        "Track not found",
                        "Nothing found by " + trackUrl,
                        authorAvatar
                );
            }

            @Override
            public void loadFailed(FriendlyException e) {
                Manager.getUtilities().error(
                        channel,
                        "play", "\uD83D\uDCBF",
                        "Could not play the track",
                        e.getMessage(),
                        authorAvatar
                );
            }
        });
    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public static synchronized PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
}