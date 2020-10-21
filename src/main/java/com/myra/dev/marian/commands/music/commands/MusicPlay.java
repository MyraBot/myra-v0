package com.myra.dev.marian.commands.music.commands;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.SearchResult;
import com.myra.dev.marian.commands.music.Music.PlayerManager;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.MessageReaction;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@CommandSubscribe(
        name = "play"
)
public class MusicPlay extends Events implements Command {
    private static HashMap<String, List<SearchResult>> results = new HashMap<>();

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        //command usage
        if (arguments.length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("play", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "play <song>`", "\uD83D\uDCBF │ add a song to the queue*", false)
                    .setFooter("supported platforms: YoutTube, SoundCloud, Bandcamp, Vimeo, Twitch streams");
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        // Get song
        String song = utilities.getString(arguments);
        // If song is url
        try {
            new URL(song).toURI();
            // Delete message
            event.getMessage().delete().queue();
            // Play song
            PlayerManager.getInstance().loadAndPlay(event.getChannel(), song, event.getAuthor().getEffectiveAvatarUrl(), null);
        }
        // If song is given by name
        catch (Exception e) {
            // Connect to YouTube
            YouTube youTube = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("Myra").setYouTubeRequestInitializer(new YouTubeRequestInitializer(utilities.youTubeKey)).build();

            YouTube.Search.List search = youTube.search().list("id,snippet")
                    //Search for keyword
                    .setQ(song)
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
                        "YouTube could not find **" + song + "**",
                        event.getAuthor().getEffectiveAvatarUrl());
                return;
            }
            // Song menu
            EmbedBuilder songs = new EmbedBuilder()
                    .setAuthor("choose a song", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().blue)
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
            MessageReaction.add("play", message.getId(), event.getChannel(), false);
        }
        // Set volume
        PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).player.setVolume(25);
    }

    //chose song
    @Override
    public void guildMessageReactionAddEvent(GuildMessageReactionAddEvent event) throws Exception {
        // Return if author is bot
        if (event.getUser().isBot()) return;
        //If reaction is on wrong message
        if (!results.containsKey(event.getMessageId())) return;
        // Get chosen song
        SearchResult song = results.get(event.getMessageId()).get(Integer.parseInt(event.getReactionEmote().getEmoji().replace("1️⃣", "0").replace("2️⃣", "1").replace("3️⃣", "2").replace("4️⃣", "3").replace("5️⃣", "4")));
        if (song == null) return;
        // Check for right reaction
        if (!MessageReaction.check(event, "play")) return;
        //get video url
        String videoUrl = "https://www.youtube.com/watch?v=" + song.getId().getVideoId();
        //play song
        PlayerManager.getInstance().loadAndPlay(event.getChannel(), videoUrl, event.getUser().getEffectiveAvatarUrl(), "https://img.youtube.com/vi/" + song.getId().getVideoId() + "/maxresdefault.jpg");
        // Set volume
        PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).player.setVolume(25);
        //delete track selector
        event.getChannel().deleteMessageById(event.getMessageId()).queue();
    }
}