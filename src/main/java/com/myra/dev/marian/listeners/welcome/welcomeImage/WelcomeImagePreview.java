package com.myra.dev.marian.listeners.welcome.welcomeImage;

import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "welcome image preview"
)
public class WelcomeImagePreview implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //missing permissions
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) return;
        //send image
        new WelcomeImageRender().welcomeImage(event.getGuild(), event.getAuthor(), event.getChannel());
    }
}
