package com.myra.dev.marian.commands.administrator;

import com.myra.dev.marian.Main;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandSubscribe(
        name = "prefix"
)
public class Prefix implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        //missing permissions
        if (!Permissions.isAdministrator(ctx.getMember())) return;
        //command usage
        if (ctx.getArguments().length != 1) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("prefix", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + ctx.getPrefix() + "prefix <prefix>`", "\uD83D\uDCCC │ Change the prefix of the bot", false)
                    .addField("`" + "@" + ctx.getEvent().getJDA().getSelfUser().getName() + "prefix`", "\uD83D\uDCCC │ Reset the prefix of the bot", false);
            ctx.getChannel().sendMessage(embed.build()).queue();
            return;
        }
// Change the prefix
        com.myra.dev.marian.database.Prefix.setPrefix(ctx.getGuild(), ctx.getArguments()[0]);
        //success information
        Manager.getUtilities().success(ctx.getChannel(),
                "prefix", "\uD83D\uDCCC",
                "Prefix changed",
                "Prefix changed to `" + new Database(ctx.getGuild()).get("prefix") + "`",
                ctx.getAuthor().getEffectiveAvatarUrl(),
                false, null);
        //prefix reset
        if (ctx.getEvent().getMessage().getContentRaw().equalsIgnoreCase(ctx.getEvent().getJDA().getSelfUser().getAsMention().replace("<@", "<@!") + "prefix")) {
            //Database
            com.myra.dev.marian.database.Prefix.setPrefix(ctx.getGuild(), Main.prefix);
            //success info
            Manager.getUtilities().success(ctx.getChannel(),
                    "prefix", "\uD83D\uDCCC",
                    "Prefix reset",
                    "Prefix changed to `" + Main.prefix + "`",
                    ctx.getAuthor().getEffectiveAvatarUrl(),
                    false, null);
        }
    }
}
