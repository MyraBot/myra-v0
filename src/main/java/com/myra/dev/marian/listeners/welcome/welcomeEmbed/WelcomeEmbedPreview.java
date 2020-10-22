package com.myra.dev.marian.listeners.welcome.welcomeEmbed;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.Instant;

@CommandSubscribe(
        name = "welcome embed preview"
)
public class WelcomeEmbedPreview implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        Database db = new Database(event.getGuild());
        //get variables
        String welcomeColour = db.getNested("welcome").get("welcomeColour");
        String welcomeEmbedMessage = db.getNested("welcome").get("welcomeEmbedMessage");
        //build embed
        EmbedBuilder join = new EmbedBuilder()
                .setAuthor("welcome", null, event.getGuild().getIconUrl())
                .setColor(Color.decode(welcomeColour))
                .setThumbnail(event.getAuthor().getEffectiveAvatarUrl())
                .setDescription(welcomeEmbedMessage.replace("{user}", event.getMember().getAsMention()).replace("{server}", event.getGuild().getName()).replace("{count}", Integer.toString(event.getGuild().getMemberCount())))
                .setTimestamp(Instant.now());
        event.getChannel().sendMessage(join.build()).queue();
    }
}