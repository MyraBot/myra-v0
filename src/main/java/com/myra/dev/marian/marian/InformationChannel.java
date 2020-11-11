package com.myra.dev.marian.marian;

import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;

import java.io.File;

@CommandSubscribe(
        name = "MDinformation"
)
public class InformationChannel implements Command {

    @Override
    public void execute(CommandContext ctx) throws Exception {
        embeds embed = new embeds();

        ctx.getChannel().editMessageById("726130459079213138", embed.welcome(ctx.getGuild().getName(), ctx.getGuild().getIconUrl()).build()).queue();
//            ctx.getChannel().sendMessage(embed.partner().build()).queue();
        ctx.getChannel().editMessageById("726130554721927168", embed.colour(ctx.getAuthor()).build()).queue();
        ctx.getChannel().editMessageById("726130554721927168", embed.leveling(ctx.getAuthor()).build()).queue();
//        ctx.getChannel().editMessageById("726130588184084490", embed.designer().build()).queue();
        ctx.getChannel().editMessageById("726130619653816381", embed.socialMedia().build()).queue();
        ctx.getChannel().editMessageById("726130651593441400", embed.botChannels().build()).queue();
        ctx.getChannel().editMessageById("726130652365324360", embed.packChannels().build()).queue();
        ctx.getChannel().editMessageById("726130523998781513", embed.colour(ctx.getAuthor()).build()).queue();
    }
}
