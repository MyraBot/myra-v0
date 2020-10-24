package com.myra.dev.marian.commands.leveling.administrator;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "leveling set"
)
public class LevelingSet implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Usage
        if (arguments.length != 2) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("leveling set", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "leveling set <user> <level>`", "\uD83C\uDFC6 │ Change the level of a user", false);
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        // Get database
        Database db = new Database(event.getGuild());
        //get provided member
        User user = utilities.getUser(event, arguments[0], "leveling set", "\uD83C\uDFC6");
        if (user == null) return;
        // When user is a bot
        if (user.isBot()) {
            Manager.getUtilities().error(event.getChannel(), "leveling set", "\uD83C\uDFC6", user.getName() + " is a bot", "Bots aren't allowed to participate in the ranking competition", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //replace level in database
        db.getMembers().getMember(event.getGuild().getMember(user)).setLevel(Integer.parseInt(arguments[1]));
        //send success message
        Manager.getUtilities().success(event.getChannel(), "leveling set", "\uD83C\uDFC6", user.getName() + "'s level changed", user.getAsMention() + " is now level `" + arguments[1] + "`", event.getAuthor().getEffectiveAvatarUrl(), false, null);
    }
}
