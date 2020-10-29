package com.myra.dev.marian.commands.leveling.administrator.levelingRoles;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.database.documents.LevelingRolesDocument;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@CommandSubscribe(
        name = "leveling roles list",
        aliases = {"leveling role list"}
)
public class LevelingRolesList implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Missing permissions
        if (!Permissions.isAdministrator(ctx.getMember())) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Get leveling roles
        List<LevelingRolesDocument> rolesList = new Database(ctx.getGuild()).getLeveling().getLevelingRoles();
        // Sort roles
        Collections.sort(rolesList, Comparator.comparing(LevelingRolesDocument::getLevel).reversed());
        // Add all roles to String
        String roles = "";

        // If list is empty
        if (rolesList.isEmpty()) {
            roles = "none";
        }
        // Else add all leveling roles to the String
        else {
            for (LevelingRolesDocument role : rolesList) {
                // When there is a role to remove
                if (!role.getRemove().equals("not set")) {
                    roles += "• level: `" + role.getLevel() + "` add: " + ctx.getGuild().getRoleById(role.getRole()).getAsMention() + " remove:" + ctx.getGuild().getRoleById(role.getRole()).getAsMention();
                    continue;
                }
                // When there is only a role to add
                roles += "• level: `" + role.getLevel() + "` add: " + ctx.getGuild().getRoleById(role.getRole()).getAsMention();
            }
        }
        // Create embed
        EmbedBuilder levelingRoles = new EmbedBuilder()
                .setAuthor("leveling roles list")
                .setColor(utilities.blue)
                .setDescription(roles);
        ctx.getChannel().sendMessage(levelingRoles.build()).queue();
    }
}
