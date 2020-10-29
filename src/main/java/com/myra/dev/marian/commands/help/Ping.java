package com.myra.dev.marian.commands.help;

import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import com.myra.dev.marian.utilities.management.commands.CommandContext;

@CommandSubscribe(
        name = "ping",
        aliases = {"latency"}
)
public class Ping implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        if (!ctx.getAuthor().isBot()) {
            EmbedBuilder ping = new EmbedBuilder()
                    .setAuthor("pong", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().blue)
                    .addField("\uD83C\uDFD3 │ latency", "My ping is `" + ctx.getEvent().getJDA().getGatewayPing() + "` ms", true);
            ctx.getChannel().sendMessage(ping.build()).queue();
        }
    }
}
