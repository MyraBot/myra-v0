package com.myra.dev.marian.commands.moderation.mute;

import com.mongodb.client.MongoCollection;
import com.myra.dev.marian.database.MongoDb;
import com.myra.dev.marian.database.allMethods.Database;

import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import org.bson.Document;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@CommandSubscribe(
        name = "tempmute",
        requires = Permissions.MODERATOR
)
public class Tempmute  implements Command {
    //Get database
    private final MongoDb mongoDb = MongoDb.getInstance();

    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Get utilities
        Utilities utilities = Utilities.getUtils();
        //command usage
        if (ctx.getArguments().length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("tempmute", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("\uD83D\uDD07 │ tempmute a specific member", "`" + ctx.getPrefix() + "tempmute <user> <duration><time unit> <reason>`", true)
                    .setFooter("Accepted time units: seconds, minutes, hours, days");
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * tempmute
         */
        //get reason
        String reason = "";
        if (ctx.getArguments().length > 2) {
            for (int i = 2; i < ctx.getArguments().length; i++) {
                reason += ctx.getArguments()[i] + " ";
            }
            //remove last space
            reason = reason.substring(0, reason.length() - 1);
        }
        //get user
        User user = utilities.getModifiedUser(ctx.getEvent(), ctx.getArguments()[0], "tempmute", "\uD83D\uDD07");
        if (user == null) return;
        //get mute role id
        String muteRoleId = new Database(ctx.getGuild()).get("muteRole");
        //no mute role set
        if (muteRoleId.equals("not set")) {
            utilities.error(ctx.getChannel(), "tempmute", "\uD83D\uDD07", "You didn't specify a mute role", "To indicate a mute role, type in `" + ctx.getPrefix() + "mute role <role>`", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //if the string is not [NumberLetters]
        if (!ctx.getArguments()[1].matches("[0-9]+[a-zA-z]+")) {
            utilities.error(ctx.getChannel(), "tempmute", "\uD83D\uDD07", "Invalid time", "please note: `<time><time unit>`", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //return duration as a list
        List durationList = utilities.getDuration(ctx.getArguments()[1]);
        String duration = durationList.get(0).toString();
        long durationInMilliseconds = Long.parseLong(durationList.get(1).toString());
        TimeUnit timeUnit = TimeUnit.valueOf(durationList.get(2).toString());
        //guild message mute
        EmbedBuilder muteGuild = new EmbedBuilder()
                .setAuthor(user.getName() + " got tempmuted", null, user.getEffectiveAvatarUrl())
                .setColor(utilities.red)
                .setDescription("\u23F1\uFE0F │ " + user.getAsMention() + " got muted for **" + duration + " " + timeUnit.toString().toLowerCase() + "**")
                .setFooter("requested by " + ctx.getAuthor().getAsTag(), ctx.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //direct message mute
        EmbedBuilder muteDirectMessage = new EmbedBuilder()
                .setAuthor("You got tempmuted on " + ctx.getGuild().getName(), null, ctx.getGuild().getIconUrl())
                .setColor(utilities.red)
                .setDescription("\u23F1\uFE0F │ You got muted on " + ctx.getGuild().getName() + " for **" + duration + " " + timeUnit.toString().toLowerCase() + "**")
                .setFooter("requested by " + ctx.getEvent().getMember().getUser().getName(), ctx.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //with reason
        if (ctx.getArguments().length > 2) {
            muteGuild.addField("\uD83D\uDCC4 │ reason:", reason, false);
            muteDirectMessage.addField("\uD83D\uDCC4 │ reason:", reason, false);
        }
        //without reason
        else {
            muteGuild.addField("\uD83D\uDCC4 │ no reason", "there was no reason given", false);
            muteDirectMessage.addField("\uD83D\uDCC4 │ no reason", "there was no reason given", false);
        }
        //send message
        ctx.getChannel().sendMessage(muteGuild.build()).queue();
        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(muteDirectMessage.build()).queue();
        });
        //mute
        ctx.getGuild().addRoleToMember(ctx.getGuild().getMember(user), ctx.getGuild().getRoleById(muteRoleId)).queue();
        //create unmute Document
        Document document = createUnmute(user.getId(), ctx.getGuild().getId(), durationInMilliseconds, ctx.getAuthor().getId());
// Unmute
        // Delay
        Utilities.TIMER.schedule(new Runnable() {
            @Override
            public void run() {
                //if member left the server
                if (ctx.getGuild().getMemberById(document.getString("userId")) == null) {
                    //delete document
                    mongoDb.getCollection("unmutes").deleteOne(document);
                }
                //remove role
                ctx.getGuild().removeRoleFromMember(document.getString("userId"), ctx.getGuild().getRoleById(muteRoleId)).queue();
                //send unmute message
                unmuteMessage(user, ctx.getGuild(), ctx.getAuthor());
                //delete document
                mongoDb.getCollection("unmutes").deleteOne(document);
            }
        }, durationInMilliseconds, TimeUnit.MILLISECONDS);
    }


    //create unmute document
    public Document createUnmute(String userId, String guildId, Long durationInMilliseconds, String moderatorId) {

        MongoCollection<Document> guilds = mongoDb.getCollection("unmutes");
        //create Document
        Document docToInsert = new Document()
                .append("userId", userId)
                .append("guildId", guildId)
                .append("unmuteTime", System.currentTimeMillis() + durationInMilliseconds)
                .append("moderatorId", moderatorId);
        guilds.insertOne(docToInsert);

        return docToInsert;
    }

    //unmute message
    private void unmuteMessage(User user, Guild guild, User author) {
        //database
        Database db = new Database(guild);
        //direct message unmute
        EmbedBuilder directMessage = new EmbedBuilder()
                .setAuthor("You got unmuted from " + guild.getName(), null, guild.getIconUrl())
                .setColor(Utilities.getUtils().green)
                .setDescription("You got unmuted from " + guild.getName())
                .setFooter("requested by " + author.getAsTag(), author.getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(directMessage.build()).queue();
        });
        //if no channel is set
        if (db.get("logChannel").equals("not set")) {
            Utilities.getUtils().error(guild.getDefaultChannel(), "tempban", "\u23F1\uFE0F", "No log channel specified", "To set a log channel type in `" + db.get("prefix") + "log channel <channel>`", author.getEffectiveAvatarUrl());
            return;
        }
        //get log channel
        TextChannel textChannel = guild.getTextChannelById(db.get("logChannel"));
        //guild message unmute
        EmbedBuilder guildMessage = new EmbedBuilder()
                .setAuthor(user.getAsTag() + " got unmuted", null, user.getEffectiveAvatarUrl())
                .setColor(Utilities.getUtils().blue)
                .setDescription(user.getAsMention() + " got unmuted")
                .setFooter("requested by " + author.getAsTag(), author.getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        textChannel.sendMessage(guildMessage.build()).queue();
    }

    public void onReady(ReadyEvent event) throws Exception {
        //for each document
        for (Document doc : mongoDb.getCollection("unmutes").find()) {
            //get unmute time
            Long unmuteTime = doc.getLong("unmuteTime");
            //get guild
            Guild guild = event.getJDA().getGuildById(doc.getString("guildId"));
            /**
             * if unmute time is already reached
             */
            if (unmuteTime < System.currentTimeMillis()) {
                //if member left the server
                if (event.getJDA().getGuildById(doc.getString("guildId")).getMemberById(doc.getString("userId")) == null) {
                    //delete document
                    mongoDb.getCollection("unmutes").deleteOne(doc);
                }
                // No mute role set
                if (new Database(guild).get("muteRole").equals("not set")) {
                    // No logging channel set
                    if (new Database(guild).get("logChannel").equals("not set")) {
                        Utilities.getUtils().error(guild.getDefaultChannel(), "tempmute", "\u23F1\uFE0F", "No log channel specified", "To set a log channel type in `" + new Database(guild).get("prefix") + "log channel <channel>`", guild.getIconUrl());
                        Utilities.getUtils().error(guild.getDefaultChannel(), "tempmute", "\uD83D\uDD07", "You didn't specify a mute role", "To indicate a mute role, type in `" + new Database(guild).get("prefix") + "mute role <role>`", guild.getIconUrl());
                        return;
                    }
                    TextChannel logChannel = guild.getTextChannelById((new Database(guild).get("muteRole")));
                    Utilities.getUtils().error(logChannel, "tempmute", "\uD83D\uDD07", "You didn't specify a mute role", "To indicate a mute role, type in `" + new Database(guild).get("prefix") + "mute role <role>`", guild.getIconUrl());
                }
                //remove role
                guild.removeRoleFromMember(doc.getString("userId"), guild.getRoleById(new Database(guild).get("muteRole"))).queue();
                //send unmute message
                unmuteMessage(event.getJDA().getUserById(doc.getString("userId")), guild, event.getJDA().getUserById(doc.getString("moderatorId")));
                //delete document
                mongoDb.getCollection("unmutes").deleteOne(doc);
                continue;
            }
            /**
             * if unmute time isn't already reached
             */
            // Delay
            Utilities.TIMER.schedule(new Runnable() {
                @Override
                public void run() {
                    //if member left the server
                    if (event.getJDA().getGuildById(doc.getString("guildId")).getMemberById(doc.getString("userId")) == null) {
                        //delete document
                        mongoDb.getCollection("unmutes").deleteOne(doc);
                    }
                    //unmute
                    guild.removeRoleFromMember(doc.getString("userId"), guild.getRoleById(new Database(guild).get("muteRole"))).queue();
                    //send unmute message
                    unmuteMessage(event.getJDA().getUserById(doc.getString("userId")), guild, event.getJDA().getUserById(doc.getString("moderatorId")));
                    //delete document
                    mongoDb.getCollection("unmutes").deleteOne(doc);
                }
            }, doc.getLong("unmuteTime") - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }
    }
}