package com.myra.dev.marian.APIs;

import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Twitch extends Events {
    public static String accessToken;

    //get access token
    public void jdaReady(ReadyEvent event) throws IOException {
        OkHttpClient client = new OkHttpClient();
        //form parameters
        RequestBody body = new FormBody.Builder()
                .add("scope", "channel_read")
                .add("client_id", Manager.getUtilities().twitchClientId)
                .add("client_secret", Manager.getUtilities().twitchClientSecret)
                .add("grant_type", "client_credentials")
                .build();
        //build request
        Request request = new Request.Builder()
                .url("https://id.twitch.tv/oauth2/token")
                .post(body)
                .build();
        //make request
        try (Response response = client.newCall(request).execute()) {
            //return access token
            String output = response.body().string();
            JSONObject obj = new JSONObject(output);
            String accessToken = obj.getString("access_token");
            Twitch.accessToken = accessToken;
        }
    }

    //get game
    public String getGame(String gameId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        //search channel request
        Request game = new Request.Builder()
                .addHeader("client-id", Manager.getUtilities().twitchClientId)
                .addHeader("Authorization", "Bearer " + Twitch.accessToken)
                .url("https://api.twitch.tv/helix/games?id=" + gameId)
                .build();
        //make request
        try (Response response = client.newCall(game).execute()) {
            //return access token
            String output = response.body().string();
            JSONObject obj = new JSONObject(output);
            String gameName = obj.getJSONArray("data").getJSONObject(0).getString("name");
            return gameName;
        }
    }

    //request stream
    public static EmbedBuilder twitchRequest(String channelName, TextChannel textChannel, Guild guild, String avatar) throws IOException {
        OkHttpClient client = new OkHttpClient();
        //search channel request
        Request channel = new Request.Builder()
                .addHeader("client-id", Manager.getUtilities().twitchClientId)
                .addHeader("Authorization", "Bearer " + accessToken)
                .url("https://api.twitch.tv/helix/search/channels?query=" + channelName)
                .build();
        //execute call
        String channelOutput = null;
        try (Response channelResponse = client.newCall(channel).execute()) {
            channelOutput = channelResponse.body().string();
        }
        //create Json object
        JSONObject JsonChannel = new JSONObject(channelOutput);
        //if no channel found
        if (JsonChannel.getJSONArray("data").length() == 0) {
            Manager.getUtilities().error(textChannel, "notification twitch", "\uD83D\uDD14", "No channel found", "**" + channelName + "** doesn't exist", avatar);
        }
        JSONObject channelData = JsonChannel.getJSONArray("data").getJSONObject(0);
        // Return null if stream is offline
        boolean live = channelData.getBoolean("is_live");
        if (!live) return null;
        //get values
        String id = channelData.getString("id");
        String name = channelData.getString("display_name");
        String title = channelData.getString("title");
        String thumbnail = channelData.getString("thumbnail_url");
        String game = new Twitch().getGame(channelData.getString("game_id"));
        //get stream start
        DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        String strDate = channelData.getString("started_at").replace("Z", "");
        String startedAt = dtf.format(LocalDateTime.parse(strDate));
// Build embed
        EmbedBuilder twitch = new EmbedBuilder()
                .setAuthor(name, "https://www.twitch.tv/" + name, thumbnail)
                .setColor(Manager.getUtilities().blue)
                .setDescription(Manager.getUtilities().hyperlink(title, "https://www.twitch.tv/" + name) + "\n" +
                        game
                )
                .setThumbnail(thumbnail)
                .setImage("https://static-cdn.jtvnw.net/previews-ttv/live_user_" + name + "-1280x720.jpg")
                .setFooter(startedAt);
        return twitch;
    }
}
