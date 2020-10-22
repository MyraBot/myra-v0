package com.myra.dev.marian.listeners.welcome.welcomeDirectMessage;

import com.myra.dev.marian.database.allMethods.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class WelcomeDirectMessageRender {
    public void embed(Guild guild, PrivateChannel privateChannel, TextChannel textChannel, User user) {
        // Get database
        Database db = new Database(guild);
        // Get variables
        String welcomeColour = db.getNested("welcome").get("welcomeColour");
        String welcomeDirectMessage = db.getNested("welcome").get("welcomeDirectMessage");
        // Send direct message
        if (privateChannel != null) {
            privateChannel.sendMessage(new EmbedBuilder()
                    .setAuthor("welcome", null, guild.getIconUrl())
                    .setColor(Color.decode(welcomeColour))
                    .setThumbnail(user.getEffectiveAvatarUrl())
                    .setDescription(welcomeDirectMessage
                            .replace("{user}", user.getAsMention())
                            .replace("{server}", guild.getName())
                            .replace("{count}", Integer.toString(guild.getMemberCount())))
                    .build()
            ).queue();
        }
        // Send message
        else {
            textChannel.sendMessage(new EmbedBuilder()
                    .setAuthor("welcome", null, guild.getIconUrl())
                    .setColor(Color.decode(welcomeColour))
                    .setThumbnail(user.getEffectiveAvatarUrl())
                    .setDescription(welcomeDirectMessage
                            .replace("{user}", user.getAsMention())
                            .replace("{server}", guild.getName())
                            .replace("{count}", Integer.toString(guild.getMemberCount())))
                    .build()
            ).queue();
        }
    }
}
