package com.myra.dev.marian.utilities.management;

import com.myra.dev.marian.commands.administrator.Prefix;
import com.myra.dev.marian.commands.administrator.Say;
import com.myra.dev.marian.commands.administrator.Someone;
import com.myra.dev.marian.commands.administrator.Toggle;
import com.myra.dev.marian.commands.economy.Balance;
import com.myra.dev.marian.commands.economy.Daily;
import com.myra.dev.marian.commands.economy.administrator.EconomySet;
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
import com.myra.dev.marian.commands.leveling.Leaderboard;
import com.myra.dev.marian.commands.leveling.administrator.LevelingHelp;
import com.myra.dev.marian.commands.leveling.administrator.LevelingSet;
import com.myra.dev.marian.commands.leveling.Rank;
import com.myra.dev.marian.commands.leveling.administrator.levelingRoles.LevelingRolesAdd;
import com.myra.dev.marian.commands.leveling.administrator.levelingRoles.LevelingRolesRemove;
import com.myra.dev.marian.commands.moderation.Clear;
import com.myra.dev.marian.commands.moderation.Kick;
import com.myra.dev.marian.commands.moderation.ModerationHelp;
import com.myra.dev.marian.commands.moderation.Nick;
import com.myra.dev.marian.commands.moderation.ban.Ban;
import com.myra.dev.marian.commands.moderation.ban.Tempban;
import com.myra.dev.marian.commands.moderation.ban.Unban;
import com.myra.dev.marian.commands.moderation.mute.Mute;
import com.myra.dev.marian.commands.moderation.mute.Tempmute;
import com.myra.dev.marian.commands.moderation.mute.Unmute;
import com.myra.dev.marian.commands.music.commands.*;
import com.myra.dev.marian.database.MongoDb;
import com.myra.dev.marian.database.MongoDbUpdate;
import com.myra.dev.marian.listeners.autorole.AutoRoleSet;
import com.myra.dev.marian.listeners.autorole.AutoroleToggle;
import com.myra.dev.marian.listeners.leveling.*;
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
import com.myra.dev.marian.listeners.welcome.welcomeDirectMessage.WelcomeDirectMessageHelp;
import com.myra.dev.marian.listeners.welcome.welcomeDirectMessage.WelcomeDirectMessageMessage;
import com.myra.dev.marian.listeners.welcome.welcomeDirectMessage.WelcomeDirectMessagePreview;
import com.myra.dev.marian.listeners.welcome.welcomeDirectMessage.WelcomeDirectMessageToggle;
import com.myra.dev.marian.listeners.welcome.welcomeEmbed.WelcomeEmbedHelp;
import com.myra.dev.marian.listeners.welcome.welcomeEmbed.WelcomeEmbedMessage;
import com.myra.dev.marian.listeners.welcome.welcomeEmbed.WelcomeEmbedPreview;
import com.myra.dev.marian.listeners.welcome.welcomeEmbed.WelcomeEmbedToggle;
import com.myra.dev.marian.listeners.welcome.WelcomeImage.*;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.commands.CommandService;
import com.myra.dev.marian.utilities.management.commands.DefaultCommandService;
import com.myra.dev.marian.utilities.management.listeners.DefaultListenerService;
import com.myra.dev.marian.utilities.management.listeners.ListenerService;

import java.util.HashMap;

public class Manager {
    //create HashMap
    public static HashMap<String, Events> commands = new HashMap<String, Events>();

    final static MongoDb MONGO_DB = new MongoDb();
    final static Utilities UTILITIES = new Utilities();
    final static Leveling LEVELING = new Leveling();
    final static CommandService COMMAND_SERVICE = new DefaultCommandService();
    final static ListenerService LISTENER_SERVICE = new DefaultListenerService();

    public void start() {
        //load database
        MongoDbUpdate.setDb(MONGO_DB);

        Reminder.setDb(MONGO_DB);

        //load commands
        commandRegistry();
    }

    // Return database
    public static MongoDb getDatabase() {
        return MONGO_DB;
    }

    // Return utilities
    public static Utilities getUtilities() {
        return UTILITIES;
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
                // Administrator
                new Prefix(),
                new Say(),
                new Toggle(),
                // Help
                new Commands(),
                new Help(),
                new Invite(),
                new Ping(),
                new Support(),
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
                new LevelingRolesAdd(),
                new LevelingRolesRemove(),

                new Rank(),
                new Leaderboard(),
                // Economy
                new EconomySet(),

                new Balance(),
                new Daily(),
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
                new AutoroleToggle(),
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
