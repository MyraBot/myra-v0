package com.myra.dev.marian.commands.fun;

import com.myra.dev.marian.APIs.Reddit;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "meme",
        aliases = {"memes"}
)
public class Meme implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        try {
            event.getChannel().sendMessage(new Reddit().getMeme(event.getAuthor()).build()).queue();
        } catch (Exception e) {
            Manager.getUtilities().error(event.getChannel(), "meme", "\uD83E\uDD2A", "Couldn't load meme", "Please try again later", event.getAuthor().getEffectiveAvatarUrl());
        }
    }
}