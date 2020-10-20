package com.myra.dev.marian.commands.moderation.ban;

import com.mongodb.client.MongoCollection;
import com.myra.dev.marian.database.MongoDb;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.time.Instant;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@CommandSubscribe(
        name = "tempban",
        aliases = {"temp ban", "tempbean", "temp bean"}
)
public class Tempban extends Events implements Command {
    //database
    private static MongoDb mongoDb;

    //set variable
    public static void setDb(MongoDb db) {
        mongoDb = db;
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //missing permissions
        if (!Permissions.isModerator(event.getMember())) return;
        // Get Utilities
        Utilities utilities = Manager.getUtilities();
        //command usage
        if (arguments.length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("tempban", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "tempban <user> <duration><time unit> [reason]`", "\u23F1\uFE0F │ Ban a user for a certain amount of time", false)
                    .setFooter("Accepted time units: seconds, minutes, hours, days");
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * tempban
         */
        //get arguments
        String durationRaw = arguments[1];
        String reason = "";
        if (arguments.length > 2) {
            for (int i = 2; i < arguments.length; i++) {
                reason += arguments[i] + " ";
            }
            //remove last space
            reason = reason.substring(0, reason.length() - 1);
        }
        //if the duration is not [NumberLetters]
        if (!durationRaw.matches("[0-9]+[a-zA-z]+")) {
            utilities.error(event.getChannel(), "tempban", "\u23F1\uFE0F", "Invalid time", "please note: `<time><time unit>`", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //get user
        User user = utilities.getModifiedUser(event, arguments[0], "tempban", "\u23F1\uFE0F");
        if (user == null) return;
        //return duration as a list
        List durationList = Manager.getUtilities().getDuration(durationRaw);
        String duration = durationList.get(0).toString();
        long durationInMilliseconds = Long.parseLong(durationList.get(1).toString());
        TimeUnit timeUnit = TimeUnit.valueOf(durationList.get(2).toString());
        //guild message ban
        EmbedBuilder guildMessageBan = new EmbedBuilder()
                .setAuthor( user.getAsTag() + " got temporary banned", null, user.getEffectiveAvatarUrl())
                .setColor(utilities.red)
                .setDescription("\u23F1\uFE0F │ " + user.getAsMention() + " got banned for **" + duration + " " + timeUnit.toString().toLowerCase() + "**")
                .setFooter("requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //direct message ban
        EmbedBuilder directMessageBan = new EmbedBuilder()
                .setAuthor("You got temporary banned", null, event.getGuild().getIconUrl())
                .setColor(Manager.getUtilities().red)
                .setDescription("\u23F1\uFE0F │ You got banned on `" + event.getGuild().getName() + "` for **" + duration + " " + timeUnit.toString().toLowerCase() + "**")
                .setFooter("requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl())
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
        event.getChannel().sendMessage(guildMessageBan.build()).queue();
        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(directMessageBan.build()).queue();
        });
        //with reason
        if (reason != null) {
            event.getGuild().getMember(user).ban(7, reason).queue();
        }
        //without reason
        else {
            event.getGuild().getMember(user).ban(7).queue();
        }
        //create unban Document
        Document document = createUnban(user.getId(), event.getGuild().getId(), durationInMilliseconds, event.getAuthor().getId());
        /**
         * unban
         */
        //delay
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //unban
                event.getGuild().unban(user).queue();
                //send unban message
                unbanMessage(user, event.getGuild(), event.getAuthor());
                //delete document

                mongoDb.getCollection("unbans").deleteOne(document);

            }
        }, durationInMilliseconds);
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
                for (net.dv8tion.jda.api.entities.Guild.Ban ban : banList) {
                    //if member left the server
                    if (event.getJDA().getGuildById(doc.getString("guildId")).getMemberById(doc.getString("userId")) == null) {
                        //delete document
                        mongoDb.getCollection("unmutes").deleteOne(doc);
                    }
                    if (ban.getUser().getId().equals(doc.getString("userId"))) {
                        //delay
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                //unban
                                guild.unban(event.getJDA().getUserById(doc.getString("userId"))).queue();
                                //send unban message
                                unbanMessage(event.getJDA().getUserById(doc.getString("userId")), guild, event.getJDA().getUserById(doc.getString("moderatorId")));
                                //delete document
                                mongoDb.getCollection("unbans").deleteOne(doc);
                            }
                        }, doc.getLong("unbanTime") - System.currentTimeMillis());

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
                .setColor(Manager.getUtilities().blue)
                .setDescription("\uD83D\uDD13 │ You got unbanned from " + guild.getName())
                .setFooter("requested by " + author.getAsTag(), author.getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(directMessage.build()).queue();
        });
        //if no channel is set
        if (db.get("logChannel").equals("not set")) {
            Manager.getUtilities().error(guild.getDefaultChannel(), "tempban", "\u23F1\uFE0F", "No log channel specified", "To set a log channel type in `" + Prefix.getPrefix(guild) + "log channel <channel>`", author.getEffectiveAvatarUrl());
            return;
        }
        //get log channel
        TextChannel textChannel = guild.getTextChannelById(db.get("logChannel"));
        //guild message
        EmbedBuilder guildMessage = new EmbedBuilder()
                .setAuthor("│ " + user.getAsTag() + " got unbanned", null, user.getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().blue)
                .setDescription("\uD83D\uDD13 │ " + user.getAsMention() + " got unbanned from " + guild.getName())
                .setFooter("requested by " + author.getAsTag(), author.getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        textChannel.sendMessage(guildMessage.build()).queue();
    }
}