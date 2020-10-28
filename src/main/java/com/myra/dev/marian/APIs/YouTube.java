package com.myra.dev.marian.APIs;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.SearchResult;
import com.myra.dev.marian.utilities.MessageReaction;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;

public class YouTube {
    // Search song
    public void search(String name, HashMap<String, List<SearchResult>> results, GuildMessageReceivedEvent event) throws IOException, GeneralSecurityException {
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Connect to YouTube
        com.google.api.services.youtube.YouTube youTube = new com.google.api.services.youtube.YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("Myra").setYouTubeRequestInitializer(new YouTubeRequestInitializer(utilities.youTubeKey)).build();

        com.google.api.services.youtube.YouTube.Search.List search = youTube.search().list("id,snippet")
                //Search for keyword
                .setQ(name)
                // Return only videos
                .setType("video")
                // Number of returned videos (maximum is 50)
                .setMaxResults(5L)
                .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
        // Save results in variable
        List<SearchResult> searchResults = search.execute().getItems();
        // Nothing found
        if (results == null) {
            utilities.error(
                    event.getChannel(),
                    "play", "\uD83D\uDCBF",
                    "No results",
                    "YouTube could not find **" + name + "**",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // Song menu
        EmbedBuilder songs = new EmbedBuilder()
                .setAuthor("choose a song", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(utilities.blue)
                .addField("\uD83D\uDD0D │ track 1\uFE0F\u20E3", searchResults.get(0).getSnippet().getTitle(), false)
                .addField("\uD83D\uDD0D │ track 2\uFE0F\u20E3", searchResults.get(1).getSnippet().getTitle(), false)
                .addField("\uD83D\uDD0D │ track 3\uFE0F\u20E3", searchResults.get(2).getSnippet().getTitle(), false)
                .addField("\uD83D\uDD0D │ track 4\uFE0F\u20E3", searchResults.get(3).getSnippet().getTitle(), false)
                .addField("\uD83D\uDD0D │ track 5\uFE0F\u20E3", searchResults.get(4).getSnippet().getTitle(), false);
        Message message = event.getChannel().sendMessage(songs.build()).complete();
        // Save results in HashMap
        results.put(message.getId(), search.execute().getItems());
        //add reactions
        message.addReaction("1\uFE0F\u20E3").queue();
        message.addReaction("2\uFE0F\u20E3").queue();
        message.addReaction("3\uFE0F\u20E3").queue();
        message.addReaction("4\uFE0F\u20E3").queue();
        message.addReaction("5\uFE0F\u20E3").queue();

        message.addReaction("\uD83D\uDEAB").queue();
        // Add reaction to HashMap
        MessageReaction.add("play", message.getId(), event.getChannel(), event.getAuthor(), false);
    }
}
