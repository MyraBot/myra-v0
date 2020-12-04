package com.myra.dev.marian.management;

import com.myra.dev.marian.commands.administrator.notifications.NotificationsList;
import com.myra.dev.marian.commands.economy.blackjack.BlackJack;
import com.myra.dev.marian.commands.fun.TextFormatter;
import com.myra.dev.marian.commands.general.information.InformationServer;
import com.myra.dev.marian.commands.help.Commands;
import com.myra.dev.marian.commands.help.Help;
import com.myra.dev.marian.commands.help.InviteThanks;
import com.myra.dev.marian.commands.leveling.Background;
import com.myra.dev.marian.commands.moderation.mute.MutePermissions;
import com.myra.dev.marian.commands.music.MusicTimeout;
import com.myra.dev.marian.commands.music.commands.MusicController;
import com.myra.dev.marian.commands.music.commands.MusicPlay;
import com.myra.dev.marian.database.MongoDbUpdate;
import com.myra.dev.marian.listeners.autorole.AutoroleAssign;
import com.myra.dev.marian.listeners.welcome.WelcomeImage.WelcomeImageFont;
import com.myra.dev.marian.listeners.welcome.WelcomeListener;
import com.myra.dev.marian.management.commands.CommandService;
import com.myra.dev.marian.management.listeners.ListenerService;
import com.myra.dev.marian.marian.Roles;
import com.myra.dev.marian.marian.ServerTracking;
import com.myra.dev.marian.marian.Shutdown;
import net.dv8tion.jda.api.entities.Message;
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
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventsManager extends ListenerAdapter {
    private final CommandService commandService = Manager.COMMAND_SERVICE;
    private final ListenerService listenerService = Manager.LISTENER_SERVICE;

    /**
     * run commands
     */
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        try {
            if (event.getMessage().getFlags().contains(Message.MessageFlag.IS_CROSSPOST)) return; // Message is a server announcement
            if (event.getMessage().isWebhookMessage()) return; // Message is a WebHook
            if (event.getAuthor().isBot()) return; // Message is from another bot

            new EventWaiter().buy(event);

            commandService.processCommandExecution(event);
            listenerService.processCommandExecution(event);
        } catch (Exception e) {
            if (e.toString().startsWith("net.dv8tion.jda.api.exceptions.InsufficientPermissionException: Cannot perform action due to a lack of Permission. Missing permission: MESSAGE_WRITE")) return;
            event.getChannel().sendMessage("Error: Please report this to my developer!");
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
            // Change rank background
            new Background().guildMessageReactionAddEvent(event);
            // Text formatter
            new TextFormatter().guildMessageReactionAddEvent(event);
            // Blackjack
            new BlackJack().guildMessageReactionAddEvent(event);
            // Music
            new MusicPlay().guildMessageReactionAddEvent(event);
            new MusicController().guildMessageReactionAddEvent(event);

            new WelcomeImageFont().guildMessageReactionAddEvent(event);

            // Notification
            new NotificationsList().switchList(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * guild
     */
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
            // Welcome
            new WelcomeListener().welcome(event);

            // Autorole
            new AutoroleAssign().onGuildMemberJoin(event);

            // Exclusive role
            new Roles().exclusive(event);
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
            // Add guild document to database
            new MongoDbUpdate().guildJoinEvent(event);
            // Server tracking message
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
            // Events
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
