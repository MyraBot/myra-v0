package com.myra.dev.marian.APIs;

import com.myra.dev.marian.utilities.management.Manager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

public class TopGG {


    public static String getUpVotes() throws Exception {
        OkHttpClient client = new OkHttpClient();
        //make get request
        Request channel = new Request.Builder()
                .url("https://top.gg/api/bots/718444709445632122")
                .header("Authorization", Manager.getUtilities().topGgKey)
                .build();
        //execute call
        String channelOutput;
        try (Response channelResponse = client.newCall(channel).execute()) {
            channelOutput = channelResponse.body().string();
        }
        //create Json object
        String votes = new JSONObject(channelOutput).get("points").toString();


        return votes;
    }

    public static void test() throws IOException {
 /*       DiscordBotListAPI api = new DiscordBotListAPI.Builder()
                .token(Manager.getUtilities().topGgKey)
                .botId("718444709445632122")
                .build();

        System.out.println(api.getStats("votes"));*/
    }
}