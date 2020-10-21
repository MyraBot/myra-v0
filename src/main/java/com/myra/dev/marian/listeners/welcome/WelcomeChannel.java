package com.myra.dev.marian.listeners.welcome;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Return;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "welcome channel"
)
public class WelcomeChannel implements Command {


    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        Database db = new Database(event.getGuild());
        //missing permissions
        if (!Permissions.isAdministrator(event.getMember())) return;
        String[] sentMessage = event.getMessage().getContentRaw().split("\\s+");
         //get channel
        if (new Return().textChannel(event, sentMessage, 2, "welcome channel", "\uD83D\uDCC1") == null) return;
        TextChannel channel = new Return().textChannel(event, sentMessage, 2, "welcome channel", "\uD83D\uDCC1");
        //get current notification channel
        String currentChannelId = db.getNested("welcome").get("welcomeChannel");
        //remove notification channel
        if (currentChannelId.equals(channel.getId())) {
            //remove channel id
            db.getNested("welcome").set("welcomeChannel", "not set");
            //success
            Manager.getUtilities().success(event.getChannel(), "welcome channel", "\uD83D\uDCC1", "Welcome channel removed", "Welcome are no longer send in " + channel.getAsMention(), event.getAuthor().getEffectiveAvatarUrl(), false, null);
            return;
        }
        //Database
        db.getNested("welcome").set("welcomeChannel", channel.getId());
        //success message
        Manager.getUtilities().success(event.getChannel(), "welcome channel", "\uD83D\uDCC1", "Welcome channel changed", "Welcome messages are now send in " + channel.getAsMention(), event.getAuthor().getEffectiveAvatarUrl(), false, null);
        //success message in welcome  channel
        EmbedBuilder logChannelInfo = new EmbedBuilder()
                .setAuthor("welcome channel", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().blue)
                .addField("\uD83D\uDCC1 │ welcome channel changed", "welcome actions are now send in here", false);
        channel.sendMessage(logChannelInfo.build()).queue();
    }
}
