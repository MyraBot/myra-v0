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
import org.bson.Document;

@CommandSubscribe(
        name = "leveling roles remove",
        aliases = {"leveling role remove"}
)
public class LevelingRolesRemove implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Usage
        if (arguments.length != 1) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("leveling roles remove", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "leveling roles remove <role>`", "\uD83D\uDDD1 │ Delete the linking between a level and a role", false);
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * Remove leveling role
         */
        // Get role
        Role role = utilities.getRole(event, arguments[0], "leveling roles remove", "\uD83C\uDFC5");
        if (role == null) return;
        // Get database
        Database db = new Database(event.getGuild());
        // Get role document
        Document roleDocument = db.getLeveling().getLevelingRoles(role.getId());
        // Remove role from database
        db.getLeveling().removeLevelingRole(role.getId());
        // Get role
        Role levelingRole = event.getGuild().getRoleById(roleDocument.getString("role"));
        // Update every member
        for (Member member : event.getGuild().getMembers()) {
            // Leave bots out
            if (member.getUser().isBot()) continue;
            // If members level is at least the level of the leveling roles
            if (db.getMembers().getMember(member).getLevel() >= roleDocument.getInteger("level")) {
                // Remove leveling role
                event.getGuild().removeRoleFromMember(member, levelingRole).queue();
            }
        }
        // Success message
        utilities.success(event.getChannel(), "leveling roles remove", "\uD83C\uDFC5", "Remove leveling role", role.getAsMention() + " is no longer linked up with level " + String.valueOf(roleDocument.getInteger("level")), event.getAuthor().getEffectiveAvatarUrl(), false, null);
    }
}
