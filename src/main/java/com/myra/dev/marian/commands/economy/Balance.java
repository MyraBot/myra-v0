package com.myra.dev.marian.commands.economy;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

@CommandSubscribe(
        name = "balance",
        aliases = {"bal", "money"}
)
public class Balance implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Get database
        Database db = new Database(ctx.getGuild());
        // Get currency
        String currency = db.getNested("economy").get("currency").toString();
        // Usage
        if (ctx.getArguments().length > 1) {
            // When 'EconomySet' class is meant
            if (ctx.getArguments()[0].equalsIgnoreCase("set")) return;
            // Usage
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("balance", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + ctx.getPrefix() + "balance <user>`", currency + " │ Shows how many " + currency + " you have.", false);
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
// Show balance
        // Get self user
        Member member = ctx.getMember();
        // Get given user
        if (ctx.getArguments().length == 1) {
            User user = utilities.getUser(ctx.getEvent(), ctx.getArguments()[0], "balance", currency);
            if (user == null) return;
            // User isn't in the guild
            if (ctx.getGuild().getMember(user) == null) {
                utilities.error(ctx.getChannel(), "balance", currency, "No member found", "the given user isn't on this server", ctx.getAuthor().getEffectiveAvatarUrl());
                return;
            }
            // Save user as member
            member = ctx.getGuild().getMember(user);
        }
        // Send balance
        EmbedBuilder balance = new EmbedBuilder()
                .setAuthor("balance", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(utilities.getMemberRoleColour(ctx.getEvent().getMember()))
                .setDescription(member.getAsMention() + "'s balance is `" + db.getMembers().getMember(member).getBalance() + "` " + currency);
        ctx.getChannel().sendMessage(balance.build()).queue();
    }
}
