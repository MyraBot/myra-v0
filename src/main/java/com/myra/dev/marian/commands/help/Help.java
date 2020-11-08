package com.myra.dev.marian.commands.help;

import com.myra.dev.marian.Bot;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.CommandEmbeds;
import com.myra.dev.marian.utilities.MessageReaction;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.management.Events;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.Arrays;

@CommandSubscribe(
        name = "help",
        aliases = {"help me"}
)
public class Help extends Events implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        //check for no arguments
        if (ctx.getArguments().length != 0) return;
        Utilities utilities = Utilities.getUtils();
        //embed
        EmbedBuilder help = new EmbedBuilder()
                .setAuthor("help", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(Utilities.getUtils().blue)
                .setThumbnail(ctx.getEvent().getJDA().getSelfUser().getEffectiveAvatarUrl())
                .setDescription(ctx.getEvent().getJDA().getSelfUser().getName() + " is a multi-purpose bot featuring moderation, music, welcoming and much more!\n" +
                        "If you found a bug please report it in " + utilities.hyperlink("my Discord server", "https://discord.gg/nG4uKuB") + " or write me (" + ctx.getEvent().getJDA().getUserById(Bot.marian).getAsTag() + ") a direct message. For suggestions join the server as well!\n" +
                        "A moderator role must have `View Audit Log` permission to use the moderation commands. To see all available commands type in `" + ctx.getPrefix() + "commands`")
                .addField("**\u2709\uFE0F │ invite**", utilities.hyperlink("Invite ", "https://discord.gg/nG4uKuB") + ctx.getEvent().getJDA().getSelfUser().getName() + " to your server", true)
                .addField("**\u26A0\uFE0F │ support**", utilities.hyperlink("Report ", "https://discord.gg/nG4uKuB") + " bugs and get " + utilities.hyperlink("help ", "https://discord.gg/nG4uKuB"), true);
        Message message = ctx.getChannel().sendMessage(help.build()).complete();
        // Add reactions
        message.addReaction("\u2709\uFE0F").queue();
        message.addReaction("\u26A0\uFE0F").queue();

        MessageReaction.add("help", message.getId(), Arrays.asList("\u2709\uFE0F", "\u26A0\uFE0F"), ctx.getChannel(), ctx.getAuthor(), true);
    }

    //reactions
    @Override
    public void guildMessageReactionAddEvent(GuildMessageReactionAddEvent event) throws Exception {
        // If reaction was added on the wrong message return
        if (!MessageReaction.check(event, "help")) return;

        CommandEmbeds embed = new CommandEmbeds(event.getGuild(), event.getJDA(), event.getUser(), new Database(event.getGuild()).get("prefix"));
        //invite bot
        if (event.getReactionEmote().getEmoji().equals("\u2709\uFE0F") && !event.getMember().getUser().isBot()) {
            event.getChannel().editMessageById(event.getMessageId(), embed.inviteJda().build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
        //support server
        if (event.getReactionEmote().getEmoji().equals("\u26A0\uFE0F") && !event.getMember().getUser().isBot()) {
            event.getChannel().editMessageById(event.getMessageId(), embed.supportServer().build()).queue();
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
        }
    }
}

