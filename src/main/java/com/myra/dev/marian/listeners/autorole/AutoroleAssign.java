package com.myra.dev.marian.listeners.autorole;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.management.Events;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class AutoroleAssign extends Events {

    public void onGuildMemberJoin(GuildMemberJoinEvent event) throws Exception {
        Database db = new Database(event.getGuild());

        //if feature is disabled
        if (!db.getListenerManager().check("autoRole")) return;
        //get role
        Role autoRole = event.getGuild().getRoleById(db.get("autoRole"));
        //assign role
        event.getGuild().addRoleToMember(event.getMember(), autoRole).queue();
    }
}
