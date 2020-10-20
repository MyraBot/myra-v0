package com.myra.dev.marian.listeners.autorole;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "autorole toggle",
        aliases = {"auto role toggle", "joinrole toggle", "join role toggle", "defaultrole toggle", "default role toggle"}
)
public class AutoroleToggle implements Command {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        Database db = new Database(event.getGuild());
        //missing permissions
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) return;
        //toggle listener
        db.getListenerManager().toggle("autoRole", "\u2709\uFE0F", event);
    }
}
