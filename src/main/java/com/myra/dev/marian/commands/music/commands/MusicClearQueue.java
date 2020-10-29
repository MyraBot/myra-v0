package com.myra.dev.marian.commands.music.commands;

import com.myra.dev.marian.APIs.LavaPlayer.GuildMusicManager;
import com.myra.dev.marian.APIs.LavaPlayer.PlayerManager;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
@CommandSubscribe(
        name = "clear queue",
        aliases = {"queue clear"}
)
public class MusicClearQueue implements Command {
    //TODO
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Check for no arguments
        if (ctx.getArguments().length != 0) return;
        //not connected to a voice channel
        if (!ctx.getGuild().getAudioManager().isConnected()) {
            Manager.getUtilities().error(
                    ctx.getChannel(),
                    "shuffle queue", "\uD83D\uDCE4",
                    "I'm not connected to a voice channel",
                    "Use `" + ctx.getPrefix() + "join` to connect me to your voice channel",
                    ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //if no track is playing
        if (PlayerManager.getInstance().getMusicManager(ctx.getGuild()).audioPlayer.getPlayingTrack() == null) {
            Manager.getUtilities().error(
                    ctx.getChannel(),
                    "shuffle queue", "\uD83C\uDFB2",
                    "The player isn`t playing any song",
                    "Use `" + ctx.getPrefix() + "play <song>` to play a song",
                    ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getMusicManager(ctx.getGuild());

        musicManager.scheduler.getQueue().clear();
        musicManager.audioPlayer.stopTrack();
        musicManager.audioPlayer.setPaused(false);

        Manager.getUtilities().success(ctx.getChannel(),
                "clear queue", "\uD83D\uDDD1",
                "Queue cleared",
                "All songs have been removed from the queue",
                ctx.getAuthor().getEffectiveAvatarUrl(),
                false, null);
    }
}
