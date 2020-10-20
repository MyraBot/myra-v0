package com.myra.dev.marian.commands.moderation.mute;

import com.myra.dev.marian.Main;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "mute role",
        aliases = {"muted role"}
)
public class MuteRole implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //missing permissions
        if (!Permissions.isModerator(event.getMember())) return;
        //command usage
        if (arguments.length != 1) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("│ mute role", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Main.prefix + "mute role <role>`", "\uD83D\uDD07 │ Change the mute role", true);
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        /**
         * change mute role
         */
        Database db = new Database(event.getGuild());
        Utilities utilities = Manager.getUtilities();

        Role role = utilities.getRole(event, arguments[0], "mute role", "\uD83D\uDD07");
        if (role == null) return;
        //get mute role id
        String muteRoleId = db.get("muteRole");
        //remove mute role
        if (role.getId().equals(muteRoleId)) {
            //success
            utilities.success(event.getChannel(), "mute role", "\uD83D\uDD07", "Removed mute role", "The mute role will no longer be " + event.getGuild().getRoleById(muteRoleId).getAsMention(), event.getAuthor().getEffectiveAvatarUrl(), false, false, null);
            //database
            db.set("muteRole", role.getId());
            return;
        }
        //change mute role
        db.set("muteRole", role.getId());
        //role changed
        utilities.success(event.getChannel(), "mute role", "\uD83D\uDCC4", "muted role successfuly changed", "mute role set to " + role.getAsMention(), event.getAuthor().getEffectiveAvatarUrl(), false, false, null);
    }
}
