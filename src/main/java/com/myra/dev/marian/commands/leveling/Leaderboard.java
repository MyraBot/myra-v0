package com.myra.dev.marian.commands.leveling;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.database.documents.MemberDocument;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;

@CommandSubscribe(
        command = "leaderboard",
        name = "leaderboard",
        aliases = {"top"}
)
public class Leaderboard implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        //get leaderboard
        List<MemberDocument> leaderboardList = new Database(ctx.getGuild()).getMembers().getLeaderboard();
        // Create leaderboard
        StringBuilder top10 = new StringBuilder();
        // Add first 10 members
        for (int i = 0; i < leaderboardList.size() - 2; i++) {
            top10.append(i + 1 + " \uD83C\uDF97 `" + leaderboardList.get(i).getLevel() + "` **" + leaderboardList.get(i).getName() + "**\n");
            // Show only the first 10 members
            if (i == 9) break;
        }
        /*        String top10 =
                        "1 \uD83D\uDC51 `" + leaderboardList.get(0).getLevel() + "` **" + leaderboardList.get(0).getName() + "**\n" +
                                "2 \uD83D\uDD31 `" + leaderboardList.get(1).getLevel() + "` **" + leaderboardList.get(1).getName() + "**\n" +
                                "3 \uD83C\uDFC6 `" + leaderboardList.get(2).getLevel() + "` **" + leaderboardList.get(2).getName() + "**\n" +
                                "4 \uD83C\uDF96 `" + leaderboardList.get(3).getLevel() + "` **" + leaderboardList.get(3).getName() + "**\n" +
                                "5 \uD83C\uDFC5 `" + leaderboardList.get(4).getLevel() + "` **" + leaderboardList.get(4).getName() + "**\n" +
                                "6 \u26A1 `" + leaderboardList.get(5).getLevel() + "` **" + leaderboardList.get(5).getName() + "**\n" +
                                "7 \uD83C\uDF97 `" + leaderboardList.get(6).getLevel() + "` **" + leaderboardList.get(6).getName() + "**\n" +
                                "8 \uD83C\uDF97 `" + leaderboardList.get(7).getLevel() + "` **" + leaderboardList.get(7).getName() + "**\n" +
                                "9 \uD83C\uDF97 `" + leaderboardList.get(8).getLevel() + "` **" + leaderboardList.get(8).getName() + "**\n" +
                                "10 \uD83C\uDF97 `" + leaderboardList.get(9).getLevel() + "` **" + leaderboardList.get(9).getName() + "**\n";*/
        //create embed
        EmbedBuilder leaderboard = new EmbedBuilder()
                .setAuthor(ctx.getGuild().getName() + "'s leaderboard", null, ctx.getGuild().getIconUrl())
                .setColor(Utilities.getUtils().blue)
                .setDescription(top10);
        //send message
        ctx.getChannel().sendMessage(leaderboard.build()).queue();
    }
}
