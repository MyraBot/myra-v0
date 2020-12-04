package com.myra.dev.marian.listeners.notifications;

import com.myra.dev.marian.APIs.Twitch;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.database.managers.NotificationsTwitchManager;
import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TwitchNotification {

    public void jdaReady(ReadyEvent event) throws Exception {
        Utilities.TIMER.scheduleAtFixedRate(() -> {   // Loop
            try {
                final Iterator<Guild> guilds = event.getJDA().getGuilds().iterator(); // Create an iterator for the guilds
                while (guilds.hasNext()) { // Loop through every guild
                    final Guild guild = guilds.next(); // Get next guild
                    Database db = new Database(guild); // Get database

                    // Get variables
                    List<String> streamers = NotificationsTwitchManager.getInstance().getStreamers(guild); // Get all streamers
                    String channelRaw = (String) db.getNested("notifications").get("channel");

                    if (streamers.isEmpty()) continue;  // No streamers are set

                    // If no notifications channel is set
                    if (channelRaw.equals("not set")) {
                        Utilities.getUtils().error(guild.getDefaultChannel(), "notifications", "\uD83D\uDD14", "No notifications channel specified", "To set a notifications channel type in `" + db.get("prefix") + "notification channel <channel>`", guild.getIconUrl());
                        continue;
                    }
                    TextChannel channel = guild.getTextChannelById(channelRaw); // Get notifications channel

                    // For each streamer
                    for (String streamer : streamers) {
                        JSONObject stream = new Twitch().getStream(streamer); // Get stream information

                        // If no stream is found
                        if (stream == null) {
                            Utilities.getUtils().error(channel, "notifications twitch", "\uD83D\uDD14", "No channel found", "**" + streamer + "** doesn't exist anymore", guild.getIconUrl());
                            continue;
                        }

                        // Streamer is offline
                        if (!stream.getBoolean("is_live")) continue;

                        //get stream start
                        System.out.println(stream.getString("started_at"));
                        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
                        final String strDate = stream.getString("started_at").replace("Z", "");
                        final String startedAt = dtf.format(LocalDateTime.parse(strDate));
                        System.out.println(startedAt);

                        boolean nextStreamer = true; // Create a variable to check if the stream notification was already send
                        for (Message message : channel.getHistory().retrievePast(25).complete()) { // Check the 25 latest messages
                            final List<MessageEmbed> embeds = message.getEmbeds(); // Get all embeds
                            // Message has no embeds or has no footer
                            if (embeds.isEmpty() || embeds.get(0).getFooter() == null) continue;
                            // If embed with same footer was already sent
                            if (embeds.get(0).getFooter().getText().equals(startedAt)) nextStreamer = false;
                        }
                        if (!nextStreamer) continue; // Skip streamer if stream notification was already sent

                        // Get all values
                        final String id = stream.getString("id");
                        final String name = stream.getString("display_name"); // Get user name of streamer
                        final String title = stream.getString("title"); // Get stream title
                        final String thumbnail = stream.getString("thumbnail_url"); // Get profile picture
                        // Create embed
                        EmbedBuilder notification = new EmbedBuilder()
                                .setAuthor(name, "https://www.twitch.tv/" + name, thumbnail)
                                .setColor(Utilities.getUtils().blue)
                                .setDescription(Utilities.getUtils().hyperlink(title, "https://www.twitch.tv/" + name) + "\n")
                                .setThumbnail(thumbnail)
                                .setImage("https://static-cdn.jtvnw.net/previews-ttv/live_user_" + name + "-440x248.jpg")
                                .setFooter(startedAt);
                        // If streamer set a game
                        if (!stream.getString("game_id").equals("0")) {
                            notification.appendDescription(new Twitch().getGame(stream.getString("game_id"))); // Add game to notification
                        }
                        channel.sendMessage(notification.build()).queue(); // Send stream notification
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, 5, TimeUnit.MINUTES);
    }
}
