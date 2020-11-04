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
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.net.URL;
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
            new YouTube().search(song, results, ctx.getEvent());
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