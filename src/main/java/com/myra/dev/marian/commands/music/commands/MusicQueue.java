package com.myra.dev.marian.commands.music.commands;

import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.APIs.LavaPlayer.PlayerManager;
import com.myra.dev.marian.database.Prefix;
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
        //not connected to a voice channel
        if (!event.getGuild().getAudioManager().isConnected()) {
            Manager.getUtilities().error(
                    event.getChannel(),
                    "shuffle queue", "\uD83D\uDCE4",
                    "I'm not connected to a voice channel",
                    "Use `" + Prefix.getPrefix(event.getGuild()) + "join` to connect me to your voice channel",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //if no track is playing
        if (PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer.getPlayingTrack() == null) {
            Manager.getUtilities().error(
                    event.getChannel(),
                    "shuffle queue", "\uD83C\uDFB2",
                    "The player isn`t playing any song",
                    "Use `" + Prefix.getPrefix(event.getGuild()) + "play <song>` to play a song",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // Get queue
        BlockingQueue<AudioTrack> queue = PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.getQueue();
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

        EmbedBuilder queuedSongs = new EmbedBuilder()
                .setAuthor("queue", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().blue)
                .addField("\uD83D\uDCC3 │ queued songs", songs, false)
                .addField("\uD83D\uDCDA │ total songs", Integer.toString(queue.size()), false);
        event.getChannel().sendMessage(queuedSongs.build()).queue();
    }
}