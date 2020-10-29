package com.myra.dev.marian.commands;

import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.management.Resources;
import net.dv8tion.jda.api.EmbedBuilder;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "dashboard",
        aliases = {"dash"}
)
public class Dashboard extends Events {
    //TODO
    /*
    public void onGuildMessageReceivedEvent(GuildMessageReceivedEvent event) throws Exception{
        Resources resources = new Resources();

        EmbedBuilder dashboard = new EmbedBuilder()
                .setAuthor("dashboard", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .addField("CPU usage", resources.getCpuLoad(), false)
                .addField("RAM usage", resources.getRAMUsage(), false)
                .addField("Shard",  String.valueOf(event.getJDA().getShardInfo().getShardId()), false)
                .addField("Guilds", String.valueOf(event.getJDA().getGuilds().size()), false);
        ctx.getChannel().sendMessage(dashboard.build()).queue();
    }
     */
}
