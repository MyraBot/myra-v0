package com.myra.dev.marian.commands.moderation.mute;

import com.myra.dev.marian.database.allMethods.Database;

import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;

@CommandSubscribe(
        name = "unmute",
        requires = "moderator"
)
public class Unmute implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        //command usage
        if (ctx.getArguments().length != 1) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("unmute", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + ctx.getPrefix() + "unmute <user>`", "\uD83D\uDD08 │ Unmute a specific user", false);
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
// Unmute
        //get user
        User user = utilities.getModifiedUser(ctx.getEvent(), ctx.getArguments()[0], "Unmute", "\uD83D\uDD08");
        if (user == null) return;
        //get mute role id
        String muteRoleId = new Database(ctx.getGuild()).get("muteRole");
        //no mute role set
        if (muteRoleId.equals("not set")) {
            Manager.getUtilities().error(ctx.getChannel(), "unmute", "\uD83D\uDD08", "You didn't specify a mute role", "To indicate a mute role, type in `" + ctx.getPrefix() + "mute role <role>`", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // User is already muted
        if (!ctx.getGuild().getMember(user).getRoles().contains(ctx.getGuild().getRoleById(muteRoleId))) {
            Manager.getUtilities().error(ctx.getChannel(), "unmute", "\uD83D\uDD08", "This user isn't muted", "Use `" + ctx.getPrefix() + "mute <user>` to mute a user", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //guild message
        EmbedBuilder guildMessage = new EmbedBuilder()
                .setColor(utilities.green)
                .setAuthor(user.getAsTag() + " got unmuted", null, user.getEffectiveAvatarUrl())
                .setDescription("\uD83D\uDD08 │ " + user.getAsMention() + " got unmuted on " + ctx.getGuild().getName())
                .setFooter("requested by " + ctx.getAuthor().getAsTag(), ctx.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        ctx.getChannel().sendMessage(guildMessage.build()).queue();
        //direct message
        EmbedBuilder directMessage = new EmbedBuilder()
                .setAuthor("You got unmuted", null, user.getEffectiveAvatarUrl())
                .setColor(utilities.green)
                .setDescription("\uD83D\uDD08 │ You got unmuted on " + ctx.getGuild().getName())
                .setFooter("requested by " + ctx.getAuthor().getAsTag(), ctx.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //send messages
        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(directMessage.build()).queue();
        });
        //unmute
        ctx.getGuild().removeRoleFromMember(user.getId(), ctx.getGuild().getRoleById(muteRoleId)).queue();
    }
}