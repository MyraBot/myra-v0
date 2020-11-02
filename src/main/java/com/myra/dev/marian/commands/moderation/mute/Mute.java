package com.myra.dev.marian.commands.moderation.mute;


import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Return;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;

@CommandSubscribe(
        name = "mute"
)
public class Mute implements Command {

    @Override
    public void execute(CommandContext ctx) throws Exception {
        //missing permissions
        if (!Permissions.isModerator(ctx.getMember())) return;
        //split message
        String[] sentMessage = ctx.getEvent().getMessage().getContentRaw().split("\\s+", 3);
        //command usage
        if (sentMessage.length == 1) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("mute", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + ctx.getPrefix() + "mute <user> <reason>`", "\uD83D\uDD07 │ mute a specific user", false)
                    .setFooter("you don't have to give a reason.");
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * mute
         */
        User user = null;
        String muteRoleId = null;
        try {
            //if I used the mute role command
            if (sentMessage[1].equalsIgnoreCase("role")) return;
            //get user
            user = new Return().userModified(ctx.getEvent(), sentMessage, "mute", "\uD83D\uDD08", 1);
            //get mute role id
            muteRoleId = new Database(ctx.getGuild()).get("muteRole");
            //no mute role set
            if (muteRoleId.equals("not set")) {
                Manager.getUtilities().error(ctx.getChannel(), "mute", "\uD83D\uDD07 ", "You didn't specify a mute role", "To indicate a mute role, type in `" + ctx.getPrefix() + "mute role <role>`", ctx.getAuthor().getEffectiveAvatarUrl());
                return;
            }
            //user is already muted
            if (ctx.getGuild().getMember(user).getRoles().contains(ctx.getGuild().getRoleById(muteRoleId))) {
                Manager.getUtilities().error(ctx.getChannel(), "mute", "\uD83D\uDD07", "This user is already muted", "Use `" + ctx.getPrefix() + "unmute <user>` to unmute a user", ctx.getAuthor().getEffectiveAvatarUrl());
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * mute
         */

        //guild message
        EmbedBuilder guildMessage = new EmbedBuilder()
                .setAuthor(user.getAsTag() + " got muted", null, user.getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().red)
                .setDescription("\uD83D\uDD07 │ " + user.getAsMention() + " got muted on " + ctx.getGuild().getName())
                .setFooter("requested by " + ctx.getAuthor().getAsTag(), ctx.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //direct message
        EmbedBuilder directMessage = new EmbedBuilder()
                .setAuthor("You got muted", null, user.getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().red)
                .setDescription("\uD83D\uDD07 │ You got muted on " + ctx.getGuild().getName())
                .setFooter("requested by " + ctx.getAuthor().getAsTag(), ctx.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //mute with no reason
        if (sentMessage.length == 2) {
            guildMessage.addField("\uD83D\uDCC4 │ no reason", "there was no reason given", false);
            directMessage.addField("\uD83D\uDCC4 │ no reason", "there was no reason given", false);
        }
        //mute with reason
        if (sentMessage.length > 2) {
            guildMessage.addField("\uD83D\uDCC4 │ reason:", sentMessage[2], false);
            directMessage.addField("\uD83D\uDCC4 │ reason:", sentMessage[2], false);
        }
        //send messages
        ctx.getChannel().sendMessage(guildMessage.build()).queue();
        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(directMessage.build()).queue();
        });
        //mute
        ctx.getGuild().addRoleToMember(ctx.getGuild().getMember(user), ctx.getGuild().getRoleById(muteRoleId)).queue();
    }
}
