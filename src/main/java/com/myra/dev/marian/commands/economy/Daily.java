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

        // Get database
        GetMember member = new Database(event.getGuild()).getMembers().getMember(event.getMember());
        // Get last claimed reward
        long lastClaim = member.getLastClaim();
        // Get duration, which passed
        long passedTime = System.currentTimeMillis() - lastClaim;
        // If 24 hours are passed
        if (TimeUnit.MILLISECONDS.toHours(passedTime) > 24) {
            // Create embed builder
            EmbedBuilder daily = new EmbedBuilder()
                    .setAuthor("daily", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().getMemberRoleColour(event.getMember()));
            // Get currency
            String currency = new Database(event.getGuild()).getNested("economy").get("currency");
            // Get streak
            int streak = member.getDailyStreak();
            // Update streak if it's not 14 (maximum)
            if (streak != 14) {
                streak = streak + 1;
                member.setDailyStreak(streak);
            }
            // Get balance without bonus
            int dailyReward = streak * 100;
            int balance = dailyReward + member.getBalance();
            daily.setDescription("**+" + dailyReward + "** " + currency + "! Now you have `" + balance + "` " + currency);
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
            // Show streak
            daily.setFooter("streak: " + streak + "/14");
            // Update balance
            member.setBalance(member.getBalance() + dailyReward);
            // Update
            member.updateClaimedReward();
            // Send daily reward
            event.getChannel().sendMessage(daily.build()).queue();
        } else {

            long nextBonusAt = lastClaim + TimeUnit.DAYS.toMillis(1);
            String nextBonusIn = Manager.getUtilities().formatTime(nextBonusAt - System.currentTimeMillis());

            EmbedBuilder daily = new EmbedBuilder()
                    .setAuthor("daily", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().getMemberRoleColour(event.getMember()))
                    .setDescription("You need to wait more " + nextBonusIn);
            event.getChannel().sendMessage(daily.build()).queue();
        }
    }
}
