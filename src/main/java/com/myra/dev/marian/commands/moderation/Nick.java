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
        name = "nick",
        aliases = {"nickname", "change nickname"}
)
public class Nick implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //missing permissions
        if (!Permissions.isModerator(event.getMember())) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        //command usage
        if (arguments.length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("nick", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "nick @user <nickname>`", "\uD83D\uDD75 │ Change a users nickname", true);
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * Change nickname
         */
        //get user
        User user = utilities.getModifiedUser(event, arguments[0], "nick", "\uD83D\uDD75");
        if (user == null) return;
        // Get nickname
        String nickname = "";
        for (int i = 1; i < arguments.length; i++) {
            nickname += arguments[i] + " ";
        }
        //remove last space
        nickname = nickname.substring(0, nickname.length() - 1);
        //success
        EmbedBuilder success = new EmbedBuilder()
                .setAuthor("nickname changed", null, user.getEffectiveAvatarUrl())
                .setColor(utilities.green)
                .addField("\uD83D\uDCC4 │ nickname changed of " + user.getName(), "`" + event.getGuild().getMember(user).getEffectiveName() + "` **→** `" + nickname + "`", true)
                .setFooter("requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        event.getChannel().sendMessage(success.build()).queue();
        event.getGuild().getMember(user).modifyNickname(nickname).queue();
    }
}
