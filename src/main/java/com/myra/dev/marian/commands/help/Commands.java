package com.myra.dev.marian.commands.help;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.CommandEmbeds;
import com.myra.dev.marian.utilities.MessageReaction;
import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.Arrays;

@CommandSubscribe(
        name = "commands",
        aliases = {"command"}
)
public class Commands extends Events implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        //menu
        Message message = ctx.getChannel().sendMessage(new CommandEmbeds(ctx.getGuild(), ctx.getEvent().getJDA(), ctx.getAuthor(), ctx.getPrefix()).commands().build()).complete();
        //add reactions
        message.addReaction("\uD83C\uDF88").queue();
        message.addReaction("\uD83D\uDD79").queue();
        message.addReaction("\uD83C\uDFC6").queue();
        message.addReaction("\uD83D\uDCB0").queue();
        message.addReaction("\uD83D\uDCFB").queue();
        message.addReaction("\uD83D\uDD28").queue();
        message.addReaction("\uD83D\uDD29").queue();

        MessageReaction.add("commands", message.getId(), Arrays.asList("\uD83C\uDF88", "\uD83D\uDD79", "\uD83C\uDFC6", "\uD83D\uDCB0", "\uD83D\uDCFB", "\uD83D\uDD28", "\uD83D\uDD29"), ctx.getChannel(), ctx.getAuthor(), true);
    }

    @Override
    public void guildMessageReactionAddEvent(GuildMessageReactionAddEvent event) throws Exception {
        //if reaction was added on the wrong message return
        if (!MessageReaction.check(event, "commands")) return;

        // Get Prefix
        final String prefix = new Database(event.getGuild()).get("prefix");
        // Get Embeds
        CommandEmbeds embed = new CommandEmbeds(event.getGuild(), event.getJDA(), event.getUser(), prefix);

        //general commands
        if (event.getReactionEmote().getEmoji().equals("\uD83C\uDF88") && !event.getMember().getUser().isBot()) {
            event.getChannel().editMessageById(event.getMessageId(), embed.general().build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        //fun commands
        if (event.getReactionEmote().getEmoji().equals("\uD83D\uDD79") && !event.getMember().getUser().isBot()) {
            event.getChannel().editMessageById(event.getMessageId(), embed.fun().build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        // Leveling commands
        if (event.getReactionEmote().getEmoji().equals("\uD83C\uDFC6") && !event.getMember().getUser().isBot()) {
            event.getChannel().editMessageById(event.getMessageId(), embed.leveling().build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }

        if (event.getReactionEmote().getEmoji().equals("\uD83D\uDCB0") && !event.getMember().getUser().isBot()) {
            event.getChannel().editMessageById(event.getMessageId(), embed.economy().build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        //music commands
        if (event.getReactionEmote().getEmoji().equals("\uD83D\uDCFB") && !event.getMember().getUser().isBot()) {
            event.getChannel().editMessageById(event.getMessageId(), embed.music().build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        //moderation commands
        if (event.getReactionEmote().getEmoji().equals("\uD83D\uDD28") && !event.getMember().getUser().isBot()) {
            event.getChannel().editMessageById(event.getMessageId(), embed.moderation().build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        //administrator commands
        if (event.getReactionEmote().getEmoji().equals("\uD83D\uDD29") && !event.getMember().getUser().isBot()) {
            event.getChannel().editMessageById(event.getMessageId(), embed.administrator().build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
    }
}