package com.myra.dev.marian.management;

import com.myra.dev.marian.Bot;
import com.myra.dev.marian.commands.administrator.Prefix;
import com.myra.dev.marian.commands.administrator.Say;
import com.myra.dev.marian.commands.administrator.Toggle;
import com.myra.dev.marian.commands.economy.*;
import com.myra.dev.marian.commands.economy.administrator.Currency;
import com.myra.dev.marian.commands.economy.administrator.EconomySet;
import com.myra.dev.marian.commands.economy.blackjack.BlackJack;
import com.myra.dev.marian.commands.fun.Meme;
import com.myra.dev.marian.commands.fun.TextFormatter;
import com.myra.dev.marian.commands.fun.WouldYouRather;
import com.myra.dev.marian.commands.general.Avatar;
import com.myra.dev.marian.commands.general.Calculate;
import com.myra.dev.marian.commands.general.Reminder;
import com.myra.dev.marian.commands.general.information.InformationBot;
import com.myra.dev.marian.commands.general.information.InformationHelp;
import com.myra.dev.marian.commands.general.information.InformationServer;
import com.myra.dev.marian.commands.general.information.InformationUser;
import com.myra.dev.marian.commands.help.*;
import com.myra.dev.marian.commands.leveling.Background;
import com.myra.dev.marian.commands.leveling.Leaderboard;
import com.myra.dev.marian.commands.leveling.Rank;
import com.myra.dev.marian.commands.leveling.administrator.LevelingHelp;
import com.myra.dev.marian.commands.leveling.administrator.LevelingSet;
import com.myra.dev.marian.commands.leveling.administrator.levelingRoles.LevelingRolesAdd;
import com.myra.dev.marian.commands.leveling.administrator.levelingRoles.LevelingRolesHelp;
import com.myra.dev.marian.commands.leveling.administrator.levelingRoles.LevelingRolesList;
import com.myra.dev.marian.commands.leveling.administrator.levelingRoles.LevelingRolesRemove;
import com.myra.dev.marian.commands.moderation.Clear;
import com.myra.dev.marian.commands.moderation.Kick;
import com.myra.dev.marian.commands.moderation.ModerationHelp;
import com.myra.dev.marian.commands.moderation.Nick;
import com.myra.dev.marian.commands.moderation.ban.Ban;
import com.myra.dev.marian.commands.moderation.ban.Tempban;
import com.myra.dev.marian.commands.moderation.ban.Unban;
import com.myra.dev.marian.commands.moderation.mute.Mute;
import com.myra.dev.marian.commands.moderation.mute.MuteRole;
import com.myra.dev.marian.commands.moderation.mute.Tempmute;
import com.myra.dev.marian.commands.moderation.mute.Unmute;
import com.myra.dev.marian.commands.music.commands.*;
import com.myra.dev.marian.database.MongoDbUpdate;
import com.myra.dev.marian.listeners.Someone;
import com.myra.dev.marian.listeners.autorole.AutoRoleSet;
import com.myra.dev.marian.listeners.leveling.Leveling;
import com.myra.dev.marian.listeners.leveling.LevelingListener;
import com.myra.dev.marian.listeners.logging.LogChannel;
import com.myra.dev.marian.listeners.notification.AddStreamer;
import com.myra.dev.marian.listeners.notification.NotificationChannel;
import com.myra.dev.marian.listeners.notification.NotificationHelp;
import com.myra.dev.marian.listeners.notification.NotificationList;
import com.myra.dev.marian.listeners.suggestions.SubmitSuggestion;
import com.myra.dev.marian.listeners.suggestions.SuggestionsChannel;
import com.myra.dev.marian.listeners.suggestions.SuggestionsHelp;
import com.myra.dev.marian.listeners.suggestions.SuggestionsToggle;
import com.myra.dev.marian.listeners.welcome.WelcomeChannel;
import com.myra.dev.marian.listeners.welcome.WelcomeColour;
import com.myra.dev.marian.listeners.welcome.WelcomeHelp;
import com.myra.dev.marian.listeners.welcome.WelcomeImage.*;
import com.myra.dev.marian.listeners.welcome.welcomeDirectMessage.WelcomeDirectMessageHelp;
import com.myra.dev.marian.listeners.welcome.welcomeDirectMessage.WelcomeDirectMessageMessage;
import com.myra.dev.marian.listeners.welcome.welcomeDirectMessage.WelcomeDirectMessagePreview;
import com.myra.dev.marian.listeners.welcome.welcomeDirectMessage.WelcomeDirectMessageToggle;
import com.myra.dev.marian.listeners.welcome.welcomeEmbed.WelcomeEmbedHelp;
import com.myra.dev.marian.listeners.welcome.welcomeEmbed.WelcomeEmbedMessage;
import com.myra.dev.marian.listeners.welcome.welcomeEmbed.WelcomeEmbedPreview;
import com.myra.dev.marian.listeners.welcome.welcomeEmbed.WelcomeEmbedToggle;
import com.myra.dev.marian.marian.Dashboard;
import com.myra.dev.marian.marian.GetInvite;
import com.myra.dev.marian.marian.Shutdown;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.management.commands.CommandService;
import com.myra.dev.marian.management.commands.CommandSubscribe;
import com.myra.dev.marian.management.commands.DefaultCommandService;
import com.myra.dev.marian.management.listeners.DefaultListenerService;
import com.myra.dev.marian.management.listeners.ListenerService;

import java.util.HashMap;
import java.util.Map;

public class Manager {
    //create HashMap
    public static HashMap<String, Events> commands = new HashMap<String, Events>();

    public static enum type {STRING, INTEGER, BOOLEAN}

    final static Leveling LEVELING = new Leveling();
    final static CommandService COMMAND_SERVICE = new DefaultCommandService();
    final static ListenerService LISTENER_SERVICE = new DefaultListenerService();

    public void start() {
        //load commands
        commandRegistry();
    }

    public static Map<Command, CommandSubscribe> getCommands() {
        return COMMAND_SERVICE.getCommands();
    }

    // Return leveling
    public static Leveling getLeveling() {
        return LEVELING;
    }

    // Register events
    public void commandRegistry() {
        // Register commands
        COMMAND_SERVICE.register(
                // Marian
                new MongoDbUpdate(),
                new GetInvite(),
                new Dashboard(),
                new Shutdown(),
                // Administrator
                new Prefix(),
                new Say(),
                new Toggle(),
                //
                new LogChannel(),
                // Help
                new Commands(),
                new Help(),
                new Invite(),
                new Ping(),
                new Support(),
                new Vote(),
                // General
                new InformationHelp(),
                new InformationServer(),
                new InformationUser(),
                new InformationBot(),

                new Avatar(),
                new Calculate(),
                new Reminder(),
                // Leveling
                new LevelingHelp(),
                new LevelingSet(),

                new LevelingRolesHelp(),
                new LevelingRolesList(),
                new LevelingRolesAdd(),
                new LevelingRolesRemove(),

                new Rank(),
                new Background(),
                new Leaderboard(),
                // Economy
                new EconomyHelp(),
                new EconomySet(),
                new Currency(),

                new Balance(),
                new Daily(),
                new Fish(),
                new BlackJack(),
                new Give(),
                // Fun
                new Meme(),
                new TextFormatter(),
                new WouldYouRather(),
                // Suggestions
                new SuggestionsHelp(),
                new SuggestionsChannel(),
                new SuggestionsToggle(),

                new SubmitSuggestion(),
                // Moderation
                new ModerationHelp(),

                new Ban(),
                new Tempban(),
                new Unban(),

                new MuteRole(),
                new Mute(),
                new Tempmute(),
                new Unmute(),

                new Clear(),
                new Kick(),
                new Nick(),
                // Music
                new MusicHelp(),
                new MusicJoin(),
                new MusicLeave(),
                new MusicPlay(),
                new MusicShuffle(),
                new MusicInformation(),
                new MusicQueue(),
                new MusicSkip(),
                new MusicClearQueue(),
                new MusicController(),
                // Autorole
                new AutoRoleSet(),
                // Notification
                new NotificationHelp(),

                new NotificationChannel(),
                new AddStreamer(),
                new NotificationList(),
                // Welcome
                new WelcomeHelp(),
                new WelcomeChannel(),
                new WelcomeColour(),
                // Welcome Image
                new WelcomeImageHelp(),
                new WelcomeImagePreview(),
                new WelcomeImageToggle(),
                new WelcomeImageBackground(),
                new WelcomeImageFont(),
                // Welcome direct message
                new WelcomeDirectMessageHelp(),
                new WelcomeDirectMessageToggle(),
                new WelcomeDirectMessageMessage(),
                new WelcomeDirectMessagePreview(),
                // Welcome embed
                new WelcomeEmbedHelp(),
                new WelcomeEmbedToggle(),
                new WelcomeEmbedMessage(),
                new WelcomeEmbedPreview()
        );
        // Register listeners
        LISTENER_SERVICE.register(
                new LevelingListener(),

                new Someone()
        );
    }
}
