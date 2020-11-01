package com.myra.dev.marian.commands.leveling;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.database.allMethods.GetMember;
import com.myra.dev.marian.utilities.Graphic;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

@CommandSubscribe(
        command = "rank",
        name = "rank"
)
public class Rank implements Command {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void execute(CommandContext ctx) throws Exception {
        Utilities utilities = Manager.getUtilities();
        // Usage
        if (ctx.getArguments().length > 1) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("rank", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + ctx.getPrefix() + "rank <user>`", "\uD83C\uDFC5 │ Shows the rank of a user", false);
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
// Show rank
        // Get self user
        Member member = ctx.getMember();
        // If user is given
        if (ctx.getArguments().length == 1) {
            User user = utilities.getUser(ctx.getEvent(), ctx.getArguments()[0], "rank", "\uD83C\uDFC5");
            if (user == null) return;
            //check if user isn't in this guild
            if (ctx.getGuild().getMember(user) == null) {
                utilities.error(ctx.getChannel(), "rank", "\uD83C\uDFC5", "No member found", "the given user isn't on this server", ctx.getAuthor().getEffectiveAvatarUrl());
                return;
            }
            member = ctx.getGuild().getMember(user);
        }
        //if member is bot
        if (member.getUser().isBot()) {
            Manager.getUtilities().error(ctx.getChannel(), "rank", "\uD83C\uDFC5", member.getEffectiveName() + " is a bot", "Bots aren't allowed to participate in the ranking competition", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //get variables
        GetMember getMember = new Database(ctx.getGuild()).getMembers().getMember(member);

        String level = String.valueOf(getMember.getLevel());
        int xp = getMember.getXp();
        int requiredXpForNextLevel = Manager.getLeveling().requiredXpForNextLevel(ctx.getGuild(), member);
        int rank = getMember.getRank();
        // Get rank background
        BufferedImage background;
        String backgroundUrl = getMember.getRankBackground();
        // No background set
        if (backgroundUrl.equals("default")) {
            background = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("defaultRank.png"));
        }
        // Custom background
        else {
            background = ImageIO.read(new URL(backgroundUrl));
        }

        Graphic graphic = new Graphic();

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
/*        //draw box over background
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(5, 5, 340, 90, 15, 15);
        graphics2D.setColor(new Color(200, 255, 255, 50));
        graphics2D.fill(roundedRectangle);*/
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
// Level
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
// Xp
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
// Rank
        //adjust font size
        font = font.deriveFont(15f);
        graphics.setFont(font);
        //draw 'xp'
        graphics.drawString("rank:",
                graphic.textCenter('x', "rank:", font, background) + 40,
                graphic.textCenter('y', "rank:", font, background) + 25
        );
        //draw xp
        graphics.drawString(String.valueOf(rank),
                graphic.textCenter('x', "rank:", font, background) + 85,
                graphic.textCenter('y', "rank:", font, background) + 25
        );
// Send rank card
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.flush();
        outStream.close();
        ImageIO.write(background, "png", outStream);
        ctx.getChannel().sendMessage("> " + member.getAsMention() + "**, you're level " + level + "**").queue();
        ctx.getChannel().sendFile(
                new ByteArrayInputStream(outStream.toByteArray()),
                member.getUser().getName().toLowerCase() + "_rank.png"
        ).queue();
    }
}
