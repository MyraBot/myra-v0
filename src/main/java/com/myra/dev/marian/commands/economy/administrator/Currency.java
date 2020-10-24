package com.myra.dev.marian.commands.economy.administrator;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(name = "economy currency")
public class Currency implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Get database
        Database db = new Database(event.getGuild());
        // Usage
        if (arguments.length != 1) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("leveling currency", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "economy currency <emoji>`", db.getNested("economy").get("currency") + " │ Set a custom currency", false);
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * Change currency
         */
        // Get new currency
        String currency = arguments[0];
        // Update database
        db.getNested("economy").set("currency", currency);
        // Send success message
        utilities.success(event.getChannel(), "economy currency", currency, "Changed currency", "Changed currency to " + currency, event.getAuthor().getEffectiveAvatarUrl(), false, null);
    }
}
