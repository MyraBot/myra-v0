package com.myra.dev.marian.commands.music.commands;

import com.myra.dev.marian.APIs.LavaPlayer.PlayerManager;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@CommandSubscribe(
        name = "shuffle",
        aliases = {"random", "randomize"}
)
public class MusicShuffle implements Command {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Check for no arguments
        if (arguments.length != 0) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
// Errors
        // Bot isn't connected to a voice channel
        if (!event.getGuild().getAudioManager().isConnected()) {
            utilities.error(event.getChannel(), "shuffle", "\uD83D\uDCE4", "I'm not connected to a voice channel", "Use `" + Prefix.getPrefix(event.getGuild()) + "join` to connect me to your voice channel", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // No audio track is playing
        if (PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer.getPlayingTrack() == null) {
            utilities.error(event.getChannel(), "shuffle", "\uD83C\uDFB2", "The player isn`t playing any song", "Use `" + Prefix.getPrefix(event.getGuild()) + "play <song>` to play a song", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
// Shuffle queue
        // Get queue
        BlockingQueue<AudioTrack> queue = PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.getQueue();
        List<AudioTrack> tracks = new ArrayList<>(queue);
        // Shuffle queue
        Collections.shuffle(tracks);
        // Replace
        PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.getQueue().clear();
        queue.addAll(tracks);
        // Success message
        EmbedBuilder success = new EmbedBuilder()
                .setAuthor("shuffle", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(utilities.blue)
                .setDescription("The current queue was jumbled");
        event.getChannel().sendMessage(success.build()).queue();
    }
}
