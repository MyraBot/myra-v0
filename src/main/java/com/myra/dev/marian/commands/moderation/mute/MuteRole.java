package com.myra.dev.marian.commands.moderation.mute;

import com.myra.dev.marian.database.allMethods.Database;

import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;

@CommandSubscribe(
        name = "mute role",
        aliases = {"muted role"},
        requires = Permissions.MODERATOR
)
public class MuteRole implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        //command usage
        if (ctx.getArguments().length != 1) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("mute role", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Utilities.getUtils().gray)
                    .addField("`" + ctx.getPrefix() + "mute role <role>`", "\uD83D\uDD07 │ Change the mute role", true);
            ctx.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        /**
         * Change mute role
         */
        // Get database
        Database db = new Database(ctx.getGuild());
        // Get utilities
        Utilities utilities = Utilities.getUtils();
        // Get role
        Role role = utilities.getRole(ctx.getEvent(), ctx.getArguments()[0], "mute role", "\uD83D\uDD07");
        if (role == null) return;
        //get mute role id
        String muteRoleId = db.getString("muteRole");
        //remove mute role
        if (role.getId().equals(muteRoleId)) {
            //success
            utilities.success(ctx.getChannel(), "mute role", "\uD83D\uDD07", "Removed mute role", "The mute role will no longer be " + ctx.getGuild().getRoleById(muteRoleId).getAsMention(), ctx.getAuthor().getEffectiveAvatarUrl(), false, null);
            //database
            db.set("muteRole", role.getId());
            return;
        }
        //change mute role
        db.set("muteRole", role.getId());
        //role changed
        utilities.success(ctx.getChannel(), "mute role", "\uD83D\uDCC4", "Muted role changed", "mute role set to " + role.getAsMention(), ctx.getAuthor().getEffectiveAvatarUrl(), false, null);
    }
}
