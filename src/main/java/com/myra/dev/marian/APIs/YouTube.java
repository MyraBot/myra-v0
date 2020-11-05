package com.myra.dev.marian.APIs;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.SearchResult;
import com.myra.dev.marian.utilities.management.Manager;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class YouTube {
    private final static YouTube YOU_TUBE = new YouTube();

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
                .setYouTubeRequestInitializer(new YouTubeRequestInitializer(Manager.getUtilities().youTubeKey))
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
}
