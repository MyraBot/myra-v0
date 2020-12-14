package com.myra.dev.marian.commands.moderation;


import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;

@CommandSubscribe(
        name = "kick",
        aliases = {"kek"},
        requires = Permissions.MODERATOR
)
public class Kick implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Get utilities
        Utilities utilities = Utilities.getUtils();
        //command usage
        if (ctx.getArguments().length == 0) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("kick", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + ctx.getPrefix() + "kick <user> <reason>`", "\uD83D\uDCE4 │ kick a specific member", false);
            ctx.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        /**
         * Kick user
         */
        // Get reason
        String reason = "";
        if (ctx.getArguments().length > 1) {
            for (int i = 1; i < ctx.getArguments().length; i++) {
                reason += ctx.getArguments()[i] + " ";
            }
            //remove last space
            reason = reason.substring(0, reason.length() - 1);
        }
        //get user
        User user = utilities.getModifiedMember(ctx.getEvent(), ctx.getArguments()[0], "kick", "\uD83D\uDCE4");
        if (user == null) return;
        //guild message
        EmbedBuilder guildMessage = new EmbedBuilder()
                .setAuthor(user.getAsTag() + " got kicked", null, user.getEffectiveAvatarUrl())
                .setColor(Utilities.getUtils().red)
                .setDescription(user.getAsMention() + " got kicked from " + ctx.getGuild().getName())
                .setFooter("requested by " + ctx.getAuthor().getAsTag(), ctx.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //direct message
        EmbedBuilder directMessage = new EmbedBuilder()
                .setAuthor("You got kicked from " + ctx.getGuild().getName(), null, ctx.getGuild().getIconUrl())
                .setColor(Utilities.getUtils().red)
                .setDescription("You got kicked from " + ctx.getGuild().getName())
                .setFooter("requested by " + ctx.getAuthor().getAsTag(), ctx.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //kick with no reason
        if (ctx.getArguments().length == 1) {
            guildMessage.addField("\uD83D\uDCC4 │ no reason", "there was no reason given", false);
            directMessage.addField("\uD83D\uDCC4 │ no reason", "there was no reason given", false);
        }
        //kick with reason
        else {
            guildMessage.addField("\uD83D\uDCC4 │ reason:", reason, false);
            directMessage.addField("\uD83D\uDCC4 │ reason:", reason, false);
        }
        //send messages
        ctx.getChannel().sendMessage(guildMessage.build()).queue();
        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(directMessage.build()).queue();
        });
        //kick with no reason
        if (ctx.getArguments().length == 1) {
            ctx.getGuild().getMember(user).kick().queue();
        }
        //kick with reason
        else {
            ctx.getGuild().getMember(user).kick(reason).queue();
        }
    }
}
