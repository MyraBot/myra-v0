package com.myra.dev.marian.listeners.autorole;

import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Return;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
@CommandSubscribe(
        name = "autorole",
        aliases = {"auto role", "defaultrole", "default role", "joinrole", "join role"}
)
public class AutoRoleSet implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        Database db = new Database(event.getGuild());

        //split message
        String[] sentMessage = event.getMessage().getContentRaw().split("\\s+");
        //alias
        String[] arg1 = {Prefix.getPrefix(event.getGuild()) + "autorole", Prefix.getPrefix(event.getGuild()) + "defaultrole", Prefix.getPrefix(event.getGuild()) + "joinrole"};
        //check for arguments
        if (Arrays.stream(arg1).anyMatch(sentMessage[0]::equalsIgnoreCase)) {
            //missing permissions
            if (!Permissions.isAdministrator(event.getMember())) return;
            //command usage
            if (sentMessage.length == 1 || sentMessage.length > 2) {
                EmbedBuilder usage = new EmbedBuilder()
                        .setAuthor("│ auto role", null, event.getAuthor().getEffectiveAvatarUrl())
                        .setColor(Manager.getUtilities().gray)
                        .addField("`" + Prefix.getPrefix(event.getGuild()) + "autorole @role`", "\uD83D\uDCDD │ Give a new joined member automatic a certain role", true);
                event.getChannel().sendMessage(usage.build()).queue();
                return;
            }
            /**
             * autorole
             */
            if (new Return().role(event, sentMessage, 1, "autorole", "\uD83D\uDCDD") == null) return;
            Role role = new Return().role(event, sentMessage, 1, "autorole", "\uD83D\uDCDD");

            //remove autorole
            if (db.get("autoRole").equals(role.getId())) {
                //error
                Manager.getUtilities().success(event.getChannel(), "auto role", "\uD83D\uDCDD", "Removed auto role", "New members no longer get the " + event.getGuild().getRoleById(db.get("autoRole")).getAsMention() + " role", event.getAuthor().getEffectiveAvatarUrl(), false, false, null);
                //database
                db.set("autoRole", "not set");
                return;
            }
            //Database
            db.set("autoRole", role.getId());
            //success
            Manager.getUtilities().success(event.getChannel(),
                    "auto role", "\uD83D\uDCDD",
                    "Added auto role",
                    "New memebrs get now the " + role.getAsMention() + " role",
                    event.getAuthor().getEffectiveAvatarUrl(),
                    false, false, null);
        }
    }
}
