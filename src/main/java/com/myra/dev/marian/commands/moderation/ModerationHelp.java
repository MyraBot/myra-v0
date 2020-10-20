package com.myra.dev.marian.commands.moderation;

import com.myra.dev.marian.utilities.CommandEmbeds;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "moderation",
        aliases = {"mod"}
)
public class ModerationHelp implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Missing permissions
        if (!Permissions.isModerator(event.getMember())) return;
        // Send message
        event.getChannel().sendMessage(
                CommandEmbeds.moderation(
                        event.getGuild(),
                        event.getAuthor().getEffectiveAvatarUrl())
                        .build())
                .queue();
    }
}