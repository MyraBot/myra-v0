package com.myra.dev.marian.marian;


import com.myra.dev.marian.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;

public class ServerTracking {

    public void guildJoinEvent(GuildJoinEvent event) {
        EmbedBuilder server = new EmbedBuilder()
                .setTitle("I joined " + event.getGuild().getName())
                .setThumbnail(event.getGuild().getIconUrl())
                .addField("\uD83D\uDC51 │ owner ", event.getGuild().getOwner().getUser().getName(), true)
                .addField("\uD83C\uDF9F │ guild id ", event.getGuild().getId(), true)
                .addField("\uD83E\uDDEE │ member count", Integer.toString(event.getGuild().getMemberCount()), true)
                .setTimestamp(event.getGuild().getMember(event.getJDA().getSelfUser()).getTimeJoined().toInstant());

        event.getJDA().getGuildById(Bot.marianServer).getTextChannelById("788448343927160852").sendMessage(server.build()).queue();
    }
}
