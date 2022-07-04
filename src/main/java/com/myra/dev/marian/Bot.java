package com.myra.dev.marian;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.myra.dev.marian.management.Startup;
import com.myra.dev.marian.utilities.ConsoleColours;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Bot {
    public static ShardManager shardManager;

    private final static String TOKEN = "NzE4NDQ0NzA5NDQ1NjMyMTIy.Xto9xg.JcLjYVCFcXMjvJmvvnBKuqUljao";
    private final static String LOADING_STATUS = "loading bars fill";
    private final static String ONLINE_INFO = ConsoleColours.GREEN + "Bot online" + ConsoleColours.RESET;
    private final static String OFFLINE_INFO = ConsoleColours.RED + "Bot offline" + ConsoleColours.RESET;

    public final static String prefix = "~";
    public final static String marian = "639544573114187797";
    public final static String myra = "718444709445632122";
    public final static String marianServer = "642809436515074053";
    public final static String myraServer = "774269364244971571";

    private final EventWaiter waiter = new EventWaiter();

    // Main method
    public static void main(String[] args) throws LoginException, RateLimitedException {
        new Bot();
    }
    private Bot() throws LoginException, RateLimitedException {
        DefaultShardManagerBuilder jda = DefaultShardManagerBuilder.createDefault(TOKEN)
                .enableIntents(GatewayIntent.GUILD_PRESENCES) // Need GatewayIntent.GUILD_PRESENCES for CacheFlag.ACTIVITY
                .enableIntents(GatewayIntent.GUILD_MEMBERS)  // Need GatewayIntent.GUILD_MEMBERS for MemberCachePolicy.ALL
                .enableCache(CacheFlag.ACTIVITY) // Need to get the activity of a member
                .enableCache(CacheFlag.VOICE_STATE)
                .enableCache(CacheFlag.EMOTE) // Need to get emotes from other servers
                .setMemberCachePolicy(MemberCachePolicy.ALL)

                .setStatus(OnlineStatus.IDLE)
                .setActivity(Activity.watching(LOADING_STATUS))

                .addEventListeners(waiter, new Startup(waiter));
        // Build JDA
        shardManager = jda.build();
        System.out.println(ONLINE_INFO); // Send success information
        consoleListener(); // Add console listener
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
                        // Set status to "offline"
                        shardManager.setStatus(OnlineStatus.OFFLINE);
                        // Stop shard manager
                        shardManager.shutdown();
                        System.out.println(OFFLINE_INFO);
                        // Stop jar file from running
                        System.exit(0);
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