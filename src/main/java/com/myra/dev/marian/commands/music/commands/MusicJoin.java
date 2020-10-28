package com.myra.dev.marian.commands.music.commands;

import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@SuppressWarnings("ConstantConditions")
@CommandSubscribe(
        name = "join"
)
public class MusicJoin implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Check for no arguments
        if (arguments.length != 0) return;
// ERRORS
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Already connected to a voice channel
        if (event.getGuild().getAudioManager().isConnected()) {
            utilities.error(event.getChannel(), "join", "\uD83D\uDCE5", "I can only be in one channel at a time", "I'm already connected to **" + event.getGuild().getAudioManager().getConnectedChannel().getName() + "**", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // Member didn't joined voice call yet
        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            utilities.error(event.getChannel(), "join", "\uD83D\uDCE5", "Please join a voice channel first", "In order for me to join a voice channel, you must already be connected to a voice channel", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // Missing permissions to connect
        if (!event.getGuild().getSelfMember().hasPermission(event.getMember().getVoiceState().getChannel(), Permission.VOICE_CONNECT)) {
            utilities.error(event.getChannel(), "join", "\uD83D\uDCE5", "I'm missing permissions to join your voice channel", "please give me the permission `Connect` under the `VOICE PERMISSIONS` category", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
// JOIN VOICE CHANNEL
        // Open audio connection
        event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
        // Send success message
        EmbedBuilder success = new EmbedBuilder()
                .setAuthor("join", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(utilities.blue)
                .setDescription("Joined voice channel: **" + event.getMember().getVoiceState().getChannel().getName() + "**");
        event.getChannel().sendMessage(success.build()).queue();
    }
}
