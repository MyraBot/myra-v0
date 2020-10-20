package com.myra.dev.marian.commands.moderation.ban;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.util.List;

@CommandSubscribe(
        name = "unban",
        aliases = {"unbean"}
)
public class Unban implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //check for no arguments
        if (arguments.length > 1) return;
        //missing permissions
        if (!Permissions.isModerator(event.getMember())) return;
        //command usage
        if (arguments.length == 0) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("│ unban", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "unban <user>`", "\uD83D\uDD13 │ unban a specific member", false);
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        /**
         * unban
         */
        //get utilities
        Utilities utilities = Manager.getUtilities();
        //get user
        User user = utilities.getUser(event, arguments[0], "unban", "\uD83D\uDD13");
        if (user == null) return;
        //user isn´t banned
        if (event.getGuild().getMember(user) != null) {
            Manager.getUtilities().error(event.getChannel(),
                    "unban", "\uD83D\uDD13",
                    "User isn´t banned",
                    "The mentioned user is already unbanned",
                    event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //retrieve bans
        List<net.dv8tion.jda.api.entities.Guild.Ban> banList = event.getGuild().retrieveBanList().complete();
        for (net.dv8tion.jda.api.entities.Guild.Ban ban : banList) {
            if (ban.getUser().equals(user)) {
                event.getGuild().unban(user).queue();
                //guild message
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor(user.getAsTag() + " got unbanned", null, user.getEffectiveAvatarUrl())
                        .setColor(Manager.getUtilities().blue)
                        .setDescription("\uD83D\uDD13 │ " + user.getAsMention() + " got unbanned from " + event.getGuild().getName())
                        .setFooter("requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl())
                        .setTimestamp(Instant.now());

                event.getChannel().sendMessage(embed.build()).queue();
                //direct message
                EmbedBuilder directMessage = new EmbedBuilder()
                        .setAuthor("You got unbanned", null, event.getGuild().getIconUrl())
                        .setColor(Manager.getUtilities().blue)
                        .setDescription("\uD83D\uDD13 │ You got unbanned from `" + event.getGuild().getName() + "`")
                        .setFooter("requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl())
                        .setTimestamp(Instant.now());
                user.openPrivateChannel().queue((channel) -> {
                    channel.sendMessage(directMessage.build()).queue();
                });
            }
        }
    }
}