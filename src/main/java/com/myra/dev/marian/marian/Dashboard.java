package com.myra.dev.marian.marian;

import com.myra.dev.marian.utilities.Resources;
import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandSubscribe(
        name = "dashboard",
        aliases = {"dash"},
        requires = Permissions.MARIAN
)
public class Dashboard implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        Resources resources = new Resources();

        EmbedBuilder dashboard = new EmbedBuilder()
                .setAuthor("dashboard", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .addField("CPU usage", resources.getCpuLoad(), false)
                .addField("RAM usage", resources.getRAMUsage() + "mb", false)
                .addField("Shards", String.valueOf(ctx.getEvent().getJDA().getShardManager().getShardsTotal()), false)
                .addField("Guilds", String.valueOf(ctx.getEvent().getJDA().getGuilds().size()), false);
        ctx.getChannel().sendMessage(dashboard.build()).queue();
    }
}
