package com.myra.dev.marian.management;

import com.myra.dev.marian.commands.Leaderboard;
import com.myra.dev.marian.commands.administrator.notifications.NotificationsList;
import com.myra.dev.marian.commands.economy.blackjack.BlackJack;
import com.myra.dev.marian.commands.fun.TextFormatter;
import com.myra.dev.marian.commands.general.information.InformationServer;
import com.myra.dev.marian.commands.help.Commands;
import com.myra.dev.marian.commands.help.Help;
import com.myra.dev.marian.commands.help.InviteThanks;
import com.myra.dev.marian.commands.leveling.Background;
import com.myra.dev.marian.commands.moderation.mute.MutePermissions;
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
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.ErrorResponse;

public class EventsManager extends ListenerAdapter {
    private final CommandService commandService = Manager.COMMAND_SERVICE;
    private final ListenerService listenerService = Manager.LISTENER_SERVICE;

    /**
     * run commands
     */
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        try {
            if (event.getMessage().getFlags().contains(Message.MessageFlag.IS_CROSSPOST))
                return; // Message is a server announcement
            if (event.getMessage().isWebhookMessage()) return; // Message is a WebHook
            if (event.getAuthor().isBot()) return; // Message is from another bot

            new EventWaiter().buy(event);
            commandService.processCommandExecution(event);
            listenerService.processCommandExecution(event);
        } catch (Exception e) {
            try {
                event.getChannel().sendMessage("Error: Please report this to my developer!").queue(null, ErrorResponseException.ignore(ErrorResponse.MISSING_PERMISSIONS));
                e.printStackTrace();
            } catch (Exception e1) {
            } //can't send msg
        }
    }

    /**
     * reactions
     */
    private final Shutdown shutdown = new Shutdown();
    private final WelcomeImageFont welcomeImageFont = new WelcomeImageFont();
    private final NotificationsList notificationsList = new NotificationsList();
    private final Commands commands = new Commands();
    private final Help help = new Help();
    private final InformationServer informationServer = new InformationServer();
    private final TextFormatter textFormatter = new TextFormatter();
    private final Background background = new Background();
    private final BlackJack blackJack = new BlackJack();
    private final Leaderboard leaderboard = new Leaderboard();

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        try {
            if (event.getUser().isBot()) return; // Don't react to bots

            // Marian
            shutdown.exitProgram(event); // Shutdown
            // Administrator
            welcomeImageFont.chooseFont(event); // Change welcome image font
            notificationsList.switchList(event); // List notification
            // Help
            commands.guildMessageReactionAddEvent(event);
            help.guildMessageReactionAddEvent(event);
            // Commands
            informationServer.guildMessageReactionAddEvent(event);
            // Fun
            textFormatter.guildMessageReactionAddEvent(event); // Text formatter
            // Leveling
            leaderboard.switchLeaderboard(event); // Switch what leaderboard shows
            // Economy
            background.confirm(event);
            blackJack.reaction(event); // Blackjack
            // Music
            new MusicPlay().guildMessageReactionAddEvent(event);
            new MusicController().guildMessageReactionAddEvent(event);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        try {
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
