package com.myra.dev.marian.commands.economy.administrator;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.management.Manager;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandSubscribe(
        name = "economy currency",
        requires = "administrator"
)
public class Currency implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Get utilities
        Utilities utilities = Utilities.getUtils();
        // Get database
        Database db = new Database(ctx.getGuild());
        // Usage
        if (ctx.getArguments().length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("leveling currency", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + ctx.getPrefix() + "economy currency <emoji>`", db.getNested("economy").get("currency") + " │ Set a custom currency", false);
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * Change currency
         */
        // Get new currency
        String currency = "";
        for (String argument : ctx.getArguments()) {
            currency += argument + " ";
        }
        //remove last space
        currency = currency.substring(0, currency.length() - 1);
        // Update database
        db.getNested("economy").set("currency", currency, Manager.type.STRING);
        // Send success message
        utilities.success(ctx.getChannel(), "economy currency", currency, "Changed currency", "Changed currency to " + currency, ctx.getAuthor().getEffectiveAvatarUrl(), false, null);
    }
}
