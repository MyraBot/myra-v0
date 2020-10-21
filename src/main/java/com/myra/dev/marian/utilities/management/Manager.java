package com.myra.dev.marian.utilities.management;

import com.myra.dev.marian.commands.administrator.Prefix;
import com.myra.dev.marian.commands.administrator.Say;
import com.myra.dev.marian.commands.administrator.Someone;
import com.myra.dev.marian.commands.administrator.Toggle;
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
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.listeners.autorole.AutoRoleSet;
import com.myra.dev.marian.listeners.autorole.AutoroleToggle;
import com.myra.dev.marian.listeners.leveling.*;
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
        Database.setDb(MONGO_DB);
        MongoDbUpdate.setDb(MONGO_DB);

        Reminder.setDb(MONGO_DB);
        Tempmute.setDb(MONGO_DB);

        //load commands
        commandRegistry();
    }

    //return utilities
    public static Utilities getUtilities() {
        return UTILITIES;
    }

    //return utilities
    public static Leveling getLeveling() {
        return LEVELING;
    }

    public void commandRegistry() {
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

                new Rank(),
                new Leaderboard(),
                // Fun
                new Meme(),
                new TextFormatter(),
                new WouldYouRather(),
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
                //autorole
                new AutoRoleSet(),
                new AutoroleToggle()
        );
        LISTENER_SERVICE.register(
                new LevelingListener(),

                new Someone()
        );
    }
}
