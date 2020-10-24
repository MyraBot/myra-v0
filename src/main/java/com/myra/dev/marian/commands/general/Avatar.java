package com.myra.dev.marian.commands.general;

import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "avatar",
        aliases = {"av", "profile picture", "pp", "profile image"}
)
public class Avatar implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        //get user
        User user = event.getAuthor();
        if (arguments.length != 0) {
            user = utilities.getUser(event, arguments[0], "avatar", "\uD83D\uDDBC");
            if (user == null) return;
        }
        //avatar
        EmbedBuilder avatar = new EmbedBuilder()
                .setAuthor(user.getName() + "'s avatar:", user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl());
        if (event.getGuild().getMember(user) != null) {
            avatar.setColor(utilities.getMemberRoleColour(event.getGuild().getMember(user)));
        }
        avatar.setImage(user.getEffectiveAvatarUrl());

        event.getChannel().sendMessage(avatar.build()).queue();
    }
}