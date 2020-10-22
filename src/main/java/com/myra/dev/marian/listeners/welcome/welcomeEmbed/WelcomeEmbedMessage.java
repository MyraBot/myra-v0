package com.myra.dev.marian.listeners.welcome.welcomeEmbed;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "welcome embed message"
)
public class WelcomeEmbedMessage implements Command {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        // Utilities
        Utilities utilities = Manager.getUtilities();
        // Usage
        if (arguments.length == 0) {
            EmbedBuilder welcomeEmbedMessage = new EmbedBuilder()
                    .setAuthor("welcome embed message", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "welcome embed message <message>`", "\uD83D\uDCAC │ Set the text of the embed message", false)
                    .setFooter("{user} = mention the user │ {server} = server name │ {count} = user count");
            event.getChannel().sendMessage(welcomeEmbedMessage.build()).queue();
            return;
        }
        // Get message
        String message = "";
        for (int i = 1; i < arguments.length; i++) {
            message += arguments[i] + " ";
        }
        //remove last space
        message = message.substring(0, message.length() - 1);
        // Get database
        Database db = new Database(event.getGuild());
        // Update database
        db.getNested("welcome").set("welcomeEmbedMessage", message);
        // Success
        utilities.success(event.getChannel(), "welcome embed message", "\uD83D\uDCAC",
                "\uD83D\uDC4B │ welcome text changed to",
                message
                        .replace("{user}", event.getAuthor().getAsMention())
                        .replace("{server}", event.getGuild().getName())
                        .replace("{count}", Integer.toString(event.getGuild().getMemberCount())),
                event.getAuthor().getEffectiveAvatarUrl(), false, null
        );
    }
}