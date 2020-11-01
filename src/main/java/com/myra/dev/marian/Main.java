package com.myra.dev.marian;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.ConsoleColours;
import com.myra.dev.marian.utilities.management.EventsManager;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class Main {
    public static ShardManager shardManager;

    public static String prefix = "~";
    public static String marian = "639544573114187797";


    //main method
    public static void main(String[] args) throws Exception {
        new Manager().start();
        //load main class
        new Main();
    }

    public Main() {
        try {
            //load utilities
            Prefix.load();
            //build bot
            DefaultShardManagerBuilder jda = new DefaultShardManagerBuilder("NzE4NDQ0NzA5NDQ1NjMyMTIy.Xto9xg.dQxtSFxxYHpKXOwLCtJuWM5w1MM")
                    //.enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS)
                    //.enableCache(CacheFlag.ACTIVITY)
                    .enableCache(CacheFlag.VOICE_STATE)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .setStatus(OnlineStatus.IDLE);
            //event listeners

            jda.addEventListeners(new EventsManager());

            shardManager = jda.build();
            System.out.println(ConsoleColours.GREEN + "Bot online" + ConsoleColours.RESET);
            //change activity and profile picture
            changeUserInformation();


//        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Shutdown Hook is running !")));
//        System.out.println("Application Terminating ...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeUserInformation() throws Exception {
        //get profile pictures
        List<InputStream> profilePictures = new ArrayList();
        profilePictures.add(this.getClass().getClassLoader().getResourceAsStream("profilePicture1.png"));
        profilePictures.add(this.getClass().getClassLoader().getResourceAsStream("profilePicture2.png"));
        profilePictures.add(this.getClass().getClassLoader().getResourceAsStream("profilePicture3.png"));
        profilePictures.add(this.getClass().getClassLoader().getResourceAsStream("profilePicture4.png"));
        profilePictures.add(this.getClass().getClassLoader().getResourceAsStream("profilePicture5.png"));
        profilePictures.add(this.getClass().getClassLoader().getResourceAsStream("profilePicture6.png"));
        profilePictures.add(this.getClass().getClassLoader().getResourceAsStream("profilePicture7.png"));
        profilePictures.add(this.getClass().getClassLoader().getResourceAsStream("profilePicture8.png"));
        //get a random one
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Get random number
                int random = new Random().nextInt(profilePictures.size());
                //change profile
                shardManager.getShards().forEach(bot -> {
                    try {
                        //change status
                        bot.getPresence().setActivity(Activity.listening("~help │ v0.6.3 │ " + bot.getGuilds().size() + " servers"));
                        //change profile picture
                        bot.getSelfUser().getManager().setAvatar(
                                Icon.from(profilePictures.get(random))).queue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }, 5 * 1000, 45 * 100 * 1000);
    }
}