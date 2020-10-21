package com.myra.dev.marian.commands.music.commands;

import com.myra.dev.marian.commands.music.Music.PlayerManager;
import com.myra.dev.marian.commands.music.Music.TrackScheduler;
import com.myra.dev.marian.utilities.MessageReaction;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.management.Manager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

@CommandSubscribe(
        name = "music controller"
)
public class MusicController extends Events implements Command {
    private static HashMap<Message, Boolean> cancelTimer = new HashMap<>();

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        AudioPlayer player = PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).player;
        //music controller
        EmbedBuilder musicController = new EmbedBuilder()
                .setAuthor("music", null, event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().blue)
                .addField("current playing track", PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).player.getPlayingTrack().getInfo().title, false)
                .setFooter(displayPosition(player));
        Message message = event.getChannel().sendMessage(musicController.build()).complete();
        //updating embed
        Timer update = new Timer();
        Timer cancel = new Timer();
        Timer removeHashMap = new Timer();
        // Add to HashMap
        cancelTimer.put(message, false);

        update.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                EmbedBuilder update = new EmbedBuilder();
                update.setAuthor("music", null, event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                //if no song is playing
                if (player.getPlayingTrack() == null) {
                    update.addField("\u23F9 │ player stopped", "no song playing", false);
                }
                //song is paused
                else if (player.isPaused()) {
                    update.addField("\u23F8 │ player paused", player.getPlayingTrack().getInfo().title, false);
                } else {
                    update
                            .setColor(Manager.getUtilities().blue)
                            .addField("\u25B6 │ current playing track", PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).player.getPlayingTrack().getInfo().title, false)
                            .setFooter(displayPosition(player));
                }

                message.editMessage(update.build()).queue();
            }
        }, 2 * 1000, 2 * 1000);
        //add reactions
        message.addReaction("\u23EF\uFE0F").queue();
        message.addReaction("\u23ED\uFE0F").queue();
        message.addReaction("\u23F9\uFE0F").queue();
        //add message id to HashMap
        MessageReaction.add("musicController", message.getId(), event.getChannel(), false);

        //cancel timer
        cancel.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).player.getPlayingTrack() == null) {
                    // Save boolean value
                    cancelTimer.put(message, true);

                    removeHashMap.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).player.getPlayingTrack() == null) {
                                MessageReaction.remove("musicController", message);
                                update.cancel();
                                cancel.cancel();
                                removeHashMap.cancel();
                                cancelTimer.remove(message);
                                System.out.println("removing command from hashmap");
                            }
                        }
                    }, 5 * 1000);
                }
                //cancel timers
                if (cancelTimer.get(message)) {
                    update.cancel();
                    cancel.cancel();
                    // Remove from HashMap
                    cancelTimer.remove(message);
                }
            }
        }, 5 * 1000, 5 * 1000);
    }

    //reactions
    @Override
    public void guildMessageReactionAddEvent(GuildMessageReactionAddEvent event) {
        //if reaction was added on the wrong message return
        if (MessageReaction.hashMap.get("musicController") == null) return;
        if (!Arrays.stream(MessageReaction.hashMap.get("musicController").toArray()).anyMatch(event.getMessageId()::equals) || event.getUser().isBot())
            return;

        AudioPlayer player = PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).player;
        TrackScheduler scheduler = PlayerManager.getInstance().getGuildMusicManger(event.getGuild()).scheduler;

        //play and pause
        if (event.getReactionEmote().getEmoji().equals("\u23EF\uFE0F") && !event.getMember().getUser().isBot()) {
            player.setPaused(!player.isPaused());
            event.getReaction().removeReaction(event.getMember().getUser()).queue();
        }
        //skip
        if (event.getReactionEmote().getEmoji().equals("\u23ED\uFE0F") && !event.getMember().getUser().isBot()) {
            scheduler.nextTrack();
            event.getReaction().removeReaction(event.getMember().getUser()).queue();
        }
        //stop
        if (event.getReactionEmote().getEmoji().equals("\u23F9\uFE0F") && !event.getMember().getUser().isBot()) {
            player.stopTrack();
            player.setPaused(false);
            event.getReaction().removeReaction(event.getMember().getUser()).queue();
        }
    }

    private String displayPosition(AudioPlayer player) {
        //split song duration in 15 parts
        Long sections = player.getPlayingTrack().getDuration() / 15;
        //get the part the song is in
        Long atSection = player.getPlayingTrack().getPosition() / sections;

        StringBuilder positionRaw = new StringBuilder("000000000000000")
                .insert(Math.toIntExact(atSection), '1');

        return positionRaw.toString().replaceAll("0", "▬").replace("1", "\uD83D\uDD18");
    }
}