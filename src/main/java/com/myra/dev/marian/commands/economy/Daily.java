package com.myra.dev.marian.commands.economy;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.database.allMethods.GetMember;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@CommandSubscribe(
        name = "daily"
)
public class Daily implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Check for no arguments
        if (arguments.length != 0) return;
        // Get member from database
        GetMember member = new Database(event.getGuild()).getMembers().getMember(event.getMember());
        // Get last claimed reward
        long lastClaim = member.getLastClaim();
        // Get duration, which passed (in milliseconds)
        long passedTime = System.currentTimeMillis() - lastClaim;
        // New streak
        int streak = 0;
        // Get reward
        int dailyReward = 0;
        // New balance
        int balance = member.getBalance();
        // Get currency
        String currency = new Database(event.getGuild()).getNested("economy").get("currency");
        // Create embed
        EmbedBuilder daily = new EmbedBuilder()
                .setAuthor("daily", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().getMemberRoleColour(event.getMember()));

        // Too early
        if (TimeUnit.MILLISECONDS.toHours(passedTime) < 12) {
            long nextBonusAt = lastClaim + TimeUnit.HOURS.toMillis(12);
            String nextBonusIn = Manager.getUtilities().formatTime(nextBonusAt - System.currentTimeMillis());


            daily.setDescription("You need to wait more " + nextBonusIn);
            event.getChannel().sendMessage(daily.build()).queue();
            return;
        }

        // Claim reward
        if (TimeUnit.MILLISECONDS.toHours(passedTime) > 12) {
            // Update streak
            streak = member.getDailyStreak() + 1;
            // Missed reward
            if (TimeUnit.MILLISECONDS.toHours(passedTime) > 24) {
                // Reset streak
                streak = 1;
            }

            // Get bonus
            if (streak != 1) { // With a steak of 1, you're not be able to receive a bonus
                // Set probability
                int percent = streak / 100 * 2;
                Random random = new Random();
                // If bonus is reached
                if (random.nextInt() <= percent) {
                    dailyReward = +500;
                    daily.addField("\uD83D\uDCB0 │ bonus", "You got a bonus! Congratulations! **+ 500**", false);
                }
                // If bonus isn't reached
                else {
                    daily.addField("\uD83D\uDCB0 │ bonus", "You could have got a bonus here \uD83D\uDE14, BUT YOU DIDN'T <:lmao:768519476400095262>", false);
                }
            }
        }
        // Get daily reward
        dailyReward += streak * 100;
        // Update balance
        member.setBalance(member.getBalance() + dailyReward);
        // Update last claim
        member.updateClaimedReward();
        // Update streak
        member.setDailyStreak(streak);

        // Show reward
        daily.setDescription("**+" + dailyReward + "** " + currency + "! Now you have `" + balance + "` " + currency);
        // Show streak
        daily.setFooter("streak: " + streak + "/14");
        // Send daily reward
        event.getChannel().sendMessage(daily.build()).queue();
    }
}
