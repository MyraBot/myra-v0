package com.myra.dev.marian.listeners.welcome.welcomeEmbed;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import java.awt.*;
import java.time.Instant;

public class WelcomeEmbed extends Events {
    @Override
    public void memberJoined(GuildMemberJoinEvent event) throws Exception {
        Database db = new Database(event.getGuild());

        //check if feature is disabled
        if (!db.getListenerManager().check("welcomeEmbed")) return;
        //if no welcome channel is set
        if (db.getNested("welcome").get("welcomeChannel").equals("not set")) {
            Manager.getUtilities().error(event.getGuild().getDefaultChannel(), "welcome embed", "\uD83D\uDCC7", "No welcome channel specified", "To set a welcome channel type in `" + Prefix.getPrefix(event.getGuild()) + "welcome channel <channel>`", event.getGuild().getIconUrl());
            return;
        }
        //get channel
        TextChannel channel = event.getGuild().getTextChannelById(db.getNested("welcome").get("welcomeChannel"));
        //get variables
        String welcomeColour = db.getNested("welcome").get("welcomeColour");
        String welcomeEmbedMessage = db.getNested("welcome").get("welcomeEmbedMessage");
        //send message
        EmbedBuilder join = new EmbedBuilder()
                .setAuthor("welcome", null, event.getGuild().getIconUrl())
                .setColor(Color.decode(welcomeColour))
                .setThumbnail(event.getUser().getEffectiveAvatarUrl())
                .setDescription(welcomeEmbedMessage.replace("{user}", event.getMember().getAsMention()).replace("{server}", event.getGuild().getName()).replace("{count}", Integer.toString(event.getGuild().getMemberCount())))
                .setTimestamp(Instant.now());
        channel.sendMessage(join.build()).queue();
    }
}
