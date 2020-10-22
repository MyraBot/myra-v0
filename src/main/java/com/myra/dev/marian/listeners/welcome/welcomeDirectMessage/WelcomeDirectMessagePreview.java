package com.myra.dev.marian.listeners.welcome.welcomeDirectMessage;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

@CommandSubscribe(
        name = "welcome direct message preview",
        aliases = {"welcoe dm preview"}
)
public class WelcomeDirectMessagePreview implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        // Check for no arguments
        if (arguments.length != 0) return;

        // Get database
        Database db = new Database(event.getGuild());
        //get variables
        String welcomeColour = db.getNested("welcome").get("welcomeColour");
        String welcomeDirectMessage = db.getNested("welcome").get("welcomeDirectMessage");
        //send message
        EmbedBuilder join = new EmbedBuilder()
                .setAuthor("│ welcome", null, event.getGuild().getIconUrl())
                .setColor(Color.decode(welcomeColour))
                .setThumbnail(event.getAuthor().getEffectiveAvatarUrl())
                .setDescription(welcomeDirectMessage
                        .replace("{user}", event.getAuthor().getAsMention())
                        .replace("{server}", event.getGuild().getName())
                        .replace("{count}", Integer.toString(event.getGuild().getMemberCount())));
        event.getChannel().sendMessage(join.build()).queue();
    }
}