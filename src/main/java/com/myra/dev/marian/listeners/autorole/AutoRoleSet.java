package com.myra.dev.marian.listeners.autorole;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Return;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "autorole",
        aliases = {"auto role", "defaultrole", "default role", "joinrole", "join role"}
)
public class AutoRoleSet implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        //command usage
        if (arguments.length != 1) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("│ auto role", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "autorole @role`", "\uD83D\uDCDD │ Give a new joined member automatic a certain role", true);
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * autorole
         */
        // Get autorole
        Role role = utilities.getRole(event, arguments[0], "autorole", "\uD83D\uDCDD");
        if (role == null) return;
        // Get database
        Database db = new Database(event.getGuild());
        //remove autorole
        if (db.get("autoRole").equals(role.getId())) {
            //error
           utilities.success(event.getChannel(), "auto role", "\uD83D\uDCDD", "Removed auto role", "New members no longer get the " + event.getGuild().getRoleById(db.get("autoRole")).getAsMention() + " role", event.getAuthor().getEffectiveAvatarUrl(), false, false, null);
            //database
            db.set("autoRole", "not set");
            return;
        }
        //Database
        db.set("autoRole", role.getId());
        //success
       utilities.success(event.getChannel(),
                "auto role", "\uD83D\uDCDD",
                "Added auto role",
                "New members get now the " + role.getAsMention() + " role",
                event.getAuthor().getEffectiveAvatarUrl(),
                false, false, null);
    }
}
