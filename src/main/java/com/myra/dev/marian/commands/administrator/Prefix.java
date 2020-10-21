package com.myra.dev.marian.commands.administrator;

import com.myra.dev.marian.Main;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "prefix"
)
public class Prefix implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        //command usage
        if (arguments.length != 1) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("│ change prefix", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + com.myra.dev.marian.database.Prefix.getPrefix(event.getGuild()) + "prefix <prefix>`", "\uD83D\uDCCC │ Change the prefix of the bot", false)
                    .addField("`" + "@" + event.getJDA().getSelfUser().getName() + "prefix`", "\uD83D\uDCCC │ Reset the prefix of the bot", false);
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        /**
         * prefix change
         */
        //Database
      com.myra.dev.marian.database.Prefix.setPrefix(event.getGuild(), arguments[0]);
        //success information
        Manager.getUtilities().success(event.getChannel(),
                "prefix", "\uD83D\uDCCC",
                "Prefix changed",
                "Prefix changed to `" + com.myra.dev.marian.database.Prefix.getPrefix(event.getGuild()) + "`",
                event.getAuthor().getEffectiveAvatarUrl(),
                false, null);
        //prefix reset
        if (event.getMessage().getContentRaw().equalsIgnoreCase(event.getJDA().getSelfUser().getAsMention().replace("<@", "<@!") + "prefix")) {
            //Database
            com.myra.dev.marian.database.Prefix.setPrefix(event.getGuild(), Main.prefix);
            //success info
            Manager.getUtilities().success(event.getChannel(),
                    "prefix", "\uD83D\uDCCC",
                    "Prefix reset",
                    "Prefix changed to `" + Main.prefix + "`",
                    event.getAuthor().getEffectiveAvatarUrl(),
                    false, null);
        }
    }
}
