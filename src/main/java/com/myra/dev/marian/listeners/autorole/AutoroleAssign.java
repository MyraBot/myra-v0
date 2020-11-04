package com.myra.dev.marian.listeners.autorole;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class AutoroleAssign extends Events {

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        try {
            Database db = new Database(event.getGuild());
            //get role
            String autoRole = db.get("autoRole");
            // Check if no role is set
            if (autoRole.equals("not set")) {
                Manager.getUtilities().error(event.getGuild().getDefaultChannel(), "autorole", "\uD83D\uDCDD", "You didn't specify a autorole", "To indicate a autorole, type in `" + new Database(event.getGuild()).get("prefix") + "autorole <role>`", event.getGuild().getIconUrl());
                return;
            }
            //assign role
            event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(autoRole)).queue();
        } catch (Exception e) {
        }
    }
}
