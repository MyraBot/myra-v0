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

@CommandSubscribe(
        name = "ban",
        aliases = {"bean"}
)
public class Ban implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Missing permissions
        if (!Permissions.isModerator(event.getMember())) return;
        // Get Utilities
        Utilities utilities = Manager.getUtilities();
        //command usage
        if (arguments.length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("ban", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "ban <user> <reason>`", "\uD83D\uDD12 │ Ban a specific member", false)
                    .setFooter("You don't have to give a reason.");
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * Ban user
         */
        // Get reason
        String reason = "";
        if (arguments.length > 1) {
            for (int i = 1; i < arguments.length; i++) {
                reason += arguments[i] + " ";
            }
            //remove last space
            reason = reason.substring(0, reason.length() - 1);
        }
        //get user
        User user = utilities.getUser(event, arguments[0], "ban", "\uD83D\uDD12");
        if (user == null) return;
        //if member isn´t in the guild
        if (event.getGuild().getMember(user) == null) {
            utilities.error(event.getChannel(), "ban", "\uD83D\uDD12", "No user found", "The user you mentioned isn´t on this server", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //guild message
        EmbedBuilder guildMessageBan = new EmbedBuilder()
                .setAuthor(user.getAsTag() + " got banned", null, user.getEffectiveAvatarUrl())
                .setColor(utilities.red)
                .setDescription("\uD83D\uDD12 │ " + user.getAsMention() + " got banned on `" + event.getGuild().getName() + "`")
                .setFooter("requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //direct message
        EmbedBuilder directMessageBan = new EmbedBuilder()
                .setAuthor("You got banned", null, event.getGuild().getIconUrl())
                .setColor(utilities.red)
                .setDescription("\uD83D\uDD12 │ You got banned from `" + event.getGuild().getName() + "`")
                .setFooter("requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //without reason
        if (arguments.length == 1) {
            guildMessageBan.addField("\uD83D\uDCC4 │ no reason", "there was no reason given", false);
            directMessageBan.addField("\uD83D\uDCC4 │ no reason", "there was no reason given", false);
        }
        //with reason
        else {
            guildMessageBan.addField("\uD83D\uDCC4 │ reason:", reason, false);
            directMessageBan.addField("\uD83D\uDCC4 │ reason:", reason, false);
        }
        //send messages
        event.getChannel().sendMessage(guildMessageBan.build()).queue();
        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(directMessageBan.build()).queue();
        });
        //ban
        if (arguments.length == 1) {
            event.getGuild().getMember(user).ban(7).queue();
        } else {
            event.getGuild().getMember(user).ban(7, reason).queue();
        }
    }
}