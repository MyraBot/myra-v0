package com.myra.dev.marian.listeners.leveling;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.management.listeners.Listener;
import com.myra.dev.marian.management.listeners.ListenerSubscribe;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Duration;
import java.util.HashMap;

@ListenerSubscribe(
        name = "leveling"
)
public class LevelingListener implements Listener {
    private final static Leveling LEVELING = new Leveling();

    @Override
    public void execute(GuildMessageReceivedEvent event) throws Exception {
        // Return if message is a command
        if (event.getMessage().getContentRaw().startsWith(new Database(event.getGuild()).get("prefix"))) return;
        if (!cooldown(event)) return;
        //check if member is bot
        if (event.getAuthor().isBot()) return;
        //check for new level
        LEVELING.levelUp(event);
        //save new xp
        new Database(
                event.getGuild()).getMembers().getMember(event.getMember()).addXp(
                LEVELING.xp(event.getMessage())
        );
    }

    private static HashMap<Guild, HashMap<Member, Message>> cooldown = new HashMap<Guild, HashMap<Member, Message>>();

    private boolean cooldown(GuildMessageReceivedEvent event) {
        boolean returnedValue = true;
        //check if guild isn't in the HashMap yet
        if (cooldown.get(event.getGuild()) == null) {
            //crate new Map for member
            HashMap<Member, Message> memberMap = new HashMap<>();
            memberMap.put(event.getMember(), event.getMessage());
            //put guild in the 'cooldown' Map
            cooldown.put(event.getGuild(), memberMap);
        }
        //check if user isn't in the HashMap yet
        else if (cooldown.get(event.getGuild()).get(event.getMember()) == null) {
            cooldown.get(event.getGuild()).put(event.getMember(), event.getMessage());
        }
        //check if 5 minutes passed
        else {
            if (Duration.between(event.getMessage().getTimeCreated(), cooldown.get(event.getGuild()).get(event.getMember()).getTimeCreated()).toMinutes() < 1) {
                cooldown.get(event.getGuild()).replace(event.getMember(), event.getMessage());
                returnedValue = false;
            }
        }
        return returnedValue;
    }
}
