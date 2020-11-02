package com.myra.dev.marian.database.allMethods;

import com.myra.dev.marian.APIs.Twitch;
import com.myra.dev.marian.database.MongoDb;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class GetNotificationManager {
    //variables
    private MongoDb mongoDb;
    private Guild guild;

    //constructor
    public GetNotificationManager(MongoDb mongoDb, Guild guild) {
        this.mongoDb = mongoDb;
        this.guild = guild;
    }

    //get streamers
    public List<String> getStreamers() {
        return mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first().getList("streamers", String.class);
    }

    //get channel
    public String getChannel() {
        return mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first().getString("notificationChannel");
    }

    //add streamer
    public void addStreamer(GuildMessageReceivedEvent event) throws Exception {
        //connect to database
        Database db = new Database(event.getGuild());
        //split message
        String[] sentMessage = event.getMessage().getContentRaw().split("\\s+");
        //get streamer
        org.json.JSONObject searchedStreamer = searchUser(sentMessage[2]);
        //define embed builder
        EmbedBuilder streamer = new EmbedBuilder()
                .setAuthor("streamers", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().blue);
        //if no streamer is found
        if (searchedStreamer.isEmpty()) {
            Manager.getUtilities().error(event.getChannel(), "streamer", "\uD83D\uDCF9", "No streamer found", "Couldn't found the given streamer", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //get streamers
        List<String> streamers = db.getNotificationManager().getStreamers();
        //get values
        String user = searchedStreamer.getString("user");
        //remove streamer
        if (streamers.contains(user)) {
            //remove from HashMap
            streamers.remove(user);
            streamer
                    .addField("\uD83D\uDD15 │ Removed streamer", "Removed **" + user + "**", false)
                    .setThumbnail(searchedStreamer.getString("profilePicture"));
        }
        //add streamer
        else {
            //add to HashMap
            streamers.add(user);
            streamer
                    .addField("\uD83D\uDD14 │ Added streamer", "Added **" + user + "**", false)
                    .setThumbnail(searchedStreamer.getString("profilePicture"));
        }
        //save in database
        Bson updateGuildDoc = new Document("$set", new Document("streamers", streamers));
        mongoDb.getCollection("guilds").findOneAndUpdate(
                mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first(),
                updateGuildDoc
        );
        //sent message
        event.getChannel().sendMessage(streamer.build()).queue();
    }

    //make twitch request
    private org.json.JSONObject searchUser(String channelName) throws Exception {
        /**
         * stream request
         */
        OkHttpClient client = new OkHttpClient();
        //search channel request
        Request channel = new Request.Builder()
                .addHeader("client-id", Manager.getUtilities().twitchClientId)
                .addHeader("Authorization", "Bearer " + Twitch.accessToken)
                .url("https://api.twitch.tv/helix/search/channels?query=" + channelName)
                .build();
        //execute call
        String channelOutput = null;
        try (Response channelResponse = client.newCall(channel).execute()) {
            channelOutput = channelResponse.body().string();
        }
        //create Json object
        org.json.JSONObject JsonChannel = new org.json.JSONObject(channelOutput);
        //if no channel found
        if (JsonChannel.getJSONArray("data").length() == 0) {
            System.out.println("no user found");
            return new org.json.JSONObject();
        }
        org.json.JSONObject channelData = JsonChannel.getJSONArray("data").getJSONObject(0);
        //search values
        String user = channelData.getString("display_name");
        String profilePicture = channelData.getString("thumbnail_url");
        return new org.json.JSONObject().put("user", user).put("profilePicture", profilePicture);
    }
}
