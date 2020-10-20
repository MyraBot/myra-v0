package com.myra.dev.marian.utilities;

import com.myra.dev.marian.utilities.management.Events;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.*;

public class MessageReaction extends Events {

    public static HashMap<String, List<String>> hashMap = new HashMap();

    public static void add(String command, String messageId, TextChannel channel, Boolean timeOut) {
        //create new key
        if (hashMap.get(command) == null) {
            List<String> list = new ArrayList<>();
            list.add(messageId);
            hashMap.put(command, list);
        }
        //add message id
        else {
            hashMap.get(command).add(messageId);
        }
        //if timeOut is true
        if (timeOut) {
            //remove id
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //if a reaction was already added
                    if (!hashMap.get(command).contains(messageId)) return;

                    //remove from hashmap
                    hashMap.get(command).remove(messageId);
                    //remove all reactions
                    channel.retrieveMessageById(messageId).complete().clearReactions().queue();
                    System.out.println(hashMap);
                }
            }, 60 * 1000);
        }
    }

    public static void remove(String command, Message message) {
        hashMap.get(command).remove(message.getId());
        message.clearReactions().queue();
    }
    public static boolean check(GuildMessageReactionAddEvent event, String command) {
        // If reaction was added on the wrong message return
        if (MessageReaction.hashMap.get(command) == null) return false;
        if (!Arrays.stream(MessageReaction.hashMap.get(command).toArray()).anyMatch(event.getMessageId()::equals) || event.getUser().isBot())
            return false;
        // Remove id from hashmap
        MessageReaction.hashMap.get(command).remove(event.getMessageId());
        return true;
    }

    @Override
    public void jdaReady(ReadyEvent event) {
        hashMap.clear();
    }
}
