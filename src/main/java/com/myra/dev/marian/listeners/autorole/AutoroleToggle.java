package com.myra.dev.marian.listeners.autorole;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.Permission;

@CommandSubscribe(
        name = "autorole toggle",
        aliases = {"auto role toggle", "joinrole toggle", "join role toggle", "defaultrole toggle", "default role toggle"}
)
public class AutoroleToggle implements Command {

    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Check for no arguments
        if (ctx.getArguments().length != 0) return;
        //missing permissions
        if (!ctx.getMember().hasPermission(Permission.ADMINISTRATOR)) return;
        // Get database
        Database db = new Database(ctx.getGuild());
        // Toggle listener
        db.getListenerManager().toggle("autoRole", "\u2709\uFE0F", ctx.getEvent());
    }
}
