package com.myra.dev.marian.commands.music.commands;

import com.google.api.services.youtube.model.SearchResult;
import com.myra.dev.marian.APIs.LavaPlayer.PlayerManager;
import com.myra.dev.marian.APIs.YouTube;
import com.myra.dev.marian.utilities.MessageReaction;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("ConstantConditions") // Requires '.enableCache(CacheFlag.VOICE_STATE)' to be not null
@CommandSubscribe(
        command = "play",
        name = "play"
)
public class MusicPlay extends Events implements Command {
    private static HashMap<String, List<SearchResult>> results = new HashMap<>();

    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        //command usage
        if (ctx.getArguments().length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("play", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + ctx.getPrefix() + "play <song>`", "\uD83D\uDCBF │ add a song to the queue*", false)
                    .setFooter("supported platforms: YoutTube, SoundCloud, Bandcamp, Vimeo, Twitch streams");
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
// Add a audio track to the queue
        // If bot isn't in a voice channel
        if (!ctx.getGuild().getSelfMember().getVoiceState().inVoiceChannel()) {
            utilities.error(ctx.getChannel(), "play", "\uD83D\uDCBF", "I need to be in a voice channel", "Use `" + ctx.getPrefix() + "join` to let me join a voice channel", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // If author isn't in a voice channel yet
        if (!ctx.getEvent().getMember().getVoiceState().inVoiceChannel()) {
            utilities.error(ctx.getChannel(), "play", "\uD83D\uDCBF", "You need to join a voice channel first to use this command", "Use `" + ctx.getPrefix() + "join` to let me join a voice channel", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // If author isn't in the same voice channel as bot
        if (!ctx.getEvent().getMember().getVoiceState().getChannel().equals(ctx.getGuild().getSelfMember().getVoiceState().getChannel())) {
            utilities.error(ctx.getChannel(), "play", "\uD83D\uDCBF", "You need to be in the same voice channel as me to use this command", "Join `" + ctx.getGuild().getSelfMember().getVoiceState().getChannel().getName() + "` to use this command", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // Get song
        String song = utilities.getString(ctx.getArguments());
        // If song is url
        try {
            new URL(song).toURI();
            // Delete message
            ctx.getEvent().getMessage().delete().queue();
            // Play song
            PlayerManager.getInstance().loadAndPlay(ctx.getChannel(), song, ctx.getAuthor().getEffectiveAvatarUrl(), null);
        }
        // If song is given by name
        catch (Exception e) {
            // Search on YouTube for song name
            List<SearchResult> searchResults = YouTube.getInstance().search(song);
            // Nothing found
            if (results == null) {
                utilities.error(
                        ctx.getChannel(),
                        "play", "\uD83D\uDCBF",
                        "No results",
                        "YouTube could not find **" + song + "**",
                        ctx.getAuthor().getEffectiveAvatarUrl());
                return;
            }
            // Song menu
            EmbedBuilder songs = new EmbedBuilder()
                    .setAuthor("choose a song", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.blue)
                    .addField("\uD83D\uDD0D │ track 1\uFE0F\u20E3", searchResults.get(0).getSnippet().getTitle(), false)
                    .addField("\uD83D\uDD0D │ track 2\uFE0F\u20E3", searchResults.get(1).getSnippet().getTitle(), false)
                    .addField("\uD83D\uDD0D │ track 3\uFE0F\u20E3", searchResults.get(2).getSnippet().getTitle(), false)
                    .addField("\uD83D\uDD0D │ track 4\uFE0F\u20E3", searchResults.get(3).getSnippet().getTitle(), false)
                    .addField("\uD83D\uDD0D │ track 5\uFE0F\u20E3", searchResults.get(4).getSnippet().getTitle(), false);
            Message message = ctx.getChannel().sendMessage(songs.build()).complete();
            // Save results in HashMap
            results.put(message.getId(), searchResults);
            //add reactions
            message.addReaction("1\uFE0F\u20E3").queue();
            message.addReaction("2\uFE0F\u20E3").queue();
            message.addReaction("3\uFE0F\u20E3").queue();
            message.addReaction("4\uFE0F\u20E3").queue();
            message.addReaction("5\uFE0F\u20E3").queue();

            message.addReaction("\uD83D\uDEAB").queue();
            // Add reaction to HashMap
            MessageReaction.add("play", message.getId(), Arrays.asList("1\uFE0F\u20E3", "2\uFE0F\u20E3", "3\uFE0F\u20E3", "4\uFE0F\u20E3", "5\uFE0F\u20E3", "5\uFE0F\u20E3", "\uD83D\uDEAB"), ctx.getChannel(), ctx.getAuthor(), false);
        }
    }

    //chose song
    @Override
    public void guildMessageReactionAddEvent(GuildMessageReactionAddEvent event) throws Exception {
        if (!MessageReaction.check(event, "play")) return;
        // Get chosen song
        SearchResult song = results.get(event.getMessageId()).get(Integer.parseInt(event.getReactionEmote().getEmoji().replace("1️⃣", "0").replace("2️⃣", "1").replace("3️⃣", "2").replace("4️⃣", "3").replace("5️⃣", "4")));
        if (song == null) return;
        //get video url
        String videoUrl = "https://www.youtube.com/watch?v=" + song.getId().getVideoId();
        //play song
        PlayerManager.getInstance().loadAndPlay(event.getChannel(), videoUrl, event.getUser().getEffectiveAvatarUrl(), "https://img.youtube.com/vi/" + song.getId().getVideoId() + "/maxresdefault.jpg");
        //delete track selector
        event.getChannel().deleteMessageById(event.getMessageId()).queue();
    }
}