package com.myra.dev.marian.listeners.suggestions;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.Permissions;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
@CommandSubscribe(
        name = "suggestions toggle"
)
public class SuggestionsToggle implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        //connect to database
        Database db = new Database(event.getGuild());
        //toggle feature
        db.getListenerManager().toggle("suggestions", "\uD83D\uDDF3", event);
    }
}
