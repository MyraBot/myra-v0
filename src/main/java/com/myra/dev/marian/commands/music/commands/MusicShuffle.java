package com.myra.dev.marian.commands.music.commands;

import com.myra.dev.marian.commands.music.Music.PlayerManager;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
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
        //not connected to a voice channel
        if (!event.getGuild().getAudioManager().isConnected()) {
            Manager.getUtilities().error(
                    event.getChannel(),
                    "shuffle queue", "\uD83D\uDCE4",
                    "I´m not connected to a voice channel",
                    "Use `" + Prefix.getPrefix(event.getGuild()) + "join` to connect me to your voice channel",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //if no track is playing
        if (PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).player.getPlayingTrack() == null) {
            Manager.getUtilities().error(
                    event.getChannel(),
                    "shuffle queue", "\uD83C\uDFB2",
                    "The player isn`t playing any song",
                    "Use `" + Prefix.getPrefix(event.getGuild()) + "play <song>` to play a song",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        BlockingQueue<AudioTrack> queue = PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).scheduler.getQueue();
        List<AudioTrack> tracks = new ArrayList<>(queue);
        Collections.shuffle(tracks);
        PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).scheduler.getQueue().clear();
        queue.addAll(tracks);

        Manager.getUtilities().success(event.getChannel(),
                "shuffle queue",
                "\uD83C\uDFB2",
                "The queue was successfully shuffled",
                "The current queue was jumbled",
                event.getAuthor().getEffectiveAvatarUrl(),
                false, false, null);
    }
}
