package com.myra.dev.marian.APIs;


import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Twitch {
    public static String accessToken;

    // Get access token
    public void jdaReady(ReadyEvent event) throws IOException {
        //form parameters
        RequestBody body = new FormBody.Builder()
                .add("scope", "channel_read")
                .add("client_id", Utilities.getUtils().twitchClientId)
                .add("client_secret", Utilities.getUtils().twitchClientSecret)
                .add("grant_type", "client_credentials")
                .build();
        //build request
        Request request = new Request.Builder()
                .url("https://id.twitch.tv/oauth2/token")
                .post(body)
                .build();
        //make request
        try (Response response = Utilities.HTTP_CLIENT.newCall(request).execute()) {
            //return access token
            String output = response.body().string();
            JSONObject obj = new JSONObject(output);
            String accessToken = obj.getString("access_token");
            Twitch.accessToken = accessToken;
        }
    }

    //get game
    public String getGame(String gameId) throws IOException {
        //search channel request
        Request game = new Request.Builder()
                .addHeader("client-id", Utilities.getUtils().twitchClientId)
                .addHeader("Authorization", "Bearer " + Twitch.accessToken)
                .url("https://api.twitch.tv/helix/games?id=" + gameId)
                .build();
        //make request
        try (Response response = Utilities.HTTP_CLIENT.newCall(game).execute()) {
            //return access token
            String output = response.body().string();
            JSONObject obj = new JSONObject(output);
            String gameName = obj.getJSONArray("data").getJSONObject(0).getString("name");
            return gameName;
        }
    }

    // Get user
    public JSONObject getChannel(String name) throws Exception {
        //search channel request
        Request channel = new Request.Builder()
                .addHeader("client-id", Utilities.getUtils().twitchClientId)
                .addHeader("Authorization", "Bearer " + Twitch.accessToken)
                .url("https://api.twitch.tv/helix/search/channels?query=" + name)
                .build();
        //execute call
        String channelOutput = null;
        try (Response channelResponse = Utilities.HTTP_CLIENT.newCall(channel).execute()) {
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
        return new JSONObject().put("user", user).put("profilePicture", profilePicture);
    }

    // Get stream
    public JSONObject getStream(String channelName) throws IOException {
        // Create channel http get request
        Request channel = new Request.Builder()
                .addHeader("client-id", Utilities.getUtils().twitchClientId)
                .addHeader("Authorization", "Bearer " + accessToken)
                .url("https://api.twitch.tv/helix/search/channels?query=" + channelName)
                .build();
        // Execute request
        String channelOutput = null;
        try (Response channelResponse = Utilities.HTTP_CLIENT.newCall(channel).execute()) {
            channelOutput = channelResponse.body().string();
        }

        JSONObject stream = new JSONObject(channelOutput); // Create Json object

        if (stream.getJSONArray("data").length() == 0) return null; // No channel found
        return stream.getJSONArray("data").getJSONObject(0); // Return channel information
    }
}
