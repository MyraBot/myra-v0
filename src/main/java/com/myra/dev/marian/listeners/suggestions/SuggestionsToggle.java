package com.myra.dev.marian.listeners.suggestions;

import com.myra.dev.marian.database.allMethods.Database;

import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;

@CommandSubscribe(
        name = "suggestions toggle",
        requires = Permissions.ADMINISTRATOR
)
public class SuggestionsToggle implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        //toggle feature
        new Database(ctx.getGuild()).getListenerManager().toggle("suggestions", "\uD83D\uDDF3", ctx.getEvent());
    }
}
