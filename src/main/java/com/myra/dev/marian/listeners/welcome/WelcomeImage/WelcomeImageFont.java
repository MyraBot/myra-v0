package com.myra.dev.marian.listeners.welcome.WelcomeImage;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.MessageReaction;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.Arrays;

@CommandSubscribe(
        name = "welcome image font",
        requires = "administrator"
)
public class WelcomeImageFont extends Events implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        //change font
        EmbedBuilder fontSelection = new EmbedBuilder()
                .setAuthor("welcome image font", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(utilities.blue)
                .addField("fonts",
                        "1\uFE0F\u20E3 default \n" +
                                "2\uFE0F\u20E3 modern \n" +
                                "3\uFE0F\u20E3 handwritten",
                        false);
        Message message = ctx.getChannel().sendMessage(fontSelection.build()).complete();
        //add reactions
        message.addReaction("1\uFE0F\u20E3").queue();
        message.addReaction("2\uFE0F\u20E3").queue();
        message.addReaction("3\uFE0F\u20E3").queue();

        MessageReaction.add("welcomeImageFont", message.getId(), Arrays.asList("1\uFE0F\u20E3", "2\uFE0F\u20E3", "3\uFE0F\u20E3"), ctx.getChannel(), ctx.getAuthor(), true);
    }

    //reaction
    @Override
    public void guildMessageReactionAddEvent(GuildMessageReactionAddEvent event) {
        //missing permissions
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) return;
        //if reaction was added on the wrong message return
        if (!MessageReaction.check(event, "welcomeImageFont")) return;
        // Get database
        Database db = new Database(event.getGuild());
        //fonts
        switch (event.getReaction().getReactionEmote().getEmoji()) {
            case "1\uFE0F\u20E3":
                db.getNested("welcome").set("welcomeImageFont", "default", Manager.type.STRING);
                Manager.getUtilities().success(event.getChannel(), "welcome image font", "\uD83D\uDDDB", "Changed welcome image font", "You have changed the font to `default`", event.getUser().getEffectiveAvatarUrl(), false, null);
                break;
            case "2\uFE0F\u20E3":
                db.getNested("welcome").set("welcomeImageFont", "modern", Manager.type.STRING);
                Manager.getUtilities().success(event.getChannel(), "welcome image font", "\uD83D\uDDDB", "Changed welcome image font", "You have changed the font to `modern`", event.getUser().getEffectiveAvatarUrl(), false, null);
                break;
            case "3\uFE0F\u20E3":
                db.getNested("welcome").set("welcomeImageFont", "handwritten", Manager.type.STRING);
                Manager.getUtilities().success(event.getChannel(), "welcome image font", "\uD83D\uDDDB", "Changed welcome image font", "You have changed the font to `handwritten`", event.getUser().getEffectiveAvatarUrl(), false, null);
                break;
        }
    }
}
