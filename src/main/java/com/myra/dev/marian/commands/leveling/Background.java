package com.myra.dev.marian.commands.leveling;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Graphic;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

@CommandSubscribe(
        name = "leveling background"
)
public class Background implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Check if argument is an image
        try {
            ImageIO.read(new URL(arguments[0]));
        } catch (Exception e) {
            utilities.error(event.getChannel(), "leveling background", "❓", "Invalid image", e.getMessage(), event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // Get image from Url
        BufferedImage background = ImageIO.read(new URL(arguments[0]));
        // Resize image
        Image modifiedImage = background.getScaledInstance(350, 100, Image.SCALE_SMOOTH);
        // Parse back to BufferedImage
        background = new Graphic().toBufferedImage(modifiedImage);
        // Parse to InputStream
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(background, "png", os);
        InputStream file = new ByteArrayInputStream(os.toByteArray());
        // Success
        EmbedBuilder success = new EmbedBuilder()
                .setAuthor("rank background", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(utilities.blue)
                .addField("\uD83C\uDFC1 │ Rank background updated", "Rank background changed to:", false)
                .setImage("attachment://cat.png"); // Specify this in sendFile as "cat.png"
        Message message = event.getChannel().sendFile(file, "cat.png").embed(success.build()).complete();
        // Save new image in database
        new Database(event.getGuild()).getMembers().getMember(event.getMember()).setRankBackground(message.getEmbeds().get(0).getImage().getUrl());
    }
}
