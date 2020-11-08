package com.myra.dev.marian;


import com.myra.dev.marian.management.EventsManager;
import com.myra.dev.marian.management.Manager;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.ConsoleColours;
import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Bot {
    public static ShardManager shardManager;

    public final static String prefix = "~";
    public final static String marian = "639544573114187797";
    public final static String myraServer = "774269364244971571";

    // Main method
    public static void main(String[] args) throws LoginException {
        new Bot();
    }


    private Bot() throws LoginException {
        DefaultShardManagerBuilder jda = DefaultShardManagerBuilder.createDefault("NzE4NDQ0NzA5NDQ1NjMyMTIy.Xto9xg.dQxtSFxxYHpKXOwLCtJuWM5w1MM")
                .enableIntents(GatewayIntent.GUILD_PRESENCES) // Need GatewayIntent.GUILD_PRESENCES for CacheFlag.ACTIVITY
                .enableIntents(GatewayIntent.GUILD_MEMBERS)  // Need GatewayIntent.GUILD_MEMBERS for MemberCachePolicy.ALL
                .enableCache(CacheFlag.ACTIVITY) // Need to get the activity of a member
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(CacheFlag.VOICE_STATE)
                .setStatus(OnlineStatus.IDLE)
                .enableCache(CacheFlag.EMOTE) // Need to get emotes from other servers
                .addEventListeners(new EventsManager());
        // Build JDA
        shardManager = jda.build();
        // Change activity and profile picture
        changeUserInformation();
        // Register commands
        new Manager().start();
        // Send ready information
        System.out.println(ConsoleColours.GREEN + "Bot online" + ConsoleColours.RESET);
        // Add console listener
        consoleListener();
    }

    private void changeUserInformation() {
        // Get profile pictures
        ArrayList<InputStream> profilePictures = new ArrayList<>();
        profilePictures.add(this.getClass().getClassLoader().getResourceAsStream("profilePicture1.png"));
        profilePictures.add(this.getClass().getClassLoader().getResourceAsStream("profilePicture2.png"));
        profilePictures.add(this.getClass().getClassLoader().getResourceAsStream("profilePicture3.png"));
        profilePictures.add(this.getClass().getClassLoader().getResourceAsStream("profilePicture4.png"));
        profilePictures.add(this.getClass().getClassLoader().getResourceAsStream("profilePicture5.png"));
        profilePictures.add(this.getClass().getClassLoader().getResourceAsStream("profilePicture6.png"));
        profilePictures.add(this.getClass().getClassLoader().getResourceAsStream("profilePicture7.png"));
        profilePictures.add(this.getClass().getClassLoader().getResourceAsStream("profilePicture8.png"));
        // Get a random one
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Get random number
                int random = new Random().nextInt(profilePictures.size());
                // Change profile
                shardManager.getShards().forEach(bot -> {
                    try {
                        // Change status
                        bot.getPresence().setActivity(Activity.listening("~help │ v0.6.3 │ " + bot.getGuilds().size() + " servers"));
                        // Change profile picture
                        bot.getSelfUser().getManager().setAvatar(
                                Icon.from(profilePictures.get(random))).queue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }, 5 * 1000, 45 * 100 * 1000);
    }

    private void consoleListener() {
        String line;
        // Create a Buffered reader, which reads the lines of the console
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while ((line = reader.readLine()) != null) {
                // Shutdown command
                if (line.equalsIgnoreCase("shutdown")) {
                    if (shardManager != null) {
                        shardManager.setStatus(OnlineStatus.OFFLINE);
                        shardManager.shutdown();
                        System.out.println(ConsoleColours.RED + "Bot offline" + ConsoleColours.RESET);
                    }
                }
                // Help command
                else {
                    System.out.println("Use " + ConsoleColours.RED + "shutdown" + ConsoleColours.RESET + " to shutdown the program");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}