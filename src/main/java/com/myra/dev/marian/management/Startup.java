package com.myra.dev.marian.management;

import com.myra.dev.marian.APIs.Twitch;
import com.myra.dev.marian.Bot;
import com.myra.dev.marian.commands.general.Reminder;
import com.myra.dev.marian.commands.moderation.mute.Tempmute;
import com.myra.dev.marian.database.MongoDbUpdate;
import com.myra.dev.marian.listeners.notification.Notification;
import com.myra.dev.marian.marian.Roles;
import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Startup extends ListenerAdapter {
    public static boolean next = false;
    final private List<InputStream> profilePictures = Arrays.asList(
            this.getClass().getClassLoader().getResourceAsStream("profilePicture1.png"),
            this.getClass().getClassLoader().getResourceAsStream("profilePicture2.png"),
            this.getClass().getClassLoader().getResourceAsStream("profilePicture3.png"),
            this.getClass().getClassLoader().getResourceAsStream("profilePicture4.png"),
            this.getClass().getClassLoader().getResourceAsStream("profilePicture5.png"),
            this.getClass().getClassLoader().getResourceAsStream("profilePicture6.png"),
            this.getClass().getClassLoader().getResourceAsStream("profilePicture7.png"),
            this.getClass().getClassLoader().getResourceAsStream("profilePicture8.png"),
            this.getClass().getClassLoader().getResourceAsStream("profilePicture9.png")
    );

    public void onReady(ReadyEvent event) {
        try {
            Utilities.getUtils().loadEmotes(event.getJDA()); // Load emotes

            // Update database
            new MongoDbUpdate().updateDatabase(event);

            while (!next) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // restore interrupted status
                }
            }
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
            // Marian's Discord role
            new Roles().jdaReady(event);

            // Register features
            Bot.shardManager.removeEventListener(new Startup()); // Remove this class from the events
            Bot.shardManager.addEventListener(new EventsManager()); // Add all other classes
            // Load all commands
            new Manager().registerFeatures();

            online(); // Change profile picture and activity
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void online() {
        // Get a random one
        Utilities.TIMER.scheduleAtFixedRate(() -> {
            // Get random number
            int random = new Random().nextInt(profilePictures.size());
            // Change profile
            Bot.shardManager.getShards().forEach(bot -> {
                try {
                    // Change status
                    bot.getPresence().setActivity(Activity.listening("~help │ " + bot.getGuilds().size() + " servers"));
                    // Change profile picture
                    bot.getSelfUser().getManager().setAvatar(
                            Icon.from(profilePictures.get(random))).queue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }, 0, 1, TimeUnit.HOURS);
    }
}
