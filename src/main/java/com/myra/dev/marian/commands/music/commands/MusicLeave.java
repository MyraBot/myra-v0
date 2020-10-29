package com.myra.dev.marian.commands.music.commands;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "leave",
        aliases = {"disconnect"}
)
public class MusicLeave implements Command {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Check for no arguments
        if (arguments.length != 0) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
// Errors
        // Not connected to a voice channel
        if (!event.getGuild().getAudioManager().isConnected()) {
            utilities.error(event.getChannel(),
                    "leave", "\uD83D\uDCE4",
                    "I'm not connected to a voice channel",
                    "Use `" + Prefix.getPrefix(event.getGuild()) + "join` to connect me to your voice channel",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // If author isn't in a voice channel yet
        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            utilities.error(event.getChannel(), "leave", "\uD83D\uDCE4", "You need to join a voice channel first to use this command", "Use `" + Prefix.getPrefix(event.getGuild()) + "join` to let me join a voice channel", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // Author isn't in the same voice channel as the bot
        if (!event.getGuild().getAudioManager().getConnectedChannel().getMembers().contains(event.getMember())) {
            utilities.error(event.getChannel(),
                    "leave", "\uD83D\uDCE4",
                    "You have to be in the same voice channel as me to use this command",
                    "To kick me you need to be in **" + event.getGuild().getAudioManager().getConnectedChannel().getName() + "**",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
// Leave voice channel
        // Leave from current channel
        event.getGuild().getAudioManager().closeAudioConnection();
        // Send success message
        EmbedBuilder success = new EmbedBuilder()
                .setAuthor("leave", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(utilities.blue)
                .setDescription("Left voice channel: **" + event.getMember().getVoiceState().getChannel().getName() + "**");
        event.getChannel().sendMessage(success.build()).queue();
    }
}
