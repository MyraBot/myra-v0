package com.myra.dev.marian.listeners.notification;

import com.myra.dev.marian.APIs.Twitch;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Notification extends Events {

    public void jdaReady(ReadyEvent event) throws Exception {
        //run loop
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    for (Guild guild : event.getJDA().getGuilds()) {
                        //connect to database
                        Database db = new Database(guild);
                        //get variables
                        List<String> streamers = db.getNotificationManager().getStreamers();
                        String channelRaw = db.getNotificationManager().getChannel();
                        //if no streamers are set
                        if (streamers.isEmpty()) continue;
                        //if no notification channel is set
                        if (channelRaw.equals("not set")) {
                            Manager.getUtilities().error(guild.getDefaultChannel(), "notification", "\uD83D\uDD14", "No notification channel specified", "To set a notification channel type in `" + Prefix.getPrefix(guild) + "notification channel <channel>`", guild.getIconUrl());
                            continue;
                        }
                        //get notification channel
                        TextChannel channel = guild.getTextChannelById(channelRaw);
                        //send message for each streamer
                        for (String streamer : streamers) {
                            //get streamer embed
                            EmbedBuilder streamerEmbed = Twitch.twitchRequest(streamer, channel, guild, guild.getIconUrl());
                            //if stream is offline
                            if (streamerEmbed == null) continue;
                            //if message is already sent
                            boolean nextStreamer = false;
                            for (Message message : channel.getHistory().retrievePast(25).complete()) {
                                List<MessageEmbed> embeds = message.getEmbeds();
                                if (embeds.isEmpty() || embeds.get(0).getFooter() == null) continue;
                                if (embeds.get(0).getFooter().getText().equals(streamerEmbed.build().getFooter().getText())) {
                                    nextStreamer = true;
                                }
                            }
                            if (nextStreamer) continue;
                            //send message
                            channel.sendMessage(streamerEmbed.build()).queue();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 5 * 60 * 1000, 5 * 60 * 1000);
    }
}
