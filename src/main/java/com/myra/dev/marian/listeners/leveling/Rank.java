package com.myra.dev.marian.listeners.leveling;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.database.allMethods.GetMember;
import com.myra.dev.marian.utilities.Graphic;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@CommandSubscribe(
        name = "rank"
)
public class Rank implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        Utilities utilities = Manager.getUtilities();
        // Usage
        if (arguments.length > 1) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("rank", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "rank <user>`", "\uD83C\uDFC5 │ Shows the rank of a user", false);
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * show rank
         */
        // Get self user
        Member member = event.getMember();
        // If user is given
        if (arguments.length == 1) {
            User user = utilities.getUser(event, arguments[0], "rank", "\uD83C\uDFC5");
            if (user == null) return;
            //check if user isn't in this guild
            if (event.getGuild().getMember(user) == null) {
                utilities.error(event.getChannel(), "rank", "\uD83C\uDFC5", "No member found", "the given user isn't on this server", event.getAuthor().getEffectiveAvatarUrl());
                return;
            }
            member = event.getGuild().getMember(user);
        }
        //if member is bot
        if (member.getUser().isBot()) {
            Manager.getUtilities().error(event.getChannel(), "rank", "\uD83C\uDFC5", member.getEffectiveName() + " is a bot", "Bots aren't allowed to participate in the ranking competition", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //get variables
        GetMember getMember = new Database(event.getGuild()).getMembers().getMember(member);

        String level = String.valueOf(getMember.getLevel());
        int xp = getMember.getXp();
        int requiredXpForNextLevel = Manager.getLeveling().requiredXpForNextLevel(event.getGuild(), member);
        int rank = getMember.getRank();

        Graphic graphic = new Graphic();

        BufferedImage background = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("defaultRank.png"));
        BufferedImage avatar = graphic.getAvatar(member.getUser().getEffectiveAvatarUrl());
        //resize avatar
        avatar = graphic.resizeSquaredImage(avatar, 0.5f);
        //graphics
        Graphics graphics = background.getGraphics();
        Graphics2D graphics2D = (Graphics2D) graphics;
        //enable anti aliasing
        graphic.enableAntiAliasing(graphics);
        //load font
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("default.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        //draw box over background
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(5, 5, 340, 90, 15, 15);
        graphics2D.setColor(new Color(200, 255, 255, 100));
        graphics2D.fill(roundedRectangle);
        //draw avatar
        graphics2D.drawImage(
                avatar,
                graphic.imageCenter('x', avatar, background) - 125,
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
                graphic.imageCenter('x', avatar, background) - 125,
                graphic.imageCenter('y', avatar, background),
                avatar.getWidth(), avatar.getHeight()
        );
        /**
         * level
         */
        //adjust font size
        font = font.deriveFont(15f);
        graphics.setFont(font);
        //draw 'level'
        graphics.drawString("level:",
                graphic.textCenter('x', level, font, background) - 55,
                graphic.textCenter('y', level, font, background) - 15
        );
        //adjust font size
        font = font.deriveFont(50f);
        graphics.setFont(font);
        //draw level
        graphics.drawString(level,
                graphic.textCenter('x', level, font, background) - 40,
                graphic.textCenter('y', level, font, background) + 50
        );
        /**
         * xp
         */
        //adjust font size
        font = font.deriveFont(15f);
        graphics.setFont(font);
        //draw 'xp'
        graphics.drawString("xp:",
                graphic.textCenter('x', "xp:", font, background) + 30,
                graphic.textCenter('y', "xp:", font, background)
        );
        //draw xp
        graphics.drawString(xp + " / " + requiredXpForNextLevel,
                graphic.textCenter('x', "xp:", font, background) + 75,
                graphic.textCenter('y', "xp:", font, background)
        );
        /**
         * rank
         */
        //adjust font size
        font = font.deriveFont(15f);
        graphics.setFont(font);
        //draw 'xp'
        graphics.drawString("rank:",
                graphic.textCenter('x', "rank:", font, background) + 40, //+10 habe ich gemacht
                graphic.textCenter('y', "rank:", font, background) + 25
        );
        //draw xp
        graphics.drawString(String.valueOf(rank),
                graphic.textCenter('x', "rank:", font, background) + 85,
                graphic.textCenter('y', "rank:", font, background) + 25
        );
        /**
         * send message
         */
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ImageIO.write(background, "png", outStream);
        event.getChannel().sendMessage(member.getAsMention() + ", you are level " + level + " and ur rank thing is " + getMember.getRank()).queue();
        event.getChannel().sendFile(
                new ByteArrayInputStream(outStream.toByteArray()),
                member.getUser().getName().toLowerCase() + "_rank.png"
        ).queue();
    }
}
