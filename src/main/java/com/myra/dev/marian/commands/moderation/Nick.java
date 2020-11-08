package com.myra.dev.marian.commands.moderation;


import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;

@CommandSubscribe(
        name = "nick",
        aliases = {"nickname", "change nickname"},
        requires = "moderator"
)
public class Nick implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Get utilities
        Utilities utilities = Utilities.getUtils();
        //command usage
        if (ctx.getArguments().length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("nick", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + ctx.getPrefix() + "nick @user <nickname>`", "\uD83D\uDD75 │ Change a users nickname", true);
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
// Change nickname
        //get user
        User user = utilities.getModifiedUser(ctx.getEvent(), ctx.getArguments()[0], "nick", "\uD83D\uDD75");
        if (user == null) return;
        // Get nickname
        String nickname = "";
        for (int i = 1; i < ctx.getArguments().length; i++) {
            nickname += ctx.getArguments()[i] + " ";
        }
        //remove last space
        nickname = nickname.substring(0, nickname.length() - 1);
        //success
        EmbedBuilder success = new EmbedBuilder()
                .setAuthor("nickname changed", null, user.getEffectiveAvatarUrl())
                .setColor(utilities.green)
                .addField("\uD83D\uDCC4 │ nickname changed of " + user.getName(), "`" + ctx.getGuild().getMember(user).getEffectiveName() + "` **→** `" + nickname + "`", true)
                .setFooter("requested by " + ctx.getAuthor().getAsTag(), ctx.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        ctx.getChannel().sendMessage(success.build()).queue();
        ctx.getGuild().getMember(user).modifyNickname(nickname).queue();
    }
}
