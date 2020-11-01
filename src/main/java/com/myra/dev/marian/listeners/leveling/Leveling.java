package com.myra.dev.marian.listeners.leveling;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.database.allMethods.GetMember;
import com.myra.dev.marian.utilities.Graphic;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Leveling {

    public void levelUp(GuildMessageReceivedEvent event) throws Exception {
        //connect to database
        GetMember getMember = new Database(event.getGuild()).getMembers().getMember(event.getMember());
        //get new level
        int newLevel = level(getMember.getXp() + xp(event.getMessage()));
        //if current level is equal to new one
        if (getMember.getLevel() == newLevel) return;
        //update level in database
        getMember.setLevel(newLevel);
        /**
         * level up image
         */
        Graphic graphic = new Graphic();

        BufferedImage background = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("levelUp.png"));
        BufferedImage avatar = graphic.getAvatar(event.getAuthor().getEffectiveAvatarUrl());
        //resize avatar
        avatar = graphic.resizeSquaredImage(avatar, 0.75f);
        //graphics
        Graphics graphics = background.getGraphics();
        Graphics2D graphics2D = (Graphics2D) graphics;
        //enable anti aliasing
        graphic.enableAntiAliasing(graphics);
        //load font
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("default.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        font = font.deriveFont(45f);
        graphics.setFont(font);
        //draw avatar
        graphics2D.drawImage(
                avatar,
                graphic.imageCenter('x', avatar, background) - 200,
                graphic.imageCenter('y', avatar, background),
                null);

        //draw circle around avatar
        graphics2D.setColor(Color.white);
        graphics2D.setStroke(new BasicStroke(
                2.5f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        ));
        graphics2D.drawOval(
                graphic.imageCenter('x', avatar, background) - 200,
                graphic.imageCenter('y', avatar, background),
                avatar.getWidth(), avatar.getHeight()
        );
        //draw 'level'
        graphics.drawString("level " + newLevel,
                graphic.textCenter('x', "level " + newLevel, font, background) - 55,
                graphic.textCenter('y', "level " + newLevel, font, background) + 40
        );
        /**
         * send message
         */
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.flush();
        outStream.close();
        ImageIO.write(background, "png", outStream);
        event.getChannel().sendMessage("> " + event.getMember().getAsMention() + " **reached a new level!**").queue();
        event.getChannel().sendFile(
                new ByteArrayInputStream(outStream.toByteArray()),
                event.getMember().getUser().getName().toLowerCase() + "_level_up.png"
        ).queue();
        /**
         * Leveling role
         */
        new Database(event.getGuild()).getLeveling().checkForNewOnesOwO(newLevel, event.getMember(), event.getGuild());
    }

    //return xp
    public int xp(Message rawMessage) {
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

    public void xpVoice(GuildVoiceJoinEvent event) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
/*                System.out.println("suppressed " + event.getMember().getVoiceState().isSuppressed());
                System.out.println("deafened " +event.getMember().getVoiceState().isDeafened());
                System.out.println("guild deafened " +event.getMember().getVoiceState().isGuildDeafened());
                System.out.println("guild muted " +event.getMember().getVoiceState().isGuildMuted());
                System.out.println("muted " +event.getMember().getVoiceState().isMuted());
                System.out.println("self deafend " +event.getMember().getVoiceState().isSelfDeafened());
                System.out.println("is self muted " +event.getMember().getVoiceState().isSelfMuted());
                System.out.println("stream " +event.getMember().getVoiceState().isStream());*/
            }
        }, 1000, 5 * 1000);
    }
}
