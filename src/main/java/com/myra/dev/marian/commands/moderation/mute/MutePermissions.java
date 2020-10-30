package com.myra.dev.marian.commands.moderation.mute;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.management.Events;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;

import java.util.EnumSet;

public class MutePermissions extends Events {

    public void textChannelCreateEvent(TextChannelCreateEvent event) {
        String id = new Database(event.getGuild()).get("muteRole");
        if (id.equals("not set")) return;
        Role muteRole = event.getGuild().getRoleById(id);
        if (muteRole == null) return;
        event.getChannel().getManager().putPermissionOverride(muteRole, null, EnumSet.of(Permission.MESSAGE_WRITE)).queue();
    }
}
