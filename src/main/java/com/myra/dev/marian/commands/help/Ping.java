package com.myra.dev.marian.commands.help;

import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "ping",
        aliases = {"latency"}
)
public class Ping implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        if (!event.getAuthor().isBot()) {
            EmbedBuilder ping = new EmbedBuilder()
                    .setAuthor("pong", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().blue)
                    .addField("\uD83C\uDFD3 │ latency", "My ping is `" + event.getJDA().getGatewayPing() + "` ms", true);
            event.getChannel().sendMessage(ping.build()).queue();
        }
    }
}
