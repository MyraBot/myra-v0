package com.myra.dev.marian.commands.help;

import com.myra.dev.marian.utilities.CommandEmbeds;
import com.myra.dev.marian.utilities.MessageReaction;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Events;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.Arrays;

@CommandSubscribe(
        name = "commands",
        aliases = {"command"}
)
public class Commands extends Events implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //menu
        Message message = event.getChannel().sendMessage(CommandEmbeds.commands(event.getAuthor().getEffectiveAvatarUrl()).build()).complete();
        //add reactions
        message.addReaction("\uD83C\uDF88").queue();
        message.addReaction("\uD83D\uDD79").queue();
        message.addReaction("\uD83C\uDFC6").queue();
        message.addReaction("\uD83D\uDCFB").queue();
        message.addReaction("\uD83D\uDD28").queue();
        message.addReaction("\uD83D\uDD29").queue();

        MessageReaction.add("commands", message.getId(), event.getChannel(), true);
    }

    @Override
    public void guildMessageReactionAddEvent(GuildMessageReactionAddEvent event) throws Exception {
        //if reaction was added on the wrong message return
        if (MessageReaction.hashMap.get("commands") == null) return;
        if (!Arrays.stream(MessageReaction.hashMap.get("commands").toArray()).anyMatch(event.getMessageId()::equals) || event.getUser().isBot())
            return;

        //remove id from hashmap
        MessageReaction.hashMap.get("commands").remove(event.getMessageId());
        //general commands
        if (event.getReactionEmote().getEmoji().equals("\uD83C\uDF88") && !event.getMember().getUser().isBot()) {
            CommandEmbeds embed = new CommandEmbeds();
            event.getChannel().editMessageById(event.getMessageIdLong(), embed.general(event.getGuild(), event.getUser().getEffectiveAvatarUrl(), event.getJDA().getSelfUser().getName()).build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        //fun commands
        if (event.getReactionEmote().getEmoji().equals("\uD83D\uDD79") && !event.getMember().getUser().isBot()) {
            CommandEmbeds embed = new CommandEmbeds();
            event.getChannel().editMessageById(event.getMessageIdLong(), embed.fun(event.getGuild(), event.getUser().getEffectiveAvatarUrl()).build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        //leveling commands
        if (event.getReactionEmote().getEmoji().equals("\uD83C\uDFC6") && !event.getMember().getUser().isBot()) {
            CommandEmbeds embed = new CommandEmbeds();
            event.getChannel().editMessageById(event.getMessageIdLong(), embed.leveling(event.getGuild(), event.getUser().getEffectiveAvatarUrl()).build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        //music commands
        if (event.getReactionEmote().getEmoji().equals("\uD83D\uDCFB") && !event.getMember().getUser().isBot()) {
            CommandEmbeds embed = new CommandEmbeds();
            event.getChannel().editMessageById(event.getMessageIdLong(), embed.music(event.getGuild(), event.getUser().getEffectiveAvatarUrl()).build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        //moderation commands
        if (event.getReactionEmote().getEmoji().equals("\uD83D\uDD28") && !event.getMember().getUser().isBot()) {
            CommandEmbeds embed = new CommandEmbeds();
            event.getChannel().editMessageById(event.getMessageIdLong(), embed.moderation(event.getGuild(), event.getUser().getEffectiveAvatarUrl()).build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        //administrator commands
        if (event.getReactionEmote().getEmoji().equals("\uD83D\uDD29") && !event.getMember().getUser().isBot()) {
            CommandEmbeds embed = new CommandEmbeds();
            event.getChannel().editMessageById(event.getMessageIdLong(), embed.administrator(event.getGuild(), event.getUser().getEffectiveAvatarUrl()).build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
    }
}