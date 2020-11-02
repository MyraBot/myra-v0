package com.myra.dev.marian.utilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Graphic {
    //enable anti aliasing for Graphics
    public void enableAntiAliasing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
    }

    //enable anti aliasing for Graphics 2D
    public void enableAntiAliasing(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    }

    //draw avatar
    public BufferedImage getAvatar(String avatarUrl) throws IOException {
        BufferedImage result = null;
        //read image from url
        BufferedImage avatar = ImageIO.read(new URL(avatarUrl));

        int diameter = Math.min(avatar.getWidth(), avatar.getHeight());
        BufferedImage mask = new BufferedImage(avatar.getWidth(), avatar.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = mask.createGraphics();
        applyQualityRenderingHints(g2d);
        g2d.fillOval(0, 0, diameter - 1, diameter - 1);
        g2d.dispose();

        result = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        g2d = result.createGraphics();
        applyQualityRenderingHints(g2d);
        int x = (diameter - avatar.getWidth()) / 2;
        int y = (diameter - avatar.getHeight()) / 2;
        g2d.drawImage(avatar, x, y, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
        g2d.drawImage(mask, 0, 0, null);
        g2d.dispose();
        return result;
    }

    //center text
    public int textCenter(Character xOrY, String text, Font font, BufferedImage background) {
        FontRenderContext fontRenderContext = new FontRenderContext(new AffineTransform(), true, true);

        int center = 0;
        //x
        if (xOrY.equals('x')) {
            int xCenterText = (int) Math.round(font.getStringBounds(text, fontRenderContext).getWidth() / 2);
            int xCenterBackground = background.getWidth() / 2;
            center = xCenterBackground - xCenterText;
        }
        //y
        else if (xOrY.equals('y')) {
            int yCenterText = (int) Math.round(font.getStringBounds(text, fontRenderContext).getHeight() / 2);
            int yCenterBackground = background.getHeight() / 2;
            center = yCenterBackground - yCenterText;
        }
        return center;
    }

    //center image
    public int imageCenter(Character xOrY, BufferedImage image, BufferedImage background) {
        int center = 0;
        //x
        if (xOrY.equals('x')) {
            int xCenterText = Math.round(image.getWidth() / 2);
            int xCenterBackground = background.getWidth() / 2;
            center = xCenterBackground - xCenterText;
        }
        //y
        else if (xOrY.equals('y')) {
            int yCenterText = Math.round(image.getHeight() / 2);
            int yCenterBackground = background.getHeight() / 2;
            center = yCenterBackground - yCenterText;
        }
        return center;
    }

    //resize Buffered images
    public BufferedImage resizeSquaredImage(BufferedImage image, float amount) {
        int endSize = Math.round(image.getWidth() * amount);

        Image temp = image.getScaledInstance(endSize, endSize, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(endSize, endSize, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(temp, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }


    //rendering hints
    public void applyQualityRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }
    // Image to BufferedImage
    public BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
}
