package com.myra.dev.marian.commands.music.commands;

import com.myra.dev.marian.commands.music.Music.PlayerManager;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "music information",
        aliases = {"music info", "track information", "track info", "track", "song", "song information", "song info"}
)
public class MusicInformation implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Check for no arguments
        if (arguments.length != 0) return;
        // Get audio player
        AudioPlayer player = PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).player;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        //the bot isn´t connected to any voice channel
        if (!event.getGuild().getAudioManager().isConnected()) {
            utilities.error(
                    event.getChannel(),
                    "track information", "\uD83D\uDDD2",
                    "I´m not connected to a voice channel",
                    "Use `" + Prefix.getPrefix(event.getGuild()) + "join` to connect me to your voice channel",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //bot isn't playing any song
        if (PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).player.getPlayingTrack() == null) {
            utilities.error(event.getChannel(),
                    "track information", "\uD83D\uDDD2",
                    "The player isn`t playing any song",
                    "Use `" + Prefix.getPrefix(event.getGuild()) + "play <song>` to play a song",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        EmbedBuilder info = new EmbedBuilder()
                .setAuthor(player.getPlayingTrack().getInfo().title + " by " + player.getPlayingTrack().getInfo().author, player.getPlayingTrack().getInfo().uri, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(utilities.blue)
                .setDescription(player.isPaused() ? "\u23F8\uFE0F " : "\u23F8\uFE0F " + utilities.formatTime(player.getPlayingTrack().getPosition()) + " - " + utilities.formatTime(player.getPlayingTrack().getDuration()))
                .setFooter(displayPosition(player));
        event.getChannel().sendMessage(info.build()).queue();
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