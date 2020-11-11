package com.myra.dev.marian.marian;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;

public class ServerTracking  {

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
