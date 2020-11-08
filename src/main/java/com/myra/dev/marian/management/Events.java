package com.myra.dev.marian.management;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;

public abstract class Events {
    public void jdaReady(ReadyEvent event) throws Exception {
    }

    public void guildMessageReceivedEvent(GuildMessageReceivedEvent event) throws Exception {
    }

    public void guildMessageReactionAddEvent(GuildMessageReactionAddEvent event) throws Exception {
    }

    public void guildNameUpdated(GuildUpdateNameEvent event) throws Exception {
    }

    public void guildJoinEvent(GuildJoinEvent event) throws Exception {
    }

    public void guildLeaveEvent(GuildLeaveEvent event) throws Exception {
    }

    public void textChannelCreateEvent(TextChannelCreateEvent event) throws Exception {
    }

    public void memberJoined(GuildMemberJoinEvent event) throws Exception {
    }

    public void guildVoiceJoinEvent(GuildVoiceJoinEvent event) throws Exception {
    }

    public void voiceChannelLeave(GuildVoiceLeaveEvent event) throws Exception {
    }

    public void voiceChannelMove(GuildVoiceMoveEvent event) throws Exception {
    }

    public void UserUpdateNameEvent(UserUpdateNameEvent event) throws Exception {

    }
}
