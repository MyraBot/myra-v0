package com.myra.dev.marian.commands.moderation.ban;


import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;
import java.util.List;

@CommandSubscribe(
        name = "unban",
        aliases = {"unbean"},
        requires = Permissions.MODERATOR
)
public class Unban implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        //check for no arguments
        if (ctx.getArguments().length > 1) return;
        //command usage
        if (ctx.getArguments().length == 0) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("│ unban", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Utilities.getUtils().gray)
                    .addField("`" + ctx.getPrefix() + "unban <user>`", "\uD83D\uDD13 │ unban a specific member", false);
            ctx.getChannel().sendMessage(embed.build()).queue();
            return;
        }
// Unban
        //get utilities
        Utilities utilities = Utilities.getUtils();
        //get user
        User user = utilities.getModifiedMember(ctx.getEvent(), ctx.getArguments()[0], "unban", "\uD83D\uDD13");
        if (user == null) return;
        //user isn't banned
        if (ctx.getGuild().getMember(user) != null) {
            Utilities.getUtils().error(ctx.getChannel(),
                    "unban", "\uD83D\uDD13",
                    "User isn't banned",
                    "The mentioned user is already unbanned",
                    ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //retrieve bans
        List<net.dv8tion.jda.api.entities.Guild.Ban> banList = ctx.getGuild().retrieveBanList().complete();
        for (net.dv8tion.jda.api.entities.Guild.Ban ban : banList) {
            if (ban.getUser().equals(user)) {
                ctx.getGuild().unban(user).queue();
                //guild message
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor(user.getAsTag() + " got unbanned", null, user.getEffectiveAvatarUrl())
                        .setColor(Utilities.getUtils().blue)
                        .setDescription("\uD83D\uDD13 │ " + user.getAsMention() + " got unbanned from " + ctx.getGuild().getName())
                        .setFooter("requested by " + ctx.getAuthor().getAsTag(), ctx.getAuthor().getEffectiveAvatarUrl())
                        .setTimestamp(Instant.now());

                ctx.getChannel().sendMessage(embed.build()).queue();
                //direct message
                EmbedBuilder directMessage = new EmbedBuilder()
                        .setAuthor("You got unbanned", null, ctx.getGuild().getIconUrl())
                        .setColor(Utilities.getUtils().blue)
                        .setDescription("\uD83D\uDD13 │ You got unbanned from `" + ctx.getGuild().getName() + "`")
                        .setFooter("requested by " + ctx.getAuthor().getAsTag(), ctx.getAuthor().getEffectiveAvatarUrl())
                        .setTimestamp(Instant.now());
                user.openPrivateChannel().queue((channel) -> {
                    channel.sendMessage(directMessage.build()).queue();
                });
            }
        }
    }
}