package com.myra.dev.marian.commands.music.commands;

import com.myra.dev.marian.APIs.LavaPlayer.GuildMusicManager;
import com.myra.dev.marian.APIs.LavaPlayer.PlayerManager;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
@CommandSubscribe(
        name = "clear queue",
        aliases = {"queue clear"}
)
public class MusicClearQueue implements Command {
    //TODO
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
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getMusicManager(event.getGuild());

        musicManager.scheduler.getQueue().clear();
        musicManager.audioPlayer.stopTrack();
        musicManager.audioPlayer.setPaused(false);

        Manager.getUtilities().success(event.getChannel(),
                "clear queue", "\uD83D\uDDD1",
                "Queue cleared",
                "All songs have been removed from the queue",
                event.getAuthor().getEffectiveAvatarUrl(),
                false, null);
    }
}
