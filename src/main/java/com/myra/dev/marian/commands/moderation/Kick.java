package com.myra.dev.marian.commands.moderation;

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
        name = "kick",
        aliases = {"kek"}
)
public class Kick implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //missing permissions
        if (!Permissions.isModerator(event.getMember())) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        //command usage
        if (arguments.length == 0) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("kick", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "kick <user> <reason>`", "\uD83D\uDCE4 │ kick a specific member", false);
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        /**
         * Kick user
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
        User user = utilities.getModifiedUser(event, arguments[0], "kick", "\uD83D\uDCE4");
        if (user == null) return;
        //guild message
        EmbedBuilder guildMessage = new EmbedBuilder()
                .setAuthor(user.getAsTag() + " got kicked", null, user.getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().red)
                .setDescription(user.getAsMention() + " got kicked from " + event.getGuild().getName())
                .setFooter("requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //direct message
        EmbedBuilder directMessage = new EmbedBuilder()
                .setAuthor("You got kicked from " + event.getGuild().getName(), null, event.getGuild().getIconUrl())
                .setColor(Manager.getUtilities().red)
                .setDescription("You got kicked from " + event.getGuild().getName())
                .setFooter("requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //kick with no reason
        if (arguments.length == 1) {
            guildMessage.addField("\uD83D\uDCC4 │ no reason", "there was no reason given", false);
            directMessage.addField("\uD83D\uDCC4 │ no reason", "there was no reason given", false);
        }
        //kick with reason
        else {
            guildMessage.addField("\uD83D\uDCC4 │ reason:", reason, false);
            directMessage.addField("\uD83D\uDCC4 │ reason:", reason, false);
        }
        //send messages
        event.getChannel().sendMessage(guildMessage.build()).queue();
        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(directMessage.build()).queue();
        });
        //kick with no reason
        if (arguments.length == 1) {
            event.getGuild().getMember(user).kick().queue();
        }
        //kick with reason
        else {
            event.getGuild().getMember(user).kick(reason).queue();
        }
    }
}
