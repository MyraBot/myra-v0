package com.myra.dev.marian.commands.economy.administrator;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "economy"
)
public class EconomyHelp implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        // Usage
        if (arguments.length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("leveling currency", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "economy set <user> <balance>`", "\uD83D\uDC5B │ Change a users balance", false)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "economy currency <currency>`", new Database(event.getGuild()).getNested("economy").get("currency") + " │ Set a custom currency", false);
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
    }
}
