package com.myra.dev.marian.commands.leveling;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Graphic;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import com.myra.dev.marian.utilities.management.commands.CommandContext;

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
    public void execute(CommandContext ctx) throws Exception {
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        // Check if argument is an image
        try {
            ImageIO.read(new URL(ctx.getArguments()[0]));
        } catch (Exception e) {
            utilities.error(ctx.getChannel(), "leveling background", "❓", "Invalid image", e.getMessage(), ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // Get image from Url
        BufferedImage background = ImageIO.read(new URL(ctx.getArguments()[0]));
        // Resize image
        Image modifiedImage = background.getScaledInstance(350, 100, Image.SCALE_SMOOTH);
        // Parse back to BufferedImage
        background = new Graphic().toBufferedImage(modifiedImage);
        // Parse to InputStream
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.flush();
        outStream.close();
        ImageIO.write(background, "png", outStream);
        InputStream file = new ByteArrayInputStream(outStream.toByteArray());
        // Success
        EmbedBuilder success = new EmbedBuilder()
                .setAuthor("rank background", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(utilities.blue)
                .addField("\uD83C\uDFC1 │ Rank background updated", "Rank background changed to:", false)
                .setImage("attachment://cat.png"); // Specify this in sendFile as "cat.png"
        Message message = ctx.getChannel().sendFile(file, "cat.png").embed(success.build()).complete();
        // Save new image in database
        new Database(ctx.getGuild()).getMembers().getMember(ctx.getEvent().getMember()).setRankBackground(message.getEmbeds().get(0).getImage().getUrl());
    }
}
