package com.myra.dev.marian.commands.help;

import com.myra.dev.marian.Main;
import com.myra.dev.marian.utilities.*;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.Arrays;
@CommandSubscribe(
        name = "help",
        aliases = {"help me"}
)
public class Help extends Events implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //check for no arguments
        if (arguments.length != 0) return;
        Utilities utilities = Manager.getUtilities();
        //embed
        EmbedBuilder help = new EmbedBuilder()
                .setAuthor("│ help", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().blue)
                .setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                .setDescription(event.getJDA().getSelfUser().getName() + " is a multi-purpose bot featuring moderation, music, welcoming and much more!\n" +
                        "If you found a bug please report it in " + utilities.hyperlink("my Discord server", "https://discord.gg/nG4uKuB") + " or write me (" + event.getJDA().getUserById(Main.marian).getAsTag() + ") a direct message. For suggestions join the server as well!\n" +
                        "A moderator role must have `View Audit Log` permission to use the moderation commands")
                .addField("**\u2709\uFE0F │ invite**", utilities.hyperlink("Invite ", "https://discord.gg/nG4uKuB") + event.getJDA().getSelfUser().getName() + " to your server", true)
                .addField("**\u26A0\uFE0F │ support**", utilities.hyperlink("Report ", "https://discord.gg/nG4uKuB") + " bugs and get " + utilities.hyperlink("help ", "https://discord.gg/nG4uKuB"), true);
        Message message = event.getChannel().sendMessage(help.build()).complete();
        //add reactions
        message.addReaction("\u2709\uFE0F").queue();
        message.addReaction("\u26A0\uFE0F").queue();

        MessageReaction.add("help", message.getId(), event.getChannel(), true);
    }
    //reactions
    @Override
    public void guildMessageReactionAddEvent(GuildMessageReactionAddEvent event) throws Exception {
        //if reaction was added on the wrong message return
        if (MessageReaction.hashMap.get("help") == null) return;
        if (!Arrays.stream(MessageReaction.hashMap.get("help").toArray()).anyMatch(event.getMessageId()::equals) || event.getUser().isBot())
            return;

        //remove id from hashmap
        MessageReaction.hashMap.get("help").remove(event.getMessageId());

        //invite bot
        if (event.getReactionEmote().getEmoji().equals("\u2709\uFE0F") && !event.getMember().getUser().isBot()) {
            CommandEmbeds embed = new CommandEmbeds();
            event.getChannel().editMessageById(event.getMessageIdLong(), embed.inviteJda(event.getJDA(), event.getUser().getEffectiveAvatarUrl()).build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        //support server
        if (event.getReactionEmote().getEmoji().equals("\u26A0\uFE0F") && !event.getMember().getUser().isBot()) {
            CommandEmbeds embed = new CommandEmbeds();
            event.getChannel().editMessageById(event.getMessageIdLong(), embed.supportServer(event.getJDA(), event.getUser().getEffectiveAvatarUrl()).build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
    }
}

