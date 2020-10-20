package com.myra.dev.marian.listeners.autorole;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class AutoroleAssign extends Events {

    public void onGuildMemberJoin(GuildMemberJoinEvent event) throws Exception {
        Database db = new Database(event.getGuild());
        //if feature is disabled
        if (!db.getListenerManager().check("autoRole")) return;
        //get role
        Role autoRole = event.getGuild().getRoleById(db.get("autoRole"));
        // Check if no role is set
        if (autoRole.equals("not set")) {
            Manager.getUtilities().error(event.getGuild().getDefaultChannel(), "autorole", "\uD83D\uDCDD", "You didn't specify a autorole", "To indicate a autorole, type in `" + Prefix.getPrefix(event.getGuild()) + "autorole <role>`", event.getGuild().getIconUrl());
        }
        //assign role
        event.getGuild().addRoleToMember(event.getMember(), autoRole).queue();
    }
}
