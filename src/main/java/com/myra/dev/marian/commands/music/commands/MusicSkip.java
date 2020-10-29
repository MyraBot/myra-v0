package com.myra.dev.marian.commands.music.commands;

import com.myra.dev.marian.APIs.LavaPlayer.PlayerManager;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import com.myra.dev.marian.utilities.management.commands.CommandContext;

@CommandSubscribe(
        name = "skip",
        aliases = {"next"}
)
public class MusicSkip implements Command {

    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Check for no arguments
        if (ctx.getArguments().length != 0) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
// Errors
        // Bot isn't connected to a voice channel
        if (!ctx.getGuild().getAudioManager().isConnected()) {
            utilities.error(
                    ctx.getChannel(),
                    "skip", "\u23ED\uFE0F",
                    "I'm not connected to a voice channel",
                    "Use `" + ctx.getPrefix() + "join` to connect me to your voice channel",
                    ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // No audio track is playing
        if (PlayerManager.getInstance().getMusicManager(ctx.getGuild()).audioPlayer.getPlayingTrack() == null) {
            utilities.error(
                    ctx.getChannel(),
                    "skip", "\u23ED\uFE0F",
                    "The player isn`t playing any song",
                    "Use `" + ctx.getPrefix() + "play <song>` to play a song",
                    ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
// Skip current playing track
        // Get audio player
        AudioTrack track = PlayerManager.getInstance().getMusicManager(ctx.getGuild()).audioPlayer.getPlayingTrack();
        // Send success message
        EmbedBuilder success = new EmbedBuilder()
                .setAuthor("skip", track.getInfo().uri, ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(utilities.blue)
                .setDescription("Skipped track: " + utilities.hyperlink(track.getInfo().title, track.getInfo().uri));
        ctx.getChannel().sendMessage(success.build()).queue();
        // Skip track
        PlayerManager.getInstance().getMusicManager(ctx.getGuild()).scheduler.nextTrack();
    }
}
