package com.myra.dev.marian.listeners.welcome.welcomeDirectMessage;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.database.allMethods.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "welcome direct message message",
        aliases = {"welcome dm message"}
)
public class WelcomeDirectMessageMessage implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        // Usage
        if (arguments.length == 0) {
            EmbedBuilder welcomeDirectMessageMessage = new EmbedBuilder()
                    .setAuthor("welcome direct message", null, event.getAuthor().getEffectiveAvatarUrl())
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome direct message message <message>`", "\uD83D\uDCAC │ change the text of the direct messages", false)
                    .setFooter("{user} = mention the user │ {server} = server name │ {count} = user count");
            event.getChannel().sendMessage(welcomeDirectMessageMessage.build()).queue();
            return;
        }
        Database db = new Database(event.getGuild());
        // Get message
        String message = "";
        for (int i = 0; i < arguments.length; i++) {
            message += arguments[i] + " ";
        }
        //remove last space
        message = message.substring(0, message.length() - 1);
        //change value in database
        db.getNested("welcome").set("welcomeDirectMessage", message);
        //success
        String welcomeMessage = db.getNested("welcome").get("welcomeDirectMessage");
        Manager.getUtilities().success(event.getChannel(), "welcome direct message", "\u2709\uFE0F", "Welcome message changed",
                welcomeMessage
                        .replace("{user}", event.getAuthor().getAsMention())
                        .replace("{server}", event.getGuild().getName())
                        .replace("{count}", Integer.toString(event.getGuild().getMemberCount())),
                event.getAuthor().getEffectiveAvatarUrl(), false, null);
    }
}
