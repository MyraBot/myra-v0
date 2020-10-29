package com.myra.dev.marian.commands.moderation.ban;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import com.myra.dev.marian.utilities.management.commands.CommandContext;

import java.time.Instant;

@CommandSubscribe(
        name = "ban",
        aliases = {"bean"}
)
public class Ban implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Missing permissions
        if (!Permissions.isModerator(ctx.getMember())) return;
        // Get Utilities
        Utilities utilities = Manager.getUtilities();
        //command usage
        if (ctx.getArguments().length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("ban", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + ctx.getPrefix() + "ban <user> <reason>`", "\uD83D\uDD12 │ Ban a specific member", false)
                    .setFooter("You don't have to give a reason.");
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * Ban user
         */
        // Get reason
        String reason = "";
        if (ctx.getArguments().length > 1) {
            for (int i = 1; i < ctx.getArguments().length; i++) {
                reason += ctx.getArguments()[i] + " ";
            }
            //remove last space
            reason = reason.substring(0, reason.length() - 1);
        }
        //get user
        User user = utilities.getUser(ctx.getEvent(), ctx.getArguments()[0], "ban", "\uD83D\uDD12");
        if (user == null) return;
        //if member isn't in the guild
        if (ctx.getGuild().getMember(user) == null) {
            utilities.error(ctx.getChannel(), "ban", "\uD83D\uDD12", "No user found", "The user you mentioned isn't on this server", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //guild message
        EmbedBuilder guildMessageBan = new EmbedBuilder()
                .setAuthor(user.getAsTag() + " got banned", null, user.getEffectiveAvatarUrl())
                .setColor(utilities.red)
                .setDescription("\uD83D\uDD12 │ " + user.getAsMention() + " got banned on `" + ctx.getGuild().getName() + "`")
                .setFooter("requested by " + ctx.getAuthor().getAsTag(), ctx.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //direct message
        EmbedBuilder directMessageBan = new EmbedBuilder()
                .setAuthor("You got banned", null, ctx.getGuild().getIconUrl())
                .setColor(utilities.red)
                .setDescription("\uD83D\uDD12 │ You got banned from `" + ctx.getGuild().getName() + "`")
                .setFooter("requested by " + ctx.getAuthor().getAsTag(), ctx.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //without reason
        if (ctx.getArguments().length == 1) {
            guildMessageBan.addField("\uD83D\uDCC4 │ no reason", "there was no reason given", false);
            directMessageBan.addField("\uD83D\uDCC4 │ no reason", "there was no reason given", false);
        }
        //with reason
        else {
            guildMessageBan.addField("\uD83D\uDCC4 │ reason:", reason, false);
            directMessageBan.addField("\uD83D\uDCC4 │ reason:", reason, false);
        }
        //send messages
        ctx.getChannel().sendMessage(guildMessageBan.build()).queue();
        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(directMessageBan.build()).queue();
        });
        //ban
        if (ctx.getArguments().length == 1) {
            ctx.getGuild().getMember(user).ban(7).queue();
        } else {
            ctx.getGuild().getMember(user).ban(7, reason).queue();
        }
    }
}