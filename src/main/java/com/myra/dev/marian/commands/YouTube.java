package com.myra.dev.marian.commands;

import net.dv8tion.jda.api.entities.TextChannel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

public class YouTube {
    OkHttpClient client = new OkHttpClient();

    //make twitch request
    public void twitchRequest(String channelName, TextChannel textChannel) throws IOException {
        /**
         * channel request
         */
        //search channel request
        Request channel = new Request.Builder()
                .url("https://www.googleapis.com/youtube/v3/channels")
                .build();
        //execute call
        String channelOutput = null;
        try (Response channelResponse = client.newCall(channel).execute()) {
            channelOutput = channelResponse.body().string();
        }
        //create Json object
        JSONObject JsonChannel = new JSONObject(channelOutput);
        //if no channel found

    }
}
