package com.myra.dev.marian.commands.moderation.ban;

import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;
import java.util.List;

@CommandSubscribe(
        name = "unban",
        aliases = {"unbean"}
)
public class Unban implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        //check for no arguments
        if (ctx.getArguments().length > 1) return;
        //missing permissions
        if (!Permissions.isModerator(ctx.getMember())) return;
        //command usage
        if (ctx.getArguments().length == 0) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("│ unban", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + ctx.getPrefix() + "unban <user>`", "\uD83D\uDD13 │ unban a specific member", false);
            ctx.getChannel().sendMessage(embed.build()).queue();
            return;
        }
// Unban
        //get utilities
        Utilities utilities = Manager.getUtilities();
        //get user
        User user = utilities.getUser(ctx.getEvent(), ctx.getArguments()[0], "unban", "\uD83D\uDD13");
        if (user == null) return;
        //user isn't banned
        if (ctx.getGuild().getMember(user) != null) {
            Manager.getUtilities().error(ctx.getChannel(),
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
                        .setColor(Manager.getUtilities().blue)
                        .setDescription("\uD83D\uDD13 │ " + user.getAsMention() + " got unbanned from " + ctx.getGuild().getName())
                        .setFooter("requested by " + ctx.getAuthor().getAsTag(), ctx.getAuthor().getEffectiveAvatarUrl())
                        .setTimestamp(Instant.now());

                ctx.getChannel().sendMessage(embed.build()).queue();
                //direct message
                EmbedBuilder directMessage = new EmbedBuilder()
                        .setAuthor("You got unbanned", null, ctx.getGuild().getIconUrl())
                        .setColor(Manager.getUtilities().blue)
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