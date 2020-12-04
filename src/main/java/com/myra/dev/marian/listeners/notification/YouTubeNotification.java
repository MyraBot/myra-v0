package com.myra.dev.marian.listeners.notification;

import com.myra.dev.marian.APIs.YouTube;
import com.myra.dev.marian.database.MongoDb;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.database.managers.NotificationsYoutubeManager;
import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import org.bson.Document;
import org.json.JSONObject;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class YouTubeNotification {
    private final MongoDb mongoDb = MongoDb.getInstance(); // Get database

    public void start(ReadyEvent event) throws Exception {
        Utilities.TIMER.scheduleAtFixedRate(() -> {   // Loop
            try {
                final Iterator<Guild> guilds = event.getJDA().getGuilds().iterator(); // Create an iterator for the guilds
                while (guilds.hasNext()) { // Loop through every guild
                    final Guild guild = guilds.next(); // Get next guild
                    Database db = new Database(guild); // Get database

                    // Get variables
                    List<String> youtubers = NotificationsYoutubeManager.getInstance().getYoutubers(guild); // Get all streamers
                    String channelRaw = (String) db.getNested("notifications").get("channel");

                    if (youtubers.isEmpty()) continue;  // No streamers are set

                    // If no notification channel is set
                    if (channelRaw.equals("not set")) {
                        Utilities.getUtils().error(guild.getDefaultChannel(), "notification", "\uD83D\uDD14", "No notification channel specified", "To set a notification channel type in `" + db.get("prefix") + "notification channel <channel>`", guild.getIconUrl());
                        continue;
                    }
                    TextChannel channel = guild.getTextChannelById(channelRaw); // Get notification channel

                    // For each youtuber
                    for (String channelId : youtubers) {
                        List<String> latestVideos = YouTube.getInstance().getLatestVideos(channelId); // Get the latest videos
                        // For every video
                        for (String videoId : latestVideos) {

                            final JSONObject video = YouTube.getInstance().getVideoById(videoId); // Get video information

                            // Get upload time
                            final ZonedDateTime date = ZonedDateTime.parse(video.getString("publishedAt"));
                            long publishedAtInMillis = date.toInstant().toEpochMilli(); // Get upload time in milliseconds

                            // Last youtube check was already made when the video came out
                            if (publishedAtInMillis < mongoDb.getCollection("config").find().first().getLong("youtube refresh")) continue;

                            // Get all values
                            final JSONObject channelInformation = YouTube.getInstance().getChannelById(video.getString("channelId")); // Get the channel information
                            final String profilePicture = channelInformation.getJSONObject("thumbnails").getJSONObject("medium").getString("url"); // Get profile picture

                            final String channelName = video.getString("channelTitle");
                            final String title = video.getString("title"); // Get video title
                            final String thumbnail = video.getJSONObject("thumbnails").getJSONObject("standard").getString("url"); // Get thumbnail image
                            System.out.println(video.getJSONObject("thumbnails"));
                            // Create embed
                            EmbedBuilder notification = new EmbedBuilder()
                                    .setAuthor(channelName, "https://www.youtube.com/watch?v=" + videoId, profilePicture)
                                    .setColor(Utilities.getUtils().blue)
                                    .setDescription(Utilities.getUtils().hyperlink(title, "https://www.youtube.com/watch?v=" + videoId) + "\n")
                                    .setThumbnail(profilePicture)
                                    .setImage(thumbnail)
                                    .setTimestamp(date.toInstant());
                            channel.sendMessage(notification.build()).queue(); // Send video notification
                        }
                    }
                }
                // Update last refresh in database
                final Document updatedDocument = mongoDb.getCollection("config").find().first(); // Get config document
                updatedDocument.replace("youtube refresh", System.currentTimeMillis()); // Update last check
                mongoDb.getCollection("config").findOneAndReplace(mongoDb.getCollection("config").find().first(), updatedDocument); // Update document

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, 5, TimeUnit.MINUTES);
    }
}
