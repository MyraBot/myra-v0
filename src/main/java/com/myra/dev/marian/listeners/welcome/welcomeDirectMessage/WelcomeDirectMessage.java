package com.myra.dev.marian.listeners.welcome.welcomeDirectMessage;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.management.Events;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class WelcomeDirectMessage extends Events {
    @Override
    public void memberJoined(GuildMemberJoinEvent event) throws Exception {
        Database db = new Database(event.getGuild());
        //check if feature is disabled
        if (!db.getListenerManager().check("welcomeDirectMessage")) return;
        // Get channel
        PrivateChannel channel = event.getUser().openPrivateChannel().complete();
        // Send message
        new WelcomeDirectMessageRender().embed(event.getGuild(), channel, null, event.getUser());
    }
}