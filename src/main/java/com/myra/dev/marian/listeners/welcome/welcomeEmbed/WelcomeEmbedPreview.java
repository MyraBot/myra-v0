package com.myra.dev.marian.listeners.welcome.welcomeEmbed;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import com.myra.dev.marian.utilities.management.commands.CommandContext;

import java.awt.*;
import java.time.Instant;

@CommandSubscribe(
        name = "welcome embed preview"
)
public class WelcomeEmbedPreview implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        Database db = new Database(ctx.getGuild());
        //get variables
        String welcomeColour = db.getNested("welcome").get("welcomeColour");
        String welcomeEmbedMessage = db.getNested("welcome").get("welcomeEmbedMessage");
        //build embed
        EmbedBuilder join = new EmbedBuilder()
                .setAuthor("welcome", null, ctx.getGuild().getIconUrl())
                .setColor(Color.decode(welcomeColour))
                .setThumbnail(ctx.getAuthor().getEffectiveAvatarUrl())
                .setDescription(welcomeEmbedMessage.replace("{user}", ctx.getEvent().getMember().getAsMention()).replace("{server}", ctx.getGuild().getName()).replace("{count}", Integer.toString(ctx.getGuild().getMemberCount())))
                .setTimestamp(Instant.now());
        ctx.getChannel().sendMessage(join.build()).queue();
    }
}