package com.myra.dev.marian.commands.economy;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

@CommandSubscribe(
        name = "give",
        aliases = {"transfer", "pay"}
)
public class Give implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Get utilities
        final Utilities utilities = Manager.getUtilities();
        // Usage
        if (ctx.getArguments().length != 2) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("give", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + ctx.getPrefix() + "give <user> <balance>`", "\uD83D\uDCB8 │ Give credits to other users", false);
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
// Get user
        final User user = utilities.getUser(ctx.getEvent(), ctx.getArguments()[0], "give", "\uD83D\uDCB8");
        if (user == null) return;
// Errors
        // Amount of money aren't digits
        if (!ctx.getArguments()[1].matches("\\d+")) {
            utilities.error(ctx.getChannel(), "give", "\uD83D\uDCB8", "Invalid number", "Please only use digits", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // User is bot
        if (user.isBot()) {
            utilities.error(ctx.getChannel(), "give", "\uD83D\uDCB8", "Invalid user", "You're not allowed to give credits to bots", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
// Transfer money;
        // Get database
        Database db = new Database(ctx.getGuild());
        // Transfer money
        db.getMembers().getMember(ctx.getGuild().getMember(user)).setBalance(db.getMembers().getMember(ctx.getMember()).getBalance() + Integer.parseInt(ctx.getArguments()[1]));
        // Success message
        EmbedBuilder success = new EmbedBuilder()
                .setAuthor("give", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(utilities.blue)
                .setDescription(ctx.getAuthor().getAsMention() + " gave you `" + ctx.getArguments()[1] + "` " + db.getNested("economy").get("currency"));
        ctx.getChannel().sendMessage(user.getAsMention() + success.build()).queue();
    }
}
