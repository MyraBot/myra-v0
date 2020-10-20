package com.myra.dev.marian.commands.general.information;

import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@CommandSubscribe(
        name = "information bot",
        aliases = {"info bot", "information BOT_NAME", "info BOT_NAME"}
)
public class InformationBot implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        EmbedBuilder bot = new EmbedBuilder()
                .setAuthor(event.getJDA().getSelfUser().getName(), null, event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().blue)
                .setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                .addField("\uD83D\uDD0C │ name", event.getJDA().getSelfUser().getName(), true)
                .addField("\uD83D\uDC51 │ owner", event.getJDA().getUserById("639544573114187797").getAsTag(), true)
                .addBlankField(true)
                .addField("\uD83C\uDF10 │ servers", Integer.toString(event.getJDA().getGuilds().size()), true)
                .addField("\uD83D\uDCBB │ language", "Java", true)
                .addField("\uD83D\uDCC5 │ joined server", event.getGuild().getSelfMember().getTimeJoined().atZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy , hh:mm")), false)
                .setFooter("\uD83D\uDCC6 │ created at " + event.getJDA().getSelfUser().getTimeCreated().atZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy , hh:mm")));
        event.getChannel().sendMessage(bot.build()).queue();
    }
}