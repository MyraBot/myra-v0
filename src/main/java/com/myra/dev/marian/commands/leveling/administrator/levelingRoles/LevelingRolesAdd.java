package com.myra.dev.marian.commands.leveling.administrator.levelingRoles;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "leveling roles add",
        aliases = {"leveling role add"}
)
public class LevelingRolesAdd implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Usage
        if (arguments.length == 0 || arguments.length > 3) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("leveling roles add", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "leveling roles add <level> <role> [remove]`", "\uD83D\uDD17 │ Link a role to a level", false);
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * Add new role
         */
        // If level is not a digit
        if (!arguments[0].matches("\\d+")) {
            utilities.error(event.getChannel(), "leveling roles add", "\uD83C\uDFC5", "Invalid level", "You can only use digits", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // Get role to add
        Role roleToAdd = utilities.getRole(event, arguments[1], "leveling roles add", "\uD83C\uDFC5");
        if (roleToAdd == null) return;
        // If role to remove is given
        Role roleToRemove = null;
        if (arguments.length == 3) {
            // Get role
            roleToRemove = utilities.getRole(event, arguments[2], "leveling roles add", "\uD83C\uDFC5");
            if (roleToRemove == null) return;
        }
        // Get database
        Database db = new Database(event.getGuild());
        // If no role to remove is set
        if (roleToRemove == null) {
            // Update database
            db.getLeveling().addLevelingRole(Integer.parseInt(arguments[0]), roleToAdd.getId(), "not set");
        } else {
            // Update database
            db.getLeveling().addLevelingRole(Integer.parseInt(arguments[0]), roleToAdd.getId(), roleToRemove.getId());
        }
        // Update every member
        for (Member member : event.getGuild().getMembers()) {
            // Leave bots out
            if (member.getUser().isBot()) continue;
            // If members level is at least the level of the leveling roles
            if (db.getMembers().getMember(member).getLevel() >= Integer.parseInt(arguments[0])) {
                // Add role
                event.getGuild().addRoleToMember(member, roleToAdd).queue();
                // Check if role to remove is not null
                if (roleToRemove != null) {
                    // Remove role from member
                    event.getGuild().removeRoleFromMember(member, roleToRemove).queue();
                }
            }
        }
        // Success message
        // If role to remove is given
        if (arguments.length == 3) {
            utilities.success(event.getChannel(), "leveling roles add", "\uD83C\uDFC5", "Added leveling role", roleToAdd.getAsMention() + " is now linked up tp level `" + arguments[0] + "` and I will remove " + roleToRemove.getAsMention(), event.getAuthor().getEffectiveAvatarUrl(), false, null);
            return;
        }
        // If role to remove isn't givem
        utilities.success(event.getChannel(), "leveling roles add", "\uD83C\uDFC5", "Added leveling role", roleToAdd.getAsMention() + " is now linked up to level `" + arguments[0] + "`", event.getAuthor().getEffectiveAvatarUrl(), false, null);
    }
}
