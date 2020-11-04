package com.myra.dev.marian.commands.music.commands;

import com.myra.dev.marian.APIs.LavaPlayer.PlayerManager;
import com.myra.dev.marian.APIs.LavaPlayer.TrackScheduler;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandSubscribe(
        command = "clear queue",
        name = "clear queue",
        aliases = {"queue clear"}
)
public class MusicClearQueue implements Command {

    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Check for no arguments
        if (ctx.getArguments().length != 0) return;
        // Get utilities
        final Utilities utilities = Manager.getUtilities();
        // Get track scheduler
        final TrackScheduler scheduler = PlayerManager.getInstance().getMusicManager(ctx.getGuild()).scheduler;
        // Get audio player
        final AudioPlayer audioPlayer = PlayerManager.getInstance().getMusicManager(ctx.getGuild()).audioPlayer;
// Errors
        // Bot isn't connected to a voice channel
        if (!ctx.getGuild().getAudioManager().isConnected()) {
            utilities.error(ctx.getChannel(), "clear queue", "\uD83D\uDDD1", "I'm not connected to a voice channel", "Use `" + ctx.getPrefix() + "join` to connect me to your voice channel", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // No audio track is playing
        if (PlayerManager.getInstance().getMusicManager(ctx.getGuild()).audioPlayer.getPlayingTrack() == null) {
            utilities.error(ctx.getChannel(), "clear queue", "\uD83D\uDDD1", "The player isn`t playing any song", "Use `" + ctx.getPrefix() + "play <song>` to play a song", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
// Clear queue
        //clear queue
        scheduler.getQueue().clear();
        // Success message
        EmbedBuilder success = new EmbedBuilder()
                .setAuthor("clear queue", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(utilities.blue)
                .setDescription("All songs have been removed from the queue");
        ctx.getChannel().sendMessage(success.build()).queue();
    }
}
