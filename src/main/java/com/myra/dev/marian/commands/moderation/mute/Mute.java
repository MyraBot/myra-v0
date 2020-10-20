package com.myra.dev.marian.commands.moderation.mute;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Return;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
@CommandSubscribe(
        name = "mute"
)
public class Mute implements Command {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //missing permissions
        if (!Permissions.isModerator(event.getMember())) return;
        //split message
        String[] sentMessage = event.getMessage().getContentRaw().split("\\s+", 3);
        //command usage
        if (sentMessage.length == 1) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("│ mute", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "mute <user> <reason>`", "\uD83D\uDD07 │ mute a specific user", false)
                    .setFooter("you don´t have to give a reason.");
            event.getChannel().sendMessage(usage.build()).queue();
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
            user = new Return().userModified(event, sentMessage, "mute", "\uD83D\uDD08", 1);
            //get mute role id
            muteRoleId = new Database(event.getGuild()).get("muteRole");
            //no mute role set
            if (muteRoleId.equals("not set")) {
                Manager.getUtilities().error(event.getChannel(), "mute", "\uD83D\uDD07 ", "You didn't specify a mute role", "To indicate a mute role, type in `" + Prefix.getPrefix(event.getGuild()) + "mute role <role>`", event.getAuthor().getEffectiveAvatarUrl());
                return;
            }
            //user is already muted
            if (event.getGuild().getMember(user).getRoles().contains(event.getGuild().getRoleById(muteRoleId))) {
                Manager.getUtilities().error(event.getChannel(), "mute", "\uD83D\uDD07", "This user is already muted", "Use `" + Prefix.getPrefix(event.getGuild()) + "unmute <user>` to unmute a user", event.getAuthor().getEffectiveAvatarUrl());
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
                .setDescription("\uD83D\uDD07 │ " + user.getAsMention() + " got muted on " + event.getGuild().getName())
                .setFooter("requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //direct message
        EmbedBuilder directMessage = new EmbedBuilder()
                .setAuthor("You got muted", null, user.getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().red)
                .setDescription("\uD83D\uDD07 │ You got muted on " + event.getGuild().getName())
                .setFooter("requested by " + event.getMember().getUser().getName(), event.getMember().getUser().getEffectiveAvatarUrl())
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
        event.getChannel().sendMessage(guildMessage.build()).queue();
        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(directMessage.build()).queue();
        });
        //mute
        event.getGuild().addRoleToMember(event.getGuild().getMember(user), event.getGuild().getRoleById(muteRoleId)).queue();
    }
}
