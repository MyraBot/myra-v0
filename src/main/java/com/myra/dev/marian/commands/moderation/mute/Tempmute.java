package com.myra.dev.marian.commands.moderation.mute;

import com.mongodb.client.MongoCollection;
import com.myra.dev.marian.database.MongoDb;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
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
        name = "tempmute"
)
public class Tempmute extends Events implements Command {
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
        // Get utilities
        Utilities utilities = Manager.getUtilities();
        //command usage
        if (arguments.length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("tempmute", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(utilities.gray)
                    .addField("\uD83D\uDD07 │ tempmute a specific member", "`" + Prefix.getPrefix(event.getGuild()) + "tempmute <user> <duration><time unit> <reason>`", true)
                    .setFooter("Accepted time units: seconds, minutes, hours, days");
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * tempmute
         */
        //get reason
        String reason = "";
        if (arguments.length > 2) {
            for (int i = 2; i < arguments.length; i++) {
                reason += arguments[i] + " ";
            }
            //remove last space
            reason = reason.substring(0, reason.length() - 1);
        }
        //get user
        User user = utilities.getModifiedUser(event, arguments[0], "tempmute", "\uD83D\uDD07");
        if (user == null) return;
        //get mute role id
        String muteRoleId = new Database(event.getGuild()).get("muteRole");
        //no mute role set
        if (muteRoleId.equals("not set")) {
            utilities.error(event.getChannel(), "tempmute", "\uD83D\uDD07", "You didn't specify a mute role", "To indicate a mute role, type in `" + Prefix.getPrefix(event.getGuild()) + "mute role <role>`", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //if the string is not [NumberLetters]
        if (!arguments[1].matches("[0-9]+[a-zA-z]+")) {
            utilities.error(event.getChannel(), "tempmute", "\uD83D\uDD07", "Invalid time", "please note: `<time><time unit>`", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //return duration as a list
        List durationList = utilities.getDuration(arguments[1]);
        String duration = durationList.get(0).toString();
        long durationInMilliseconds = Long.parseLong(durationList.get(1).toString());
        TimeUnit timeUnit = TimeUnit.valueOf(durationList.get(2).toString());
        //guild message mute
        EmbedBuilder muteGuild = new EmbedBuilder()
                .setAuthor(user.getName() + " got tempmuted", null, user.getEffectiveAvatarUrl())
                .setColor(utilities.red)
                .setDescription("\u23F1\uFE0F │ " + user.getAsMention() + " got muted for **" + duration + " " + timeUnit.toString().toLowerCase() + "**")
                .setFooter("requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //direct message mute
        EmbedBuilder muteDirectMessage = new EmbedBuilder()
                .setAuthor("You got tempmuted on " + event.getGuild().getName(), null, event.getGuild().getIconUrl())
                .setColor(utilities.red)
                .setDescription("\u23F1\uFE0F │ You got muted on " + event.getGuild().getName() + " for **" + duration + " " + timeUnit.toString().toLowerCase() + "**")
                .setFooter("requested by " + event.getMember().getUser().getName(), event.getMember().getUser().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        //with reason
        if (arguments.length > 2) {
            muteGuild.addField("\uD83D\uDCC4 │ reason:", reason, false);
            muteDirectMessage.addField("\uD83D\uDCC4 │ reason:", reason, false);
        }
        //without reason
        else {
            muteGuild.addField("\uD83D\uDCC4 │ no reason", "there was no reason given", false);
            muteDirectMessage.addField("\uD83D\uDCC4 │ no reason", "there was no reason given", false);
        }
        //send message
        event.getChannel().sendMessage(muteGuild.build()).queue();
        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(muteDirectMessage.build()).queue();
        });
        //mute
        event.getGuild().addRoleToMember(event.getGuild().getMember(user), event.getGuild().getRoleById(muteRoleId)).queue();
        //create unmute Document
        Document document = createUnmute(user.getId(), event.getGuild().getId(), durationInMilliseconds, event.getAuthor().getId());
        /**
         * unmute
         */
        //delay
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //if member left the server
                if (event.getGuild().getMemberById(document.getString("userId")) == null) {
                    //delete document
                    mongoDb.getCollection("unmutes").deleteOne(document);
                }
                //remove role
                event.getGuild().removeRoleFromMember(document.getString("userId"), event.getGuild().getRoleById(muteRoleId)).queue();
                //send unmute message
                unmuteMessage(user, event.getGuild(), event.getAuthor());
                //delete document
                mongoDb.getCollection("unmutes").deleteOne(document);

            }
        }, durationInMilliseconds);
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
                .setColor(Manager.getUtilities().green)
                .setDescription("You got unmuted from " + guild.getName())
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
        //guild message unmute
        EmbedBuilder guildMessage = new EmbedBuilder()
                .setAuthor(user.getAsTag() + " got unmuted", null, user.getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().blue)
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
            Database db = new Database(guild);
            /**
             * if unmute time is already reached
             */
            if (unmuteTime < System.currentTimeMillis()) {
                //if member left the server
                if (event.getJDA().getGuildById(doc.getString("guildId")).getMemberById(doc.getString("userId")) == null) {
                    //delete document
                    mongoDb.getCollection("unmutes").deleteOne(doc);
                }
                //remove role
                guild.removeRoleFromMember(doc.getString("userId"), guild.getRoleById(db.get("muteRole"))).queue();
                //send unmute message
                unmuteMessage(event.getJDA().getUserById(doc.getString("userId")), guild, event.getJDA().getUserById(doc.getString("moderatorId")));
                //delete document
                mongoDb.getCollection("unmutes").deleteOne(doc);
                continue;
            }
            /**
             * if unmute time isn't already reached
             */
            //delay
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //if member left the server
                    if (event.getJDA().getGuildById(doc.getString("guildId")).getMemberById(doc.getString("userId")) == null) {
                        //delete document
                        mongoDb.getCollection("unmutes").deleteOne(doc);
                    }
                    //unmute
                    guild.removeRoleFromMember(doc.getString("userId"), guild.getRoleById(db.get("muteRole"))).queue();
                    //send unmute message
                    unmuteMessage(event.getJDA().getUserById(doc.getString("userId")), guild, event.getJDA().getUserById(doc.getString("moderatorId")));
                    //delete document
                    mongoDb.getCollection("unmutes").deleteOne(doc);
                }
            }, doc.getLong("unmuteTime") - System.currentTimeMillis());
        }
    }
}