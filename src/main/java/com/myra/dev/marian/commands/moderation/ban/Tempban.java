package com.myra.dev.marian.commands.moderation.ban;

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
        name = "tempban",
        aliases = {"temp ban", "tempbean", "temp bean"},
        requires = Permissions.MODERATOR
)
public class Tempban  implements Command {
    //database
    private static MongoDb mongoDb;

    //set variable
    public static void setDb(MongoDb db) {
        mongoDb = db;
    }

    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Get Utilities
        Utilities utilities = Utilities.getUtils();
        //command usage
        if (ctx.getArguments().length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("tempban", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + ctx.getPrefix() + "tempban <user> <duration><time unit> [reason]`", "\u23F1\uFE0F │ Ban a user for a certain amount of time", false)
                    .setFooter("Accepted time units: seconds, minutes, hours, days");
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * tempban
         */
        //get arguments
        String durationRaw = ctx.getArguments()[1];
        String reason = "";
        if (ctx.getArguments().length > 2) {
            for (int i = 2; i < ctx.getArguments().length; i++) {
                reason += ctx.getArguments()[i] + " ";
            }
            //remove last space
            reason = reason.substring(0, reason.length() - 1);
        }
        //if the duration is not [NumberLetters]
        if (!durationRaw.matches("[0-9]+[a-zA-z]+")) {
            utilities.error(ctx.getChannel(), "tempban", "\u23F1\uFE0F", "Invalid time", "please note: `<time><time unit>`", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //get user
        User user = utilities.getModifiedUser(ctx.getEvent(), ctx.getArguments()[0], "tempban", "\u23F1\uFE0F");
        if (user == null) return;
        //return duration as a list
        List durationList = Utilities.getUtils().getDuration(durationRaw);
        String duration = durationList.get(0).toString();
        long durationInMilliseconds = Long.parseLong(durationList.get(1).toString());
        TimeUnit timeUnit = TimeUnit.valueOf(durationList.get(2).toString());
        //guild message ban
        EmbedBuilder guildMessageBan = new EmbedBuilder()
                .setAuthor(user.getAsTag() + " got temporary banned", null, user.getEffectiveAvatarUrl())
                .setColor(utilities.red)
                .setDescription("\u23F1\uFE0F │ " + user.getAsMention() + " got banned for **" + duration + " " + timeUnit.toString().toLowerCase() + "**")
                .setFooter("requested by " + ctx.getAuthor().getAsTag(), ctx.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //direct message ban
        EmbedBuilder directMessageBan = new EmbedBuilder()
                .setAuthor("You got temporary banned", null, ctx.getGuild().getIconUrl())
                .setColor(Utilities.getUtils().red)
                .setDescription("\u23F1\uFE0F │ You got banned on `" + ctx.getGuild().getName() + "` for **" + duration + " " + timeUnit.toString().toLowerCase() + "**")
                .setFooter("requested by " + ctx.getAuthor().getAsTag(), ctx.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //with reason
        if (reason != null) {
            guildMessageBan.addField("\uD83D\uDCC4 │ reason:", reason, false);
            directMessageBan.addField("\uD83D\uDCC4 │ reason:", reason, false);
        }
        //without reason
        else {
            guildMessageBan.addField("\uD83D\uDCC4 │ no reason", "there was no reason given", false);
            directMessageBan.addField("\uD83D\uDCC4 │ no reason", "there was no reason given", false);
        }
        //send messages
        ctx.getChannel().sendMessage(guildMessageBan.build()).queue();
        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(directMessageBan.build()).queue();
        });
        //with reason
        if (reason != null) {
            ctx.getGuild().getMember(user).ban(7, reason).queue();
        }
        //without reason
        else {
            ctx.getGuild().getMember(user).ban(7).queue();
        }
        //create unban Document
        Document document = createUnban(user.getId(), ctx.getGuild().getId(), durationInMilliseconds, ctx.getAuthor().getId());
        /**
         * unban
         */
        //delay
        Utilities.TIMER.schedule(new Runnable() {
            @Override
            public void run() {
                //unban
                ctx.getGuild().unban(user).queue();
                //send unban message
                unbanMessage(user, ctx.getGuild(), ctx.getAuthor());
                //delete document
                mongoDb.getCollection("unbans").deleteOne(document);
            }
        }, durationInMilliseconds, TimeUnit.MILLISECONDS);
    }

    public Document createUnban(String userId, String guildId, Long durationInMilliseconds, String moderatorId) {

        MongoCollection<Document> guilds = mongoDb.getCollection("unbans");
        //create Document
        Document docToInsert = new Document()
                .append("userId", userId)
                .append("guildId", guildId)
                .append("unbanTime", System.currentTimeMillis() + durationInMilliseconds)
                .append("moderatorId", moderatorId);
        guilds.insertOne(docToInsert);

        return docToInsert;
    }

    public void onReady(ReadyEvent event) {
        try {
            //for each document
            for (Document doc : mongoDb.getCollection("unbans").find()) {
                //get unban time
                Long unbanTime = doc.getLong("unbanTime");
                //get guild
                Guild guild = event.getJDA().getGuildById(doc.getString("guildId"));
                //retrieve bans
                List<Guild.Ban> banList = guild.retrieveBanList().complete();
                //if unban time is already reached
                if (unbanTime < System.currentTimeMillis()) {
                    //if member left the server
                    if (event.getJDA().getGuildById(doc.getString("guildId")).getMemberById(doc.getString("userId")) == null) {
                        //delete document
                        mongoDb.getCollection("unbans").deleteOne(doc);
                    }
                    for (Guild.Ban ban : banList) {
                        if (ban.getUser().getId().equals(doc.getString("userId"))) {
                            //unban
                            guild.unban(event.getJDA().getUserById(doc.getString("userId"))).queue();
                            //send unban message
                            unbanMessage(event.getJDA().getUserById(doc.getString("userId")), guild, event.getJDA().getUserById(doc.getString("moderatorId")));
                            //delete document
                            mongoDb.getCollection("unbans").deleteOne(doc);
                        }
                    }
                    continue;
                }

                /**
                 * if unban time isn't already reached
                 */
                for (Guild.Ban ban : banList) {
                    //if member left the server
                    if (event.getJDA().getGuildById(doc.getString("guildId")).getMemberById(doc.getString("userId")) == null) {
                        //delete document
                        mongoDb.getCollection("unmutes").deleteOne(doc);
                    }
                    if (ban.getUser().getId().equals(doc.getString("userId"))) {
                        //delay
                        Utilities.TIMER.schedule(new Runnable() {
                            @Override
                            public void run() {
                                //unban
                                guild.unban(event.getJDA().getUserById(doc.getString("userId"))).queue();
                                //send unban message
                                unbanMessage(event.getJDA().getUserById(doc.getString("userId")), guild, event.getJDA().getUserById(doc.getString("moderatorId")));
                                //delete document
                                mongoDb.getCollection("unbans").deleteOne(doc);
                            }
                        }, doc.getLong("unbanTime") - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                    }
                }
            }
            //close database

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unbanMessage(User user, Guild guild, User author) {
        //database
        Database db = new Database(guild);
        //direct message
        EmbedBuilder directMessage = new EmbedBuilder()
                .setAuthor("│ You got unbanned", null, guild.getIconUrl())
                .setColor(Utilities.getUtils().blue)
                .setDescription("\uD83D\uDD13 │ You got unbanned from " + guild.getName())
                .setFooter("requested by " + author.getAsTag(), author.getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(directMessage.build()).queue();
        });
        //if no channel is set
        if (db.get("logChannel").equals("not set")) {
            Utilities.getUtils().error(guild.getDefaultChannel(), "tempban", "\u23F1\uFE0F", "No log channel specified", "To set a log channel type in `" + new Database(guild).get("prefix") + "log channel <channel>`", author.getEffectiveAvatarUrl());
            return;
        }
        //get log channel
        TextChannel textChannel = guild.getTextChannelById(db.get("logChannel"));
        //guild message
        EmbedBuilder guildMessage = new EmbedBuilder()
                .setAuthor("│ " + user.getAsTag() + " got unbanned", null, user.getEffectiveAvatarUrl())
                .setColor(Utilities.getUtils().blue)
                .setDescription("\uD83D\uDD13 │ " + user.getAsMention() + " got unbanned from " + guild.getName())
                .setFooter("requested by " + author.getAsTag(), author.getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        textChannel.sendMessage(guildMessage.build()).queue();
    }
}