package com.myra.dev.marian.listeners.leveling;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.database.allMethods.GetMember;
import com.myra.dev.marian.management.listeners.ListenerContext;
import com.myra.dev.marian.utilities.Graphic;
import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import org.bson.Document;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

public class Leveling {

    public void levelUp(ListenerContext ctx, GetMember db) throws Exception {
        int newLevel = level(db.getInteger("xp") + getXpFromMessage(ctx.getMessage())); // Get new level
        // Current level is equal to new one
        if (db.getInteger("level") == newLevel) return;
// Level up
        db.setInteger("level", newLevel); // Update level in database
// Level up image
        Graphic graphic = Graphic.getInstance(); // Get graphics
        final BufferedImage background = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("levelUp.png")); // Get level up image background
        BufferedImage avatar = graphic.getAvatar(ctx.getMessage().getAuthor().getEffectiveAvatarUrl()); // Get avatar as a BufferedImage

        avatar = graphic.resizeImage(avatar, 85, 85); // Resize avatar
        // Graphics
        Graphics graphics = background.getGraphics(); // Create graphics object from background
        Graphics2D graphics2D = (Graphics2D) graphics; // Create graphics2D object from background

        graphic.enableAntiAliasing(graphics); //Enable anti aliasing
        // Load font
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("default.ttf"); // Get font as InputStream
        Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream); // Create font
        font = font.deriveFont(45f); // Set font size
        graphics.setFont(font); // Set font

        // Draw avatar
        graphics2D.drawImage(
                avatar,
                graphic.imageCenter(Graphic.axis.X, avatar, background) - 200,
                graphic.imageCenter(Graphic.axis.Y, avatar, background),
                null);

        // Draw circle around avatar
        graphics2D.setColor(Color.white); // Set circle colour
        graphics2D.setStroke(new BasicStroke(
                2.5f, // Set stroke width
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        ));
        graphics2D.drawOval(
                graphic.imageCenter(Graphic.axis.X, avatar, background) - 200,
                graphic.imageCenter(Graphic.axis.Y, avatar, background),
                avatar.getWidth(), avatar.getHeight()
        );

        // Draw 'level'
        graphics.drawString("level " + newLevel,
                graphic.textCenter(Graphic.axis.X, "level " + newLevel, font, background) - 55,
                graphic.textCenter(Graphic.axis.Y, "level " + newLevel, font, background) + 40
        );

// Send level up message

        // If no level-up message channel is set
        if (new Database(ctx.getGuild()).getNested("leveling").getString("channel").equals("not set")) {
            ctx.getChannel().
                    sendMessage("> **" + ctx.getMessage().getMember().getAsMention() + " reached a new level!**")
                    .addFile(graphic.toInputStream(background), ctx.getMessage().getAuthor().getName().toLowerCase() + "_level_up.png")
                    .queue();
        }
        // Custom channel
        else {
            final String channelId = new Database(ctx.getGuild()).getNested("leveling").getString("channel"); // Get channel id
            // Channel is invalid
            if (ctx.getGuild().getTextChannelById(channelId) == null) {
                Utilities.getUtils().error(ctx.getChannel(), "rank up", "\uD83C\uDF96", "Couldn't send your rank-up image", "The leveling channel is invalid", ctx.getGuild().getIconUrl());
            }
            // Channel is valid
            else {
                ctx.getGuild().getTextChannelById(channelId).
                        sendMessage("> **" + ctx.getMessage().getMember().getAsMention() + " reached a new level!**")
                        .addFile(graphic.toInputStream(background), ctx.getMessage().getAuthor().getName().toLowerCase() + "_level_up.png")
                        .queue();
            }
        }
// Leveling role
        levelingRoles(ctx.getGuild(), ctx.getEvent().getMember(), db); // Check for leveling roles
    }


    public void levelingRoles(Guild guild, Member member, GetMember dbMember) {
        final Document levelingRoles = new Database(guild).getNested("leveling").get("roles", Document.class); // Get leveling roles

        // For each role
        for (String key : levelingRoles.keySet()) {
            final Document levelingRole = levelingRoles.get(key, Document.class); // Get leveling role

            final Role role = guild.getRoleById(levelingRole.getString("role")); // Get leveling role to add
            final String removeRaw = levelingRole.getString("remove"); // Get role to remove

            // Member's level is too low
            if (levelingRole.getInteger("level") > dbMember.getInteger("level")) {
                // Member has this role
                if (member.getRoles().contains(role)) {
                    guild.removeRoleFromMember(member, role).queue(); // Remove role from member
                }
                // Role to remove is set
                if (!removeRaw.equals("not set")) {
                    final Role remove = guild.getRoleById(removeRaw); // Get role to remove
                    guild.addRoleToMember(member, remove).queue(); // Add role to member
                }
            }
            // Member get the roles
            else {
                guild.addRoleToMember(member, role).queue(); // Add role to member

                // Leveling role to remove
                if (!levelingRole.getString("remove").equals("not set")) { // Check if role is set
                    final Role remove = guild.getRoleById(levelingRole.getString("remove")); // Get role
                    guild.removeRoleFromMember(member, remove).queue(); // Remove role from member
                }
            }
        }
    }

    //return xp
    public int getXpFromMessage(Message rawMessage) {
        //return 0 if the author is a bot
        if (rawMessage.getAuthor().isBot()) return 0;
        //define variable
        String stringMessage = rawMessage.getContentDisplay();
        //return '1' or '2' random
        Random random = new Random();
        int oneOrTwo = random.nextInt(3 - 1) + 1;
        //remove quoted message
        if (stringMessage.startsWith("> ") && stringMessage.contains("\n")) {
            //split message into paragraphs
            String[] paragraphs = stringMessage.split("\n");
            //remove all paragraphs, which aren't quotes
            for (String paragraph : paragraphs) {
                if (paragraph.startsWith("> ")) {
                    stringMessage = stringMessage.replace(paragraph, "");
                }
            }
        }
        //if contains link
        String[] eachWord = rawMessage.getContentRaw().split("\\s+");
        for (String word : eachWord) {
            //remove all links
            if (word.startsWith("http") || word.startsWith("www")) {
                stringMessage = stringMessage.replace(word, "");
            }
        }
        //convert message to character array
        char[] msg = stringMessage.toCharArray();
        //calculate the xp for the message
        return msg.length / 20 + oneOrTwo;
    }

    //return level
    public int level(int xp) {
        //parabola
        double dividedNumber = xp / 5;
        double exactLevel = Math.sqrt(dividedNumber);
        //round
        return (int) Math.round(exactLevel);
    }

    public Integer xpFromLevel(int level) {
        //parabola
        double squaredNumber = Math.pow(level, 2);
        double exactXp = squaredNumber * 5;
        //round
        return (int) Math.round(exactXp);
    }

    //return missing xp
    public int requiredXpForNextLevel(Guild guild, Member member) {
        int currentLevel = new Database(guild).getMembers().getMember(member).getLevel();
        //define variable
        double xp;
        //parabola
        double squaredNumber = Math.pow(currentLevel + 1, 2);
        double exactXp = squaredNumber * 5;
        //round off
        DecimalFormat f = new DecimalFormat("###");
        xp = Double.parseDouble(f.format(exactXp));
        //round down number
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        //convert to int and remove the '.0'
        return Integer.parseInt(String.valueOf(xp).replace(".0", ""));
    }
}
