package com.myra.dev.marian.utilities;

import com.myra.dev.marian.utilities.management.Events;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MessageReaction extends Events {

    private static HashMap<String, HashMap<String, User>> hashMap = new HashMap();

    public static void add(String command, String messageId, TextChannel channel, User user, Boolean timeOut) {
        //create new key
        if (hashMap.get(command) == null) {
            // Create HashMap
            HashMap<String, User> map = new HashMap<>();
            // Put the message id and the author in the HashMap
            map.put(messageId, user);
            // Add the command to the HashMap
            hashMap.put(command, map);
        }
        // Add to existing command
        else {
            // Add the message id and the author to the hashmap
            hashMap.get(command).put(messageId, user);
        }
        // When the command has a time out
        if (timeOut) {
            //remove id
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // Can't find the command
                    if (!hashMap.get(command).containsKey(messageId)) return;
                    // Remove message id from hashmap
                    hashMap.get(command).remove(messageId);
                    //remove all reactions
                    channel.retrieveMessageById(messageId).complete().clearReactions().queue();
                }
            }, 60 * 1000);
        }
    }

    public static void remove(String command, Message message) {
        // Remove message id from HashMap
        hashMap.get(command).remove(message.getId());
        // Clear all reactions from the message
        message.clearReactions().queue();
    }

    public static boolean check(GuildMessageReactionAddEvent event, String command) {
        // When command isn't in the hashMap yet
        if (hashMap.get(command) == null) return false;
        // When author is a bot
        if (event.getUser().isBot()) return false;
        // Check for the right author
        if (hashMap.get(command).get(event.getMessageId()).equals(event.getUser())) return true;
        else return false;
    }
}
