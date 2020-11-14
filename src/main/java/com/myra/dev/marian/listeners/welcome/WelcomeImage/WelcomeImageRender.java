package com.myra.dev.marian.listeners.welcome.WelcomeImage;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Graphic;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class WelcomeImageRender {

    /**
     * @param user    The user who is greeted.
     * @param guild   The guild the new user joined.
     * @param channel The channel the greeting will be send.
     */
    public void welcomeImage(Guild guild, User user, TextChannel channel) throws Exception {
        //database
        Database db = new Database(guild);
        //get welcome image background
        BufferedImage background;
        //if no background is set
        if (db.getNested("welcome").get("welcomeImageBackground").equals("not set")) {
            background = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("welcomeImage.png"));
        }
        //if guild has a custom background
        else {
            // Url is available
            try {
                background = ImageIO.read(new URL(db.getNested("welcome").get("welcomeImageBackground").toString()));
            }
            // Invalid link
            catch (IOException e) {
                background = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("welcomeImage.png"));
            }
        }
        //get font
        String font = db.getNested("welcome").get("welcomeImageFont").toString();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(font + ".ttf");
        //graphics
        Graphic graphic = Graphic.getInstance();
        Graphics graphics = background.getGraphics();
        Graphics2D graphics2D = (Graphics2D) graphics;
        //enable anti aliasing
        graphic.enableAntiAliasing(graphics);
        //choose format
        if (background.getHeight() > background.getWidth()) {
            portrait(background, user, graphic, graphics, graphics2D, inputStream);
        } else {
            landscape(background, user, graphic, graphics, graphics2D, inputStream);
        }

        /**
         * send message
         */
        channel.sendFile(
                graphic.toInputStream(background),
                user.getName().toLowerCase() + "_welcome.png"
        ).queue();
    }

    private void landscape(BufferedImage background, User user, Graphic graphic, Graphics graphics, Graphics2D graphics2D, InputStream inputStream) throws Exception {
        //resize avatar
        BufferedImage avatar = graphic.getAvatar(user.getEffectiveAvatarUrl());
        avatar = graphic.resizeSquaredImage(avatar, background.getHeight() / 350f);
        //load font
        Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        //draw avatar
        graphics2D.drawImage(
                avatar,
                graphic.imageCenter(Graphic.axis.X, avatar, background),
                graphic.imageCenter(Graphic.axis.Y, avatar, background) - background.getHeight() / 4,
                null);
        //draw circle around avatar
        graphics2D.setColor(Color.white);
        graphics2D.setStroke(new BasicStroke(
                background.getHeight() / 200f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        ));
        graphics2D.drawOval(
                graphic.imageCenter(Graphic.axis.X, avatar, background),
                graphic.imageCenter(Graphic.axis.Y, avatar, background) - background.getHeight() / 4,
                avatar.getWidth(), avatar.getHeight()
        );
        //draw 'welcome'
        font = font.deriveFont(background.getWidth() / 15f);
        graphics.setFont(font);
        //draw 'level'
        graphics.drawString("welcome",
                graphic.textCenter(Graphic.axis.X, "welcome", font, background),
                graphic.textCenter(Graphic.axis.Y, "welcome", font, background) + background.getHeight() / 5
        );
        //draw user name

        //make font size smaller if text is too big
        FontRenderContext fontRenderContext = new FontRenderContext(new AffineTransform(), true, true);
        float size = background.getWidth() / 10f;

        while (Math.round(font.getStringBounds(user.getName(), fontRenderContext).getWidth()) > background.getWidth()) {
            size = size - 1.0F;
            font = font.deriveFont(size);
        }
        //set font
        graphics.setFont(font);
        //draw user name
        graphics.drawString(user.getName(),
                graphic.textCenter(Graphic.axis.X, user.getName(), font, background),
                (int) (graphic.textCenter(Graphic.axis.Y, user.getName(), font, background) + background.getHeight() / 2.25)
        );
    }

    private void portrait(BufferedImage background, User user, Graphic graphic, Graphics graphics, Graphics2D graphics2D, InputStream inputStream) throws Exception {
        //resize avatar
        BufferedImage avatar = graphic.getAvatar(user.getEffectiveAvatarUrl());
        avatar = graphic.resizeSquaredImage(avatar, background.getWidth() / 200f);
        //load font
        Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        //draw avatar
        graphics2D.drawImage(
                avatar,
                graphic.imageCenter(Graphic.axis.X, avatar, background),
                graphic.imageCenter(Graphic.axis.Y, avatar, background) - background.getHeight() / 3,
                null);
        //draw circle around avatar
        graphics2D.setColor(Color.white);
        graphics2D.setStroke(new BasicStroke(
                background.getHeight() / 250f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        ));
        graphics2D.drawOval(
                graphic.imageCenter(Graphic.axis.X, avatar, background),
                graphic.imageCenter(Graphic.axis.Y, avatar, background) - background.getHeight() / 3,
                avatar.getWidth(), avatar.getHeight()
        );
        //draw 'welcome'
        font = font.deriveFont(background.getWidth() / 6f);
        graphics.setFont(font);
        //draw 'level'
        graphics.drawString("welcome",
                graphic.textCenter(Graphic.axis.X, "welcome", font, background),
                graphic.textCenter(Graphic.axis.Y, "welcome", font, background)
        );
        //draw user name

        //make font size smaller if text is too big
        FontRenderContext fontRenderContext = new FontRenderContext(new AffineTransform(), true, true);
        float size = background.getWidth() / 5f;

        while (Math.round(font.getStringBounds(user.getName(), fontRenderContext).getWidth()) > background.getWidth()) {
            size = size - 1.0F;
            font = font.deriveFont(size);
        }
        //set font
        graphics.setFont(font);
        //draw user name
        graphics.drawString(user.getName(),
                graphic.textCenter(Graphic.axis.X, user.getName(), font, background),
                (graphic.textCenter(Graphic.axis.Y, user.getName(), font, background) + background.getHeight() / 5)
        );
    }
}
