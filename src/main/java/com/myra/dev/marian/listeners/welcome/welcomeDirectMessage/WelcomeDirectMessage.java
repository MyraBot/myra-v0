package com.myra.dev.marian.listeners.welcome.welcomeDirectMessage;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.management.Events;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import java.awt.*;

public class WelcomeDirectMessage extends Events {
    @Override
    public void memberJoined(GuildMemberJoinEvent event) throws Exception {
        Database db = new Database(event.getGuild());
        //check if feature is disabled
        if (!db.getListenerManager().check("welcomeDirectMessage")) return;
        //get variables
        String welcomeColour = db.getNested("welcome").get("welcomeColour");
        String welcomeDirectMessage = db.getNested("welcome").get("welcomeDirectMessage");
        //send message
        EmbedBuilder join = new EmbedBuilder()
                .setAuthor("welcome", null, event.getGuild().getIconUrl())
                .setColor(Color.decode(welcomeColour))
                .setThumbnail(event.getUser().getEffectiveAvatarUrl())
                .setDescription(welcomeDirectMessage
                        .replace("{user}", event.getUser().getAsMention())
                        .replace("{server}", event.getGuild().getName())
                        .replace("{count}", Integer.toString(event.getGuild().getMemberCount())));
        event.getUser().openPrivateChannel().queue((channel) -> {
            channel.sendMessage(join.build()).queue();
        });
    }
}