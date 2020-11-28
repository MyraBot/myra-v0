package com.myra.dev.marian;

import com.myra.dev.marian.APIs.Spotify;
import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;

@CommandSubscribe(name = "testing")
public class SpotifyTest implements Command {

    @Override
    public void execute(CommandContext ctx) throws Exception {
        new Spotify().authorizationToken();
        System.out.println(new Spotify().accessToken);
    }
}
