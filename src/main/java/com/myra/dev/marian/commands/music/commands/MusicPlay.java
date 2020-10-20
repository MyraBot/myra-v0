package com.myra.dev.marian.commands.music.commands;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.SearchResult;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.commands.music.Music.PlayerManager;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.MessageReaction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
@CommandSubscribe(
        name = "play"
)
public class MusicPlay extends Events implements Command {
    private YouTube youtube;
    static List<SearchResult> results;

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //split message
        String[] sentMessage = event.getMessage().getContentRaw().split("\\s+", 2);
        //command usage
        if (sentMessage.length == 1) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("│ add song", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "play <song>`", "\uD83D\uDCBF │ add a song to the queue*", false)
                    .setFooter("supported platforms: YoutTube, SoundCloud, Bandcamp, Vimeo, Twitch streams");
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        //add song from url
        if (sentMessage[1].startsWith("https://")) {
            //delete messages
            event.getMessage().delete().queue();
            //play song
            PlayerManager.getInstance().loadAndPlay(event.getChannel(), sentMessage[1], event.getAuthor().getEffectiveAvatarUrl(), null);
            PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).player.setVolume(25);
        }
        //connect to YouTube
        youtube = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("Myra").setYouTubeRequestInitializer(new YouTubeRequestInitializer(Manager.getUtilities().youTubeKey)).build();
        //search YouTube
        if (!sentMessage[1].startsWith("https://")) {
            YouTube.Search.List search = youtube.search().list("id,snippet");
            //search keyword eg. "Sean Paul - She Doesn't Mind"
            search.setQ(sentMessage[1]);
            //return only video type data
            search.setType("video");
            // number of video data new Return(). maximum is 50
            search.setMaxResults(5L);
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            //save results in variable
            results = search.execute().getItems();
            //nothing found
            if (results == null) {
                Manager.getUtilities().error(
                        event.getChannel(),
                        "play", "\uD83D\uDCBF",
                        "No results",
                        "YouTube could not find **" + sentMessage[1] + "**",
                        event.getAuthor().getEffectiveAvatarUrl());
                return;
            }
            //choose song
            if (!results.isEmpty()) {
                EmbedBuilder songs = new EmbedBuilder()
                        .setAuthor("choose a song", null, event.getAuthor().getEffectiveAvatarUrl())
                        .setColor(Manager.getUtilities().blue)
                        .addField("\uD83D\uDD0D │ track 1\uFE0F\u20E3", results.get(0).getSnippet().getTitle(), false)
                        .addField("\uD83D\uDD0D │ track 2\uFE0F\u20E3", results.get(1).getSnippet().getTitle(), false)
                        .addField("\uD83D\uDD0D │track 3\uFE0F\u20E3", results.get(2).getSnippet().getTitle(), false)
                        .addField("\uD83D\uDD0D │track 4\uFE0F\u20E3", results.get(3).getSnippet().getTitle(), false)
                        .addField("\uD83D\uDD0D │ track 5\uFE0F\u20E3", results.get(4).getSnippet().getTitle(), false);
                Message message = event.getChannel().sendMessage(songs.build()).complete();
                //add reactions
                message.addReaction("1\uFE0F\u20E3").queue();
                message.addReaction("2\uFE0F\u20E3").queue();
                message.addReaction("3\uFE0F\u20E3").queue();
                message.addReaction("4\uFE0F\u20E3").queue();
                message.addReaction("5\uFE0F\u20E3").queue();

                message.addReaction("\uD83D\uDEAB").queue();
                //add to HashMap
                MessageReaction.add("musicPlay", message.getId(), event.getChannel(), false);
            }
        }
    }

    //chose song
    @Override
    public void guildMessageReactionAddEvent(GuildMessageReactionAddEvent event) throws Exception {
        //if reaction was added on the wrong message return
        if (MessageReaction.hashMap.get("musicPlay") == null) return;
        if (!Arrays.stream(MessageReaction.hashMap.get("musicPlay").toArray()).anyMatch(event.getMessageId()::equals) || event.getUser().isBot())
            return;

        if (event.getReaction().getReactionEmote().getEmoji().equals("\uD83D\uDEAB")) {
            event.getChannel().retrieveMessageById(event.getMessageId()).complete().delete().queue();
            return;
        }

        //get id of video
        SearchResult song = results.get(Integer.parseInt(event.getReactionEmote().getEmoji().replace("1️⃣", "0").replace("2️⃣", "1").replace("3️⃣", "2").replace("4️⃣", "3").replace("5️⃣", "4")));
        //get video url
        String videoUrl = "https://www.youtube.com/watch?v=" + song.getId().getVideoId();
        //play song
        PlayerManager.getInstance().loadAndPlay(event.getChannel(), videoUrl, event.getUser().getEffectiveAvatarUrl(), "https://img.youtube.com/vi/" + song.getId().getVideoId() + "/maxresdefault.jpg");
        PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).player.setVolume(25);
        //delete track selector
        event.getChannel().deleteMessageById(event.getMessageId()).queue();
    }
}