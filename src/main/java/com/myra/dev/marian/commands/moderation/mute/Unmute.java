package com.myra.dev.marian.commands.moderation.mute;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
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
        name = "unmute"
)
public class Unmute implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //missing permissions
        if (!Permissions.isModerator(event.getMember())) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        //command usage
        if (arguments.length != 1) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("│ unmute", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "unmute <user>`", "\uD83D\uDD08 │ Unmute a specific user", false);
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * unmute
         */
        //get user
        User user = utilities.getModifiedUser(event, arguments[0], "Unmute", "\uD83D\uDD08");
        if (user == null) return;
        //get mute role id
        String muteRoleId = new Database(event.getGuild()).get("muteRole");
        //no mute role set
        if (muteRoleId.equals("not set")) {
            Manager.getUtilities().error(event.getChannel(), "unmute", "\uD83D\uDD08", "You didn't specify a mute role", "To indicate a mute role, type in `" + Prefix.getPrefix(event.getGuild()) + "mute role <role>`", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // User is already muted
        if (!event.getGuild().getMember(user).getRoles().contains(event.getGuild().getRoleById(muteRoleId))) {
            Manager.getUtilities().error(event.getChannel(), "unmute", "\uD83D\uDD08", "This user isn´t muted", "Use `" + Prefix.getPrefix(event.getGuild()) + "mute <user>` to mute a user", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //guild message
        EmbedBuilder guildMessage = new EmbedBuilder()
                .setColor(utilities.green)
                .setAuthor(user.getAsTag() + " got unmuted", null, user.getEffectiveAvatarUrl())
                .setDescription("\uD83D\uDD08 │ " + user.getAsMention() + " got unmuted on " + event.getGuild().getName())
                .setFooter("requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        event.getChannel().sendMessage(guildMessage.build()).queue();
        //direct message
        EmbedBuilder directMessage = new EmbedBuilder()
                .setAuthor("You got unmuted", null, user.getEffectiveAvatarUrl())
                .setColor(utilities.green)
                .setDescription("\uD83D\uDD08 │ You got unmuted on " + event.getGuild().getName())
                .setFooter("requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //send messages
        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(directMessage.build()).queue();
        });
        //unmute
        event.getGuild().removeRoleFromMember(user.getId(), event.getGuild().getRoleById(muteRoleId)).queue();
    }
}