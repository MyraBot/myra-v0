package com.myra.dev.marian.listeners;


import com.myra.dev.marian.utilities.management.listeners.Listener;
import com.myra.dev.marian.utilities.management.listeners.ListenerSubscribe;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

@ListenerSubscribe(
        name = "@someone",
        needsExecutor = true,
        requires = "administrator"
)
public class Someone implements Listener {
    @Override
    public void execute(GuildMessageReceivedEvent event) throws Exception {
        //get random number
        Random random = new Random();
        int number = random.nextInt(event.getGuild().getMembers().size());
        //get random member
        String randomMember = event.getGuild().getMembers().get(number).getAsMention();

        String message = event.getMessage().getContentRaw().replace("@someone", randomMember);
        event.getChannel().deleteMessageById(event.getChannel().getLatestMessageIdLong()).queue();
        event.getChannel().sendMessage(message).queue();
    }
}
