package com.myra.dev.marian.commands.economy.administrator;

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
        name = "economy set",
        aliases = {"balance set", "bal set", "money set"}
)
public class EconomySet implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Usage
        if (arguments.length != 2) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("economy set", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "economy set <user> <balance>`", "\uD83D\uDC5B │ Change a users balance", false)
                    .setFooter("Use: + / -, to add and subtract money");
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * Change balance
         */
        // Get User
        // Get self user
        Member member = event.getMember();
        // Get given user
        if (arguments.length == 2) {
            User user = utilities.getUser(event, arguments[0], "economy set", "\uD83D\uDC5B");
            if (user == null) return;
            // User isn't in the guild
            if (event.getGuild().getMember(user) == null) {
                utilities.error(event.getChannel(), "economy set", "\uD83D\uDC5B", "No member found", "the given user isn't on this server", event.getAuthor().getEffectiveAvatarUrl());
                return;
            }
            // Save user as member
            member = event.getGuild().getMember(user);
        }
        // Get database
        Database db = new Database(event.getGuild());
        // Get old balance
        int oldBalance = db.getMembers().getMember(member).getBalance();
        // Get new balance
        int updatedBalance;
        // Add balance
        if (arguments[1].matches("[+]\\d+")) {
            updatedBalance = oldBalance + Integer.parseInt(arguments[1].substring(1));
        }
        // Subtract balance
        else if (arguments[1].matches("[-]\\d+")) {
            updatedBalance = oldBalance - Integer.parseInt(arguments[1].substring(1));
        }
        // Set balance
        else if (arguments[1].matches("\\d+")) {
            updatedBalance = Integer.parseInt(arguments[1]);
        }
        // Error
        else {
            utilities.error(event.getChannel(), "economy set", "\uD83D\uDC5B", "Invalid operator", "Please use `+` to add money, `-` to subtract money or leave the operators out to set an exact amount of money", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // Change balance in database
        db.getMembers().getMember(member).setBalance(updatedBalance);
        // Success
        utilities.success(event.getChannel(), "economy set", "\uD83D\uDC5B", "Updated balance", member.getAsMention() + "has now `" + updatedBalance + "` " + db.getNested("economy").get("currency"), event.getAuthor().getEffectiveAvatarUrl(), false, null);
    }
}
