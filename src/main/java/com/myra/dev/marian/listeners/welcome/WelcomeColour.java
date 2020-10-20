package com.myra.dev.marian.listeners.welcome;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
@CommandSubscribe(
        name = "welcome colour",
        aliases = {"welcome color"}
)
public class WelcomeColour implements Command {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        Database db = new Database(event.getGuild());

        //missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        //split
        String[] sentMessage = event.getMessage().getContentRaw().split("\\s+");
        //alias
        String[] args1 = {Prefix.getPrefix(event.getGuild()) + "welcome"};
        String[] args2 = {"colour"};

        if (Arrays.stream(args1).anyMatch(sentMessage[0]::equalsIgnoreCase) && Arrays.stream(args2).anyMatch(sentMessage[1]::equalsIgnoreCase) && sentMessage.length == 3) {
            String hex = null;
            //remove #
            if (sentMessage[2].startsWith("#")) {
                StringBuilder raw = new StringBuilder(sentMessage[2]);
                raw.deleteCharAt(0);
                hex = "0x" + raw.toString();
            }
            //add 0x
            else {
                hex = "0x" + sentMessage[2];
            }
            //if colour doesn't exist
            try {
                Color.decode(hex);
            } catch (Exception e) {
                Manager.getUtilities().error(event.getChannel(), "welcome embed colour", "\uD83C\uDFA8", "Invalid colour", "The given colour doesn't exist", event.getAuthor().getEffectiveAvatarUrl());
                return;
            }
            //save in database
            db.getNested("welcome").set("welcomeColour", hex);
            //success
            Manager.getUtilities().success(event.getChannel(), "welcome embed colour", "\uD83C\uDFA8", "Updated Colour", "Colour changed to `" + db.getNested("welcome").get("welcomeColour").replace("0x", "#") + "`", event.getAuthor().getEffectiveAvatarUrl(), false, false, null);
        }
    }
}