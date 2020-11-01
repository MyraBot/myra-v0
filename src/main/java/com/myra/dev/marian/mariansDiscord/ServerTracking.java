package com.myra.dev.marian.mariansDiscord;

import com.myra.dev.marian.utilities.management.Events;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServerTracking extends Events {

    public void guildJoinEvent(GuildJoinEvent event) {
        EmbedBuilder server = new EmbedBuilder();
        server.setTitle("I joined " + event.getGuild().getName());
        server.setThumbnail(event.getGuild().getIconUrl());
        server.addField("\uD83D\uDC51 │ owner ", event.getGuild().getOwner().getUser().getName(), true);
        server.addField("\uD83C\uDF9F │ guild id ", event.getGuild().getId(), true);
        server.addField("\uD83E\uDDEE │ member count", Integer.toString(event.getGuild().getMemberCount()), true);

        event.getJDA().getGuildById("642809436515074053").getTextChannelById("721004402915147847").sendMessage(server.build()).queue();
    }
}
