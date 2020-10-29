package com.myra.dev.marian.commands.general;

import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import com.myra.dev.marian.utilities.management.commands.CommandContext;

@CommandSubscribe(
        name = "avatar",
        aliases = {"av", "profile picture", "pp", "profile image"}
)
public class Avatar implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        //get user
        User user = ctx.getAuthor();
        if (ctx.getArguments().length != 0) {
            user = utilities.getUser(ctx.getEvent(), ctx.getArguments()[0], "avatar", "\uD83D\uDDBC");
            if (user == null) return;
        }
        //avatar
        EmbedBuilder avatar = new EmbedBuilder()
                .setAuthor(user.getName() + "'s avatar:", user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl());
        if (ctx.getGuild().getMember(user) != null) {
            avatar.setColor(utilities.getMemberRoleColour(ctx.getGuild().getMember(user)));
        }
        avatar.setImage(user.getEffectiveAvatarUrl());

        ctx.getChannel().sendMessage(avatar.build()).queue();
    }
}