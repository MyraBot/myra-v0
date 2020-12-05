package com.myra.dev.marian.commands.economy;

import com.myra.dev.marian.utilities.APIs.TopGG;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.database.allMethods.GetMember;
import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.concurrent.TimeUnit;

@CommandSubscribe(
        name = "daily"
)
public class Daily implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Check for no arguments
        if (ctx.getArguments().length != 0) return;
        // Get member from database
        GetMember member = new Database(ctx.getGuild()).getMembers().getMember(ctx.getEvent().getMember());
        // Get last claimed reward
        long lastClaim = member.getLastClaim();
        // Get duration, which passed (in milliseconds)
        long passedTime = System.currentTimeMillis() - lastClaim;
        int streak = 0; // Create daily streak variable
        int dailyReward = 0; // Create reward variable
        final String currency = new Database(ctx.getGuild()).getNested("economy").get("currency").toString(); // Get currency
        // Create embed
        EmbedBuilder daily = new EmbedBuilder()
                .setAuthor("daily", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(Utilities.getUtils().getMemberRoleColour(ctx.getEvent().getMember()));

        // Too early
        if (TimeUnit.MILLISECONDS.toHours(passedTime) < 12) {
            long nextBonusAt = lastClaim + TimeUnit.HOURS.toMillis(12);
            String nextBonusIn = Utilities.getUtils().formatTime(nextBonusAt - System.currentTimeMillis());


            daily.setDescription("You need to wait more " + nextBonusIn);
            ctx.getChannel().sendMessage(daily.build()).queue();
            return;
        }

        // Claim reward
        if (TimeUnit.MILLISECONDS.toHours(passedTime) >= 12) {
            // Didn't miss reward
            if (TimeUnit.MILLISECONDS.toHours(passedTime) <= 24) {
                streak = member.getDailyStreak() + 1; // Update streak
            }
            // Missed reward
            if (TimeUnit.MILLISECONDS.toHours(passedTime) > 24) {
                streak = 1; // Reset streak
            }


            dailyReward += streak * 100; // Get daily reward
            // Get vote bonus
            if (TopGG.getInstance().hasVoted(ctx.getAuthor())) {
                daily.setDescription("Thanks for voting **+ 500**\n");
                dailyReward =+ 500;
            }
            dailyReward += streak * 100;
            int newBalance = member.getBalance() + dailyReward; // Get new balance

            member.setBalance(newBalance); // Update balance
            member.updateClaimedReward(); // Update last claim
            member.setDailyStreak(streak); // Update streak

            // Show reward
            daily.appendDescription("**+" + dailyReward + "** " + currency + "! Now you have `" + newBalance + "` " + currency);
            // Show streak
            daily.setFooter("streak: " + streak + "/14");
            // Send daily reward
            ctx.getChannel().sendMessage(daily.build()).queue();
        }
    }
}
