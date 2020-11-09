package com.myra.dev.marian.marian;

import com.myra.dev.marian.management.Events;
import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class UnicornRole extends Events {

    public void jdaReady(ReadyEvent event) throws Exception {
        // Get unicorn role
        Role unicornRole = event.getJDA().getGuildById("642809436515074053").getRoleById("774210055259947008");
        // Change colour every minute
        Utilities.TIMER.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // Generate a random colour
                Color colour = new Color((int) (Math.random() * 0x1000000));
                unicornRole.getManager().setColor(colour).queue();
            }
        }, 5, 5, TimeUnit.MINUTES);
    }
}