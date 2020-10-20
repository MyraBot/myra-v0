package com.myra.dev.marian.mariansDiscord;

import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;
@CommandSubscribe(
        name = "MDinformation"
)
public class MariansDiscordInformation implements Command{

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        embeds embed = new embeds();
        File welcome = new File("D:/Projektdateien/bot/footage/welcome.jpg");
        File partner = new File("D:/Projektdateien/bot/footage/partner.jpg");
        File shop = new File("D:/Projektdateien/bot/footage/shop.jpg");
        File levelingRoles = new File("D:/Projektdateien/bot/footage/leveling roles.jpg");
        File designerRanks = new File("D:/Projektdateien/bot/footage/designer ranks.jpg");
        File reactionRoles = new File("D:/Projektdateien/bot/footage/reaction roles.jpg");
        File socialMedia = new File("D:/Projektdateien/bot/footage/social media.jpg");


//            event.getChannel().sendFile(welcome).queue();
        event.getChannel().editMessageById("726130459079213138", embed.welcome(event.getGuild().getName(), event.getGuild().getIconUrl()).build()).queue();
//            event.getChannel().sendFile(partner).queue();
//            event.getChannel().sendMessage(embed.partner().build()).queue();
//            event.getChannel().sendFile(shop).queue();
        event.getChannel().editMessageById("726130554721927168", embed.colour().build()).queue();
//            event.getChannel().sendFile(levelingRoles).queue();
        event.getChannel().editMessageById("726130554721927168", embed.leveling().build()).queue();
//            event.getChannel().sendFile(designerRanks).queue();
        event.getChannel().editMessageById("726130588184084490", embed.designer().build()).queue();
//            event.getChannel().sendFile(socialMedia).queue();
        event.getChannel().editMessageById("726130619653816381", embed.socialMedia().build()).queue();
//            event.getChannel().sendFile(reactionRoles).queue();
        event.getChannel().editMessageById("726130651593441400", embed.botChannels().build()).queue();
        event.getChannel().editMessageById("726130652365324360", embed.packChannels().build()).queue();
    }
}
