package com.myra.dev.marian.commands.economy.administrator;

import com.myra.dev.marian.database.allMethods.Database;

import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

@CommandSubscribe(
        name = "economy set",
        aliases = {"balance set", "bal set", "money set"},
        requires = Permissions.ADMINISTRATOR
)
public class EconomySet implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Get utilities
        Utilities utilities = Utilities.getUtils();
        // Usage
        if (ctx.getArguments().length != 2) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("economy set", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + ctx.getPrefix() + "economy set <user> <balance>`", "\uD83D\uDC5B │ Change a users balance", false)
                    .setFooter("Use: + / -, to add and subtract money");
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
// Change balance
        // Get User
        Member member = ctx.getEvent().getMember();
        // Get given user
        if (ctx.getArguments().length == 2) {
            User user = utilities.getUser(ctx.getEvent(), ctx.getArguments()[0], "economy set", "\uD83D\uDC5B");
            if (user == null) return;
            // User isn't in the guild
            if (ctx.getGuild().getMember(user) == null) {
                utilities.error(ctx.getChannel(), "economy set", "\uD83D\uDC5B", "No member found", "the given user isn't on this server", ctx.getAuthor().getEffectiveAvatarUrl());
                return;
            }
            // Save user as member
            member = ctx.getGuild().getMember(user);
        }
        // Get database
        Database db = new Database(ctx.getGuild());
        // Get old balance
        int oldBalance = db.getMembers().getMember(member).getBalance();
        // Get new balance
        int updatedBalance;
        // Add balance
        if (ctx.getArguments()[1].matches("[+]\\d+")) {
            updatedBalance = oldBalance + Integer.parseInt(ctx.getArguments()[1].substring(1));
        }
        // Subtract balance
        else if (ctx.getArguments()[1].matches("[-]\\d+")) {
            updatedBalance = oldBalance - Integer.parseInt(ctx.getArguments()[1].substring(1));
        }
        // Set balance
        else if (ctx.getArguments()[1].matches("\\d+")) {
            updatedBalance = Integer.parseInt(ctx.getArguments()[1]);
        }
        // Error
        else {
            utilities.error(ctx.getChannel(), "economy set", "\uD83D\uDC5B", "Invalid operator", "Please use `+` to add money, `-` to subtract money or leave the operators out to set an exact amount of money", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // Change balance in database
        db.getMembers().getMember(member).setBalance(updatedBalance);
        // Success
        utilities.success(ctx.getChannel(), "economy set", "\uD83D\uDC5B", "Updated balance", member.getAsMention() + "has now `" + updatedBalance + "` " + db.getNested("economy").getString("currency"), ctx.getAuthor().getEffectiveAvatarUrl(), false, null);
    }
}
