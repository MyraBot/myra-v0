package com.myra.dev.marian.marian;

import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;

import java.io.File;

@CommandSubscribe(
        name = "MDinformation"
)
public class MariansDiscordInformation implements Command {

    @Override
    public void execute(CommandContext ctx) throws Exception {
        embeds embed = new embeds();
        File welcome = new File("D:/Projektdateien/bot/footage/welcome.jpg");
        File partner = new File("D:/Projektdateien/bot/footage/partner.jpg");
        File shop = new File("D:/Projektdateien/bot/footage/shop.jpg");
        File levelingRoles = new File("D:/Projektdateien/bot/footage/leveling roles.jpg");
        File designerRanks = new File("D:/Projektdateien/bot/footage/designer ranks.jpg");
        File reactionRoles = new File("D:/Projektdateien/bot/footage/reaction roles.jpg");
        File socialMedia = new File("D:/Projektdateien/bot/footage/social media.jpg");


//            ctx.getChannel().sendFile(welcome).queue();
        ctx.getChannel().editMessageById("726130459079213138", embed.welcome(ctx.getGuild().getName(), ctx.getGuild().getIconUrl()).build()).queue();
//            ctx.getChannel().sendFile(partner).queue();
//            ctx.getChannel().sendMessage(embed.partner().build()).queue();
//            ctx.getChannel().sendFile(shop).queue();
        ctx.getChannel().editMessageById("726130554721927168", embed.colour().build()).queue();
//            ctx.getChannel().sendFile(levelingRoles).queue();
        ctx.getChannel().editMessageById("726130554721927168", embed.leveling().build()).queue();
//            ctx.getChannel().sendFile(designerRanks).queue();
//        ctx.getChannel().editMessageById("726130588184084490", embed.designer().build()).queue();
//            ctx.getChannel().sendFile(socialMedia).queue();
        ctx.getChannel().editMessageById("726130619653816381", embed.socialMedia().build()).queue();
//            ctx.getChannel().sendFile(reactionRoles).queue();
        ctx.getChannel().editMessageById("726130651593441400", embed.botChannels().build()).queue();
        ctx.getChannel().editMessageById("726130652365324360", embed.packChannels().build()).queue();
    }
}
