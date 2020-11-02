package com.myra.dev.marian.commands.leveling.administrator;

import com.myra.dev.marian.database.allMethods.Database;

import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

@CommandSubscribe(
        name = "leveling set",
        requires = "administrator"
)
public class LevelingSet implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Usage
        if (ctx.getArguments().length != 2) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("leveling set", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + ctx.getPrefix() + "leveling set <user> <level>`", "\uD83C\uDFC6 │ Change the level of a user", false);
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        // Get database
        Database db = new Database(ctx.getGuild());
        //get provided member
        User user = utilities.getUser(ctx.getEvent(), ctx.getArguments()[0], "leveling set", "\uD83C\uDFC6");
        if (user == null) return;
        // When user is a bot
        if (user.isBot()) {
            Manager.getUtilities().error(ctx.getChannel(), "leveling set", "\uD83C\uDFC6", user.getName() + " is a bot", "Bots aren't allowed to participate in the ranking competition", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //replace level in database
        db.getMembers().getMember(ctx.getGuild().getMember(user)).setLevel(Integer.parseInt(ctx.getArguments()[1]));
        //send success message
        Manager.getUtilities().success(ctx.getChannel(), "leveling set", "\uD83C\uDFC6", user.getName() + "'s level changed", user.getAsMention() + " is now level `" + ctx.getArguments()[1] + "`", ctx.getAuthor().getEffectiveAvatarUrl(), false, null);
    }
}
