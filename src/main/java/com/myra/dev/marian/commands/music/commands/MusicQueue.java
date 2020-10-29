package com.myra.dev.marian.commands.music.commands;

import com.myra.dev.marian.APIs.LavaPlayer.PlayerManager;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@CommandSubscribe(
        name = "queue",
        aliases = {"songs", "tracks"}
)
public class MusicQueue implements Command {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Check for no arguments
        if (arguments.length != 0) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
// Errors
        // Bot isn't connected to a voice channel
        if (!event.getGuild().getAudioManager().isConnected()) {
            utilities.error(event.getChannel(), "shuffle queue", "\uD83D\uDCE4", "I'm not connected to a voice channel", "Use `" + Prefix.getPrefix(event.getGuild()) + "join` to connect me to your voice channel", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // No audio track is playing
        if (PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer.getPlayingTrack() == null) {
            utilities.error(event.getChannel(), "shuffle queue", "\uD83C\uDFB2", "The player isn`t playing any song", "Use `" + Prefix.getPrefix(event.getGuild()) + "play <song>` to play a song", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
// Send Queue
        // Get queue
        BlockingQueue<AudioTrack> queue = PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.getQueue();
        // Get the first 15 audio tracks
        int trackCount = Math.min(queue.size(), 15);
        List<AudioTrack> tracks = new ArrayList<>(queue);
        String songs = "";
        for (int i = 0; i < trackCount; i++) {
            songs += ("\n• " + tracks.get(i).getInfo().title);
        }
        // If there are no songs queued
        if (songs.equals("")) {
            songs = "none \uD83D\uDE14";
        }
        // Get audio player
        AudioPlayer audioPlayer = PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer;
        // Get current playing Song
        String currentPlaying = utilities.hyperlink(audioPlayer.getPlayingTrack().getInfo().title, audioPlayer.getPlayingTrack().getInfo().uri);

        EmbedBuilder queuedSongs = new EmbedBuilder()
                .setAuthor("queue", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().blue)
                .addField("\uD83D\uDCC3 │ queued songs", songs, false)
                .addField("\uD83D\uDCDA │ total songs", Integer.toString(queue.size()), false)
                .addField("\uD83D\uDCBF │ current playing", currentPlaying, false);
        event.getChannel().sendMessage(queuedSongs.build()).queue();
    }
}