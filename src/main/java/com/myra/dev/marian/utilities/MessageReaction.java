package com.myra.dev.marian.utilities;


import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MessageReaction  {

    private static HashMap<String, HashMap<String, Document>> hashMap = new HashMap();

    public static void add(String command, String messageId, List<String> emojis, TextChannel channel, User user, Boolean timeOut) {
        //create new key
        if (hashMap.get(command) == null) {
            // Create HashMap
            HashMap<String, Document> map = new HashMap<>();
            // Create Document
            Document reaction = new Document()
                    .append("messageId", messageId)
                    .append("user", user.getId())
                    .append("emojis", emojis);
            // Put the message id and the author in the HashMap
            map.put(messageId, reaction);
            // Add the command to the HashMap
            hashMap.put(command, map);
        }
        // Add to existing command
        else {
            // Create Document
            Document reaction = new Document()
                    .append("messageId", messageId)
                    .append("user", user.getId())
                    .append("emojis", emojis);
            // Add the message id and the author to the hashmap
            hashMap.get(command).put(messageId, reaction);
        }
        // When the command has a time out
        if (timeOut) {
            //remove id
            Utilities.TIMER.schedule(() -> {
                // Reaction already removed
                if (hashMap.get(command).get(messageId) == null) return;
                // Remove all reactions
                channel.retrieveMessageById(messageId).complete().clearReactions().complete();
                // Remove message id from hashmap
                hashMap.get(command).remove(messageId);
            }, 1, TimeUnit.MINUTES);
        }
    }

    public static void remove(String command, Message message) {
        // Remove message id from HashMap
        hashMap.get(command).remove(message.getId());
        // Clear all reactions from the message
        message.clearReactions().queue();
    }

    public static boolean check(GuildMessageReactionAddEvent event, String command) {
        // When author is a bot
        if (event.getUser().isBot()) return false;
        // When command isn't in the hashMap yet
        if (hashMap.get(command) == null) return false;
        // Return if emoji is emote
        if (event.getReactionEmote().isEmote()) return false;
        // Check for the right message
        if (hashMap.get(command).containsKey(event.getMessageId())) {
            // Get Document
            Document reaction = hashMap.get(command).get(event.getMessageId());
            // Check for right author
            if (!reaction.getString("user").equals(event.getUserId())) return false;
            // Check for right emoji
            if (!reaction.getList("emojis", String.class).contains(event.getReactionEmote().getEmoji())) return false;
            // Delete message from Hashmap
            hashMap.get(command).remove(event.getMessageId());
            return true;
        } else return false;
    }
}
