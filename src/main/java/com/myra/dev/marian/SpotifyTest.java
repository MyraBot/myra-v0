package com.myra.dev.marian;

import com.myra.dev.marian.APIs.Spotify;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(name = "testing")
public class SpotifyTest implements Command {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        new Spotify().authorizationToken();
        System.out.println(new Spotify().accessToken);
    }
}
