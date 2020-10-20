package com.myra.dev.marian.listeners.leveling;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
@CommandSubscribe(
        name = "leveling set"
)
public class SetLevel implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        if (arguments.length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("leveling set", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "leveling set <user> <level>`", "\uD83C\uDF96 │ Change the level of a user", false);
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        //connect to database
        Database db = new Database(event.getGuild());
        Utilities utilities = Manager.getUtilities();
        //get provided member
        User user = utilities.getUser(event, arguments[0], "leveling set", "\uD83C\uDFC5");
        if (user == null) return;
        //replace level in database
        db.getMembers().getMember(event.getGuild().getMember(user)).setLevel(Integer.parseInt(arguments[1]));
        //send success message
        Manager.getUtilities().success(event.getChannel(), "leveling set", "\uD83C\uDFC5", user.getName() + "'s level changed", user.getAsMention() + " is now level `" + arguments[1] + "`", event.getAuthor().getEffectiveAvatarUrl(), false, false, null);
    }
}
