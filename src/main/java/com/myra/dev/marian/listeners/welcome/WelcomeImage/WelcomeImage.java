package com.myra.dev.marian.listeners.welcome.WelcomeImage;


import com.myra.dev.marian.database.allMethods.Database;

import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class WelcomeImage  {

    public void memberJoined(GuildMemberJoinEvent event) throws Exception {
        Database db = new Database(event.getGuild());

        //check if feature is disabled
        if (!db.getListenerManager().check("welcomeImage")) return;
        //if no welcome channel is set
        if (db.getNested("welcome").get("welcomeChannel").equals("not set")) {
            Utilities.getUtils().error(event.getGuild().getDefaultChannel(), "welcome image", "\uD83D\uDDBC", "No welcome channel specified", "To set a welcome channel type in `" + new Database(event.getGuild()).get("prefix") + "welcome channel <channel>`", event.getGuild().getIconUrl());
            return;
        }
        TextChannel channel = event.getGuild().getTextChannelById(db.getNested("welcome").get("welcomeChannel").toString());
        //send welcome image
        WelcomeImageRender welcomeImage = new WelcomeImageRender();
        welcomeImage.welcomeImage(event.getGuild(), event.getUser(), channel);
    }
}
