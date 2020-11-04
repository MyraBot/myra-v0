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
        message.addReaction("\uD83D\uDCD6").queue(); // Help
        message.addReaction("\uD83C\uDF88").queue(); // General
        message.addReaction("\uD83D\uDD79").queue(); // Fun
        message.addReaction("\uD83C\uDFC6").queue(); // Leveling
        message.addReaction("\uD83D\uDCB0").queue(); // Economy
        message.addReaction("\uD83D\uDCFB").queue(); // Music
        message.addReaction("\uD83D\uDD28").queue(); // Moderation
        message.addReaction("\uD83D\uDD29").queue(); // Administrator

        MessageReaction.add("commands", message.getId(), Arrays.asList("\uD83D\uDCD6", "\uD83C\uDF88", "\uD83D\uDD79", "\uD83C\uDFC6", "\uD83D\uDCB0", "\uD83D\uDCFB", "\uD83D\uDD28", "\uD83D\uDD29"), ctx.getChannel(), ctx.getAuthor(), true);
    }

    @Override
    public void guildMessageReactionAddEvent(GuildMessageReactionAddEvent event) throws Exception {
        //if reaction was added on the wrong message return
        if (!MessageReaction.check(event, "commands")) return;

        // Get Prefix
        final String prefix = new Database(event.getGuild()).get("prefix");
        // Get Embeds
        CommandEmbeds embed = new CommandEmbeds(event.getGuild(), event.getJDA(), event.getUser(), prefix);

        // Help commands
        if (event.getReactionEmote().getEmoji().equals("\uD83D\uDCD6") && !event.getMember().getUser().isBot()) {
            event.getChannel().editMessageById(event.getMessageId(), embed.help().build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        // General commands
        else if (event.getReactionEmote().getEmoji().equals("\uD83C\uDF88") && !event.getMember().getUser().isBot()) {
            event.getChannel().editMessageById(event.getMessageId(), embed.general().build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        // Fun commands
        else if (event.getReactionEmote().getEmoji().equals("\uD83D\uDD79") && !event.getMember().getUser().isBot()) {
            event.getChannel().editMessageById(event.getMessageId(), embed.fun().build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        // Leveling commands
        else if (event.getReactionEmote().getEmoji().equals("\uD83C\uDFC6") && !event.getMember().getUser().isBot()) {
            event.getChannel().editMessageById(event.getMessageId(), embed.leveling().build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        // Economy commands
        else if (event.getReactionEmote().getEmoji().equals("\uD83D\uDCB0") && !event.getMember().getUser().isBot()) {
            event.getChannel().editMessageById(event.getMessageId(), embed.economy().build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        // Music commands
        else if (event.getReactionEmote().getEmoji().equals("\uD83D\uDCFB") && !event.getMember().getUser().isBot()) {
            event.getChannel().editMessageById(event.getMessageId(), embed.music().build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        // Moderation commands
        else if (event.getReactionEmote().getEmoji().equals("\uD83D\uDD28") && !event.getMember().getUser().isBot()) {
            event.getChannel().editMessageById(event.getMessageId(), embed.moderation().build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        // Administrator commands
        else if (event.getReactionEmote().getEmoji().equals("\uD83D\uDD29") && !event.getMember().getUser().isBot()) {
            event.getChannel().editMessageById(event.getMessageId(), embed.administrator().build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
    }
}