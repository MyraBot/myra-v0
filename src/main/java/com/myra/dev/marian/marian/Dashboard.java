package com.myra.dev.marian.marian;

import com.myra.dev.marian.utilities.management.Resources;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandSubscribe(
        name = "dashboard",
        aliases = {"dash"}
)
public class Dashboard implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        Resources resources = new Resources();

        EmbedBuilder dashboard = new EmbedBuilder()
                .setAuthor("dashboard", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .addField("CPU usage", resources.getCpuLoad(), false)
                .addField("RAM usage", resources.getRAMUsage(), false)
                .addField("Shard", String.valueOf(ctx.getEvent().getJDA().getShardInfo().getShardId()), false)
                .addField("Guilds", String.valueOf(ctx.getEvent().getJDA().getGuilds().size()), false);
        ctx.getChannel().sendMessage(dashboard.build()).queue();
    }
}
