package com.myra.dev.marian.listeners.welcome.welcomeDirectMessage;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;

@CommandSubscribe(
        name = "welcome direct message toggle",
        aliases = {"welcome dm toggle"}
)
public class WelcomeDirectMessageToggle implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Missing permissions
        if (!Permissions.isAdministrator(ctx.getMember())) return;
        //toggle feature
        new Database(ctx.getGuild()).getListenerManager().toggle("welcomeDirectMessage", "\u2709\uFE0F", ctx.getEvent());
    }
}
