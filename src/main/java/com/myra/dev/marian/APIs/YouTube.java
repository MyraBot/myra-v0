package com.myra.dev.marian.APIs;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.SearchResult;
import com.myra.dev.marian.utilities.Utilities;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class YouTube {
    private final static YouTube YOU_TUBE = new YouTube();
    private final static OkHttpClient client = new OkHttpClient();

    public static YouTube getInstance() {
        return YOU_TUBE;
    }

    public List<SearchResult> search(String video) throws IOException, GeneralSecurityException {
        // Connect to YouTube
        final com.google.api.services.youtube.YouTube youTube = new com.google.api.services.youtube.YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                new HttpRequestInitializer() {
                    public void initialize(HttpRequest request) throws IOException {
                    }
                })
                .setApplicationName("CloudStudios bot")
                .setYouTubeRequestInitializer(new YouTubeRequestInitializer(Utilities.getUtils().youTubeKey))
                .build();

        com.google.api.services.youtube.YouTube.Search.List search = youTube.search().list("id,snippet")
                //Search for keyword
                .setQ(video)
                // Return only videos
                .setType("video")
                // Number of returned videos (maximum is 50)
                .setMaxResults(5L)
                .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
        // Save results in variable
        return search.execute().getItems();
    }


    public JSONObject getChannelByName(String name) throws IOException {
        // Create channel get request
        Request request = new Request.Builder()
                .url("https://www.googleapis.com/youtube/v3/search?" +
                        "part=snippet" +
                        "&type=channel" +
                        //"&order=rating" +
                        "&q=" + name +
                        "&maxResults=1" +
                        "&key=" + Utilities.getUtils().youTubeKey
                )
                .build();
        //Execute call
        String channelOutput;
        try (Response channelResponse = client.newCall(request).execute()) {
            channelOutput = channelResponse.body().string();
        }

        final JSONArray items = new JSONObject(channelOutput).getJSONArray("items"); // Create Json object
        final JSONObject channel = items.getJSONObject(0);
        final JSONObject channelInformation = channel.getJSONObject("snippet");
        return channelInformation;
    }

    public JSONObject getChannelByUrl(String url) throws IOException {
        // Channel has no custom url
        if (url.startsWith("https://www.youtube.com/channel/")) {
            url = url.replace("?view_as=subscriber", ""); // Remove 'view_as=subscriber' tag
            final String id = url.split("/")[4]; // Get channel id
            // Create channel get request
            Request request = new Request.Builder()
                    .url("https://www.googleapis.com/youtube/v3/search?" +
                            "part=snippet" +
                            "&type=channel" +
                            "&channelId=" + id +
                            "&maxResults=1" +
                            "&key=" + Utilities.getUtils().youTubeKey
                    )
                    .build();
            // Execute call
            String channelOutput;
            try (Response channelResponse = client.newCall(request).execute()) {
                channelOutput = channelResponse.body().string();
            }

            final JSONArray items = new JSONObject(channelOutput).getJSONArray("items"); // Create Json object
            final JSONObject channel = items.getJSONObject(0); // Get first channel
            final JSONObject channelInformation = channel.getJSONObject("snippet");
            return channelInformation;
        }

        // Channel has custom url
        if (url.startsWith("https://www.youtube.com/user/")) {
            final String name = url.split("/")[4]; // Get channel id
            // Create channel get request
            Request request = new Request.Builder()
                    .url("https://www.googleapis.com/youtube/v3/search?" +
                            "part=snippet" +
                            "&type=channel" +
                            "&q=" + name +
                            "&maxResults=1" +
                            "&key=" + Utilities.getUtils().youTubeKey
                    )
                    .build();

            //Execute call
            String channelOutput;
            try (Response channelResponse = client.newCall(request).execute()) {
                channelOutput = channelResponse.body().string();
            }

            final JSONArray items = new JSONObject(channelOutput).getJSONArray("items"); // Create Json object
            final JSONObject channel = items.getJSONObject(0); // Get first channel
            final JSONObject channelInformation = channel.getJSONObject("snippet");
            return channelInformation;
        }

        return null; // Error
    }

    public JSONObject getChannelById(String id) throws IOException {
        // Create channel get request
        Request request = new Request.Builder()
                .url("https://www.googleapis.com/youtube/v3/search?" +
                        "part=snippet" +
                        "&type=channel" +
                        "&channelId=" + id +
                        "&maxResults=1" +
                        "&key=" + Utilities.getUtils().youTubeKey
                )
                .build();
        //Execute call
        String channelOutput;
        try (Response channelResponse = client.newCall(request).execute()) {
            channelOutput = channelResponse.body().string();
        }

        final JSONArray items = new JSONObject(channelOutput).getJSONArray("items"); // Create Json object
        final JSONObject channel = items.getJSONObject(0);
        final JSONObject channelInformation = channel.getJSONObject("snippet");
        return channelInformation;
    }

    public List<String> getLatestVideos(String channelId) throws IOException {
        // Create video get request
        Request request = new Request.Builder()
                .url(
                        "https://www.googleapis.com/youtube/v3/search?" +
                                "snippet=" +
                                "type=video" +
                                "&channelId=" + channelId +
                                "&order=date" +
                                "&key=" + Utilities.getUtils().youTubeKey
                ) // Will return by default 5 videos
                .build();
        // Execute call
        String channelOutput;
        try (Response channelResponse = client.newCall(request).execute()) {
            channelOutput = channelResponse.body().string();
        }

        final JSONArray items = new JSONObject(channelOutput).getJSONArray("items"); // Create JSON object
        List<String> ids = new ArrayList<String>(); // Create a list for all ids
        for (Object videoObject : items) { // Loop through every video
            final JSONObject video = (JSONObject) videoObject; // Pass Object to JSON object
            final String id = video.getJSONObject("id").getString("videoId"); // Get video id
            ids.add(id); // Add video id to list
        }

        return ids; // Return the list
    }

    public JSONObject getVideoById(String id) throws IOException {
        // Create video get request
        Request request = new Request.Builder()
                .url(
                        "https://www.googleapis.com/youtube/v3/videos?" +
                                "part=snippet" +
                                "&id=" + id +
                                "&maxResults=1" +
                                "&key=" + Utilities.getUtils().youTubeKey
                ) // Will return by default 5 videos
                .build();
        // Execute call
        String channelOutput;
        try (Response channelResponse = client.newCall(request).execute()) {
            channelOutput = channelResponse.body().string();
        }

        final JSONArray items = new JSONObject(channelOutput).getJSONArray("items"); // Create JSON object
        final JSONObject video = items.getJSONObject(0).getJSONObject("snippet"); // Get first video information

        return video; // Return video information
    }
}
