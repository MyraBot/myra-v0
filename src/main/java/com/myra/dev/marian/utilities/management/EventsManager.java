package com.myra.dev.marian.utilities.management;

import com.myra.dev.marian.APIs.Twitch;
import com.myra.dev.marian.InviteThanks;
import com.myra.dev.marian.commands.fun.TextFormatter;
import com.myra.dev.marian.commands.general.Reminder;
import com.myra.dev.marian.commands.general.information.InformationServer;
import com.myra.dev.marian.commands.help.Commands;
import com.myra.dev.marian.commands.help.Help;
import com.myra.dev.marian.commands.moderation.mute.MutePermissions;
import com.myra.dev.marian.commands.moderation.mute.Tempmute;
import com.myra.dev.marian.commands.music.MusicTimeout;
import com.myra.dev.marian.commands.music.commands.MusicController;
import com.myra.dev.marian.commands.music.commands.MusicPlay;
import com.myra.dev.marian.database.MongoDbUpdate;
import com.myra.dev.marian.listeners.autorole.AutoroleAssign;
import com.myra.dev.marian.listeners.leveling.Leveling;
import com.myra.dev.marian.listeners.notification.Notification;
import com.myra.dev.marian.listeners.welcome.WelcomeImage.WelcomeImage;
import com.myra.dev.marian.listeners.welcome.WelcomeImage.WelcomeImageFont;
import com.myra.dev.marian.listeners.welcome.welcomeDirectMessage.WelcomeDirectMessage;
import com.myra.dev.marian.marian.ServerTracking;
import com.myra.dev.marian.marian.Shutdown;
import com.myra.dev.marian.utilities.MessageReaction;
import com.myra.dev.marian.utilities.management.commands.CommandService;
import com.myra.dev.marian.utilities.management.listeners.ListenerService;
import net.dv8tion.jda.api.entities.Message;
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
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventsManager extends ListenerAdapter {
    private final CommandService commandService = Manager.COMMAND_SERVICE;
    private final ListenerService listenerService = Manager.LISTENER_SERVICE;

    /**
     * run commands
     */
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        try {
            //if message is a server
            if (event.getMessage().getFlags().contains(Message.MessageFlag.IS_CROSSPOST)) return;
            //if message is a Webhook
            if (event.getMessage().isWebhookMessage()) return;

            commandService.processCommandExecution(event);
            listenerService.processCommandExecution(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * reactions
     */
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        try {
            // Shutdown
            new Shutdown().guildMessageReactionAddEvent(event);
            // Commands
            new Commands().guildMessageReactionAddEvent(event);
            new Help().guildMessageReactionAddEvent(event);

            new InformationServer().guildMessageReactionAddEvent(event);
            // Text formatter
            new TextFormatter().guildMessageReactionAddEvent(event);
            // Music
            new MusicPlay().guildMessageReactionAddEvent(event);
            new MusicController().guildMessageReactionAddEvent(event);

            new WelcomeImageFont().guildMessageReactionAddEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * guild
     */
    //guild name changed
    public void onGuildUpdateName(GuildUpdateNameEvent event) {
        try {
            new MongoDbUpdate().guildNameUpdated(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onTextChannelCreate(TextChannelCreateEvent event) {
        try {
            // Set permissions for mute role
            new MutePermissions().textChannelCreateEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        try {
            //update database
            new MongoDbUpdate().memberJoined(event);

            //welcome image
            new WelcomeImage().memberJoined(event);
            //welcome direct message
            new WelcomeDirectMessage().memberJoined(event);

            // Autorole
            new AutoroleAssign().onGuildMemberJoin(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * jda ready
     */
    public void onReady(ReadyEvent event) {
        try {
            //add missing members to the database
            new MongoDbUpdate().jdaReady(event);
            //load reminders
            new Reminder().onReady(event);
            //load bans
//            new Tempban().onReady(event);
            //load mutes
            new Tempmute().onReady(event);


            //get access token for Twitch
            new Twitch().jdaReady(event);
            //load streamers
            new Notification().jdaReady(event);
            //clear HashMap
            new MessageReaction().jdaReady(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * voice channel
     */
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        try {
            new MusicTimeout().voiceChannelLeave(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        try {
            new MusicTimeout().voiceChannelMove(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onGuildJoin(GuildJoinEvent event) {
        try {
            //add guild document
            new MongoDbUpdate().guildJoinEvent(event);
            // Log
            new ServerTracking().guildJoinEvent(event);
            // Thank message to server owner
            new InviteThanks().guildJoinEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onGuildLeave(GuildLeaveEvent event) {
        try {
            //delete guild document
            new MongoDbUpdate().guildLeaveEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        try {
            new Leveling().xpVoice(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onUserUpdateName(UserUpdateNameEvent event) {
        try {
            // Update database
            new MongoDbUpdate().userUpdateNameEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
