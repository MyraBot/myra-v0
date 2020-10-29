package com.myra.dev.marian.commands.general.information;

import com.myra.dev.marian.utilities.MessageReaction;
import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@CommandSubscribe(
        command = "information",
        name = "information server",
        aliases = {"info server", "information guild", "info guild", "information GUILD_NAME", "info GUILD_NAME"}
)
public class InformationServer extends Events implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        //servers information
        EmbedBuilder server = new EmbedBuilder()
                .setAuthor(ctx.getGuild().getName(), null, ctx.getGuild().getIconUrl())
                .setColor(Manager.getUtilities().blue)
                .setThumbnail(ctx.getGuild().getIconUrl())
                .addField("\uD83D\uDC51 │ owner", ctx.getGuild().getOwner().getAsMention(), true)
                .addField("\uD83C\uDF9F │ server id", ctx.getGuild().getId(), true)
                .addBlankField(true)
                .addField("\uD83D\uDE80 │ boosts", "level " + ctx.getGuild().getBoostTier().toString().replace("NONE", "0").replace("TIER_", "") + " (" + ctx.getGuild().getBoostCount() + " boosts)", true)
                .addField("\uD83E\uDDEE │ member count", Integer.toString(ctx.getGuild().getMemberCount()), true)
                .addBlankField(true)
                .addField("\uD83D\uDCC6 │ created at", ctx.getGuild().getTimeCreated().getDayOfMonth() + " " + ctx.getGuild().getTimeCreated().getMonth() + " " + ctx.getGuild().getTimeCreated().getYear(), true);
        Message message = ctx.getChannel().sendMessage(server.build()).complete();
        //reactions
        message.addReaction("\uD83D\uDCDC").queue();

        MessageReaction.add("informationServer", message.getId(), ctx.getChannel(), ctx.getAuthor(), true);
    }


    @Override
    public void guildMessageReactionAddEvent(GuildMessageReactionAddEvent event) {
        //if reaction was added on the wrong message return
        if (!MessageReaction.check(event, "informationServer")) return;

        if (event.getReactionEmote().getEmoji().equals("\uD83D\uDCDC")) {
            //remove reaction
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
            //servers information
            EmbedBuilder server = new EmbedBuilder()
                    .setAuthor(event.getGuild().getName(), null, event.getGuild().getIconUrl())
                    .setColor(Manager.getUtilities().blue)
                    .setThumbnail(event.getGuild().getIconUrl())
                    .addField("\uD83D\uDC51 │ owner", event.getGuild().getOwner().getAsMention(), true)
                    .addField("\uD83C\uDF9F │ server id", event.getGuild().getId(), true)
                    .addBlankField(true)
                    .addField("\uD83D\uDE80 │ boosts", "level " + event.getGuild().getBoostTier().toString().replace("NONE", "0") + " (" + event.getGuild().getBoostCount() + " boosts)", true)
                    .addField("\uD83E\uDDEE │ member count", Integer.toString(event.getGuild().getMemberCount()), true)
                    .addBlankField(true)
                    .addField("\uD83D\uDCC6 │ created at", event.getGuild().getTimeCreated().atZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy , hh:mm")), true)


                    .addField("\uD83D\uDDFA │ region", event.getGuild().getRegionRaw(), false)
                    .addField("\uD83D\uDDD2 │ details",
                            "\uD83D\uDCC1 │ channels: `" + event.getGuild().getChannels().size() + "`" +
                                    "\n\uD83D\uDC65 │ roles: `" + event.getGuild().getRoles().size() + "`" +
                                    "\n\uD83E\uDD2A │ emojis: `" + event.getGuild().getRoles().size() + "`",
                            true)
                    .addField("\uD83D\uDDD2 │ moderation",
                            "\u2705 │ verification level: `" + event.getGuild().getVerificationLevel().toString().toLowerCase() + "`" +
                                    "\n\uD83D\uDCFA │ media content filter: `" + event.getGuild().getExplicitContentLevel().toString().toLowerCase() + "`",
                            true);
            event.getChannel().editMessageById(event.getMessageId(), server.build()).queue();
        }
    }
}

