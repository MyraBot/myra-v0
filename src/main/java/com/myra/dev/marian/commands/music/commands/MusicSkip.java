package com.myra.dev.marian.commands.music.commands;

import com.myra.dev.marian.commands.music.Music.PlayerManager;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
@CommandSubscribe(
        name = "skip",
        aliases = {"next"}
)
public class MusicSkip implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Check for no arguments
        if (arguments.length != 0) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        //not connected to a voice channel
        if (!event.getGuild().getAudioManager().isConnected()) {
            utilities.error(
                    event.getChannel(),
                    "skip", "\u23ED\uFE0F",
                    "I'm not connected to a voice channel",
                    "Use `" + Prefix.getPrefix(event.getGuild()) + "join` to connect me to your voice channel",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //if no track is playing
        if (PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).player.getPlayingTrack() == null) {
            utilities.error(
                    event.getChannel(),
                    "skip", "\u23ED\uFE0F",
                    "The player isn`t playing any song",
                    "Use `" + Prefix.getPrefix(event.getGuild()) + "play <song>` to play a song",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // Skip current track
        utilities.success(event.getChannel(),
                "skip",
                "\u23ED\uFE0F",
                "skipped song",
                "skipped **" + PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).player.getPlayingTrack().getInfo().title + "**",
                event.getAuthor().getEffectiveAvatarUrl(),
                false, null);
        PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).scheduler.nextTrack();
    }
}
