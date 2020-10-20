package com.myra.dev.marian.listeners.welcome.welcomeEmbed;

import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
@CommandSubscribe(
        name = "welcome embed message"
)
public class WelcomeEmbedMessage implements Command {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        Database db = new Database(event.getGuild());

        //split message
        String[] sentMessage = event.getMessage().getContentRaw().split("\\s+", 4);
        //check for arguments
        if (sentMessage.length > 3 && sentMessage[0].equalsIgnoreCase(Prefix.getPrefix(event.getGuild()) + "welcome") && sentMessage[1].equalsIgnoreCase("embed") && sentMessage[2].equalsIgnoreCase("message")) {
            //Database
            db.getNested("welcome").set("welcomeEmbedMessage", sentMessage[3]);
            //success
            EmbedBuilder welcomeText = new EmbedBuilder();
            welcomeText.setAuthor("│ welcome", null, event.getAuthor().getEffectiveAvatarUrl());
            welcomeText.setColor(Manager.getUtilities().blue);
            welcomeText.addField("\uD83D\uDC4B │ welcome text changed to",
                    db.getNested("welcome").get("welcomeEmbedMessage")
                            .replace("{user}", event.getAuthor().getAsMention())
                            .replace("{server}", event.getGuild().getName())
                            .replace("{count}", Integer.toString(event.getGuild().getMemberCount())),
                    false);
            event.getChannel().sendMessage(welcomeText.build()).queue();
        }
    }
}