package com.myra.dev.marian.commands.music.commands;

import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.commands.music.Music.PlayerManager;
import com.myra.dev.marian.database.Prefix;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.TimeUnit;
@CommandSubscribe(
        name = "music information",
        aliases = {"music info", "track information", "track info", "track", "song", "song information", "song info"}
)
public class MusicInformation implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Check for no arguments
        if (arguments.length != 0) return;

        AudioPlayer player = PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).player;
        //the bot isn´t connected to any voice channel
        if (!event.getGuild().getAudioManager().isConnected()) {
            Manager.getUtilities().error(
                    event.getChannel(),
                    "track information", "\uD83D\uDDD2",
                    "I´m not connected to a voice channel",
                    "Use `" + Prefix.getPrefix(event.getGuild()) + "join` to connect me to your voice channel",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //bot isn´t playing any song
        if (PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).player.getPlayingTrack() == null) {
            Manager.getUtilities().error(event.getChannel(),
                    "track information", "\uD83D\uDDD2",
                    "The player isn`t playing any song",
                    "Use `" + Prefix.getPrefix(event.getGuild()) + "play <song>` to play a song",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        EmbedBuilder info = new EmbedBuilder()
                .setAuthor("│ " + player.getPlayingTrack().getInfo().title + " by " + player.getPlayingTrack().getInfo().author, player.getPlayingTrack().getInfo().uri, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().blue)
                .setDescription(player.isPaused() ? "\u23F8\uFE0F " : "\u23F8\uFE0F " + formatTime(player.getPlayingTrack().getPosition()) + " - " + formatTime(player.getPlayingTrack().getDuration()))
                .setFooter(displayPosition(player));
        event.getChannel().sendMessage(info.build()).queue();
    }

    private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private String displayPosition(AudioPlayer player) {
        //split song duration in 15 parts
        Long sections = player.getPlayingTrack().getDuration() / 15;
        //get the part the song is in
        Long atSection = player.getPlayingTrack().getPosition() / sections;

        StringBuilder positionRaw = new StringBuilder("000000000000000")
                .insert(Math.toIntExact(atSection), '1');

        return positionRaw.toString().replaceAll("0", "▬").replace("1", "\uD83D\uDD18");
    }
}