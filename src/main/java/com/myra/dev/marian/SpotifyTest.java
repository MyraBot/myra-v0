package com.myra.dev.marian;

import com.myra.dev.marian.APIs.Spotify;
import com.myra.dev.marian.utilities.management.Events;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SpotifyTest extends Events {
    @Override
    public void guildMessageReceivedEvent(GuildMessageReceivedEvent event) throws Exception{
        new Spotify().authorizationToken();
        System.out.println(new Spotify().accessToken);
    }
}
