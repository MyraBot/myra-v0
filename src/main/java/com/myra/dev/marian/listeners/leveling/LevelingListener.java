package com.myra.dev.marian.listeners.leveling;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.database.allMethods.GetMember;
import com.myra.dev.marian.management.listeners.Listener;
import com.myra.dev.marian.management.listeners.ListenerContext;
import com.myra.dev.marian.management.listeners.ListenerSubscribe;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.time.Duration;
import java.util.HashMap;

@ListenerSubscribe(
        name = "leveling"
)
public class LevelingListener implements Listener {
    private final Leveling LEVELING = new Leveling();

    @Override
    public void execute(ListenerContext ctx) throws Exception {
        if (ctx.getEvent().getAuthor().isBot()) return; // Check if member is a bot

        final GetMember db = new Database(ctx.getGuild()).getMembers().getMember(ctx.getEvent().getMember()); // Get member from database

        // Update message count
        final Integer messages = db.getInteger("messages"); // Get current messages
        db.setInteger("messages", messages + 1); // Add 1 message

        if (ctx.getMessage().getContentRaw().startsWith(new Database(ctx.getGuild()).getString("prefix"))) return; // Message is a command
        if (!cooldown(ctx)) return; // Cooldown

        LEVELING.levelUp(ctx, db); // Check for new level
        db.setInteger("xp", db.getInteger("xp") + LEVELING.getXpFromMessage(ctx.getMessage())); // Update xp
    }

    private static HashMap<Guild, HashMap<Member, Message>> cooldown = new HashMap<Guild, HashMap<Member, Message>>();

    private boolean cooldown(ListenerContext event) {
        boolean returnedValue = true;
        //check if guild isn't in the HashMap yet
        if (cooldown.get(event.getGuild()) == null) {
            //crate new Map for member
            HashMap<Member, Message> memberMap = new HashMap<>();
            memberMap.put(event.getMessage().getMember(), event.getMessage());
            //put guild in the 'cooldown' Map
            cooldown.put(event.getGuild(), memberMap);
        }
        //check if user isn't in the HashMap yet
        else if (cooldown.get(event.getGuild()).get(event.getMessage().getMember()) == null) {
            cooldown.get(event.getGuild()).put(event.getMessage().getMember(), event.getMessage());
        }
        //check if 1 minutes passed
        else {
            if (Duration.between(event.getMessage().getTimeCreated(), cooldown.get(event.getGuild()).get(event.getMessage().getMember()).getTimeCreated()).toMinutes() < 1) {
                cooldown.get(event.getGuild()).replace(event.getMessage().getMember(), event.getMessage());
                returnedValue = false;
            }
        }
        return returnedValue;
    }
}
