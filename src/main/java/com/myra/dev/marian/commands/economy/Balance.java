package com.myra.dev.marian.commands.economy;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "balance",
        aliases = {"bal", "money"}
)
public class Balance implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Get database
        Database db = new Database(event.getGuild());
        // Get currency
        String currency = db.getNested("economy").get("currency");
        // Usage
        if (arguments.length > 1) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("balance", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "balance <user>`", currency + " │ Shows how many " + currency + " you have.", false);
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * Show balance
         */
        // Get self user
        Member member = event.getMember();
        // Get given user
        if (arguments.length == 1) {
            User user = utilities.getUser(event, arguments[0], "balance", currency);
            if (user == null) return;
            // User isn't in the guild
            if (event.getGuild().getMember(user) == null) {
                utilities.error(event.getChannel(), "balance", currency, "No member found", "the given user isn't on this server", event.getAuthor().getEffectiveAvatarUrl());
                return;
            }
            // Save user as member
            member = event.getGuild().getMember(user);
        }
        // Send balance
        EmbedBuilder balance = new EmbedBuilder()
                .setAuthor("balance", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(utilities.getMemberRoleColour(event.getMember()))
                .setDescription(member.getAsMention() + "'s balance is `" + db.getMembers().getMember(member).getBalance() + "` " + currency);
        event.getChannel().sendMessage(balance.build()).queue();
    }
}
