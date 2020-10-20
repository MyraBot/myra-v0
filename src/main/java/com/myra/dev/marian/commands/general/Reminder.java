package com.myra.dev.marian.commands.general;

import com.mongodb.client.MongoCollection;
import com.myra.dev.marian.database.MongoDb;
import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@CommandSubscribe(
        name = "reminder",
        aliases = {"remind"}
)
public class Reminder extends Events implements Command {
    //database
    private static MongoDb mongoDb;

    //set variable
    public static void setDb(MongoDb db) {
        mongoDb = db;
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //usage
        if (arguments.length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("reminder", null, event.getMember().getUser().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("`" + Prefix.getPrefix(event.getGuild()) + "reminder <duration><time unit> <description>`", "\u23F0 │ Reminds you after a specific amount of time", false)
                    .setFooter("Accepted time units: seconds, minutes, hours, days");
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        /**
         * set reminder
         */
        //get arguments
        String durationRaw = arguments[0];
        String reason = "";
        for (int i = 1; i < arguments.length; i++) {
            reason += arguments[i] + " ";
        }
        //remove last space
        reason = reason.substring(0, reason.length() - 1);
        //if the string is not (NumberLetters)
        if (!durationRaw.matches("[0-9]+[a-zA-z]+")) {
            Manager.getUtilities().error(event.getChannel(), "reminder", "\u23F0", "Invalid time", "please note: `<time><time unit>`", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //return duration as a list
        List durationList = Manager.getUtilities().getDuration(durationRaw);
        String duration = durationList.get(0).toString();
        long durationInMilliseconds = Long.parseLong(durationList.get(1).toString());
        TimeUnit timeUnit = TimeUnit.valueOf(durationList.get(2).toString());
        //reminder info
        EmbedBuilder reminderInfo = new EmbedBuilder()
                .setAuthor("reminder", null, event.getAuthor().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().blue)
                .setDescription("Im gonna remind you in " + duration + " " + timeUnit.toString().toLowerCase() + "!");
        event.getChannel().sendMessage(reminderInfo.build()).queue();
        //create reminder document
        Document document = createReminder(event.getAuthor().getId(), durationInMilliseconds + System.currentTimeMillis(), reason, timeUnit);
        //delay
        String finalReason = reason;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //send reminder
                remind(event.getAuthor(), finalReason);
                //delete document
                mongoDb.getCollection("reminders").deleteOne(document);
            }
        }, durationInMilliseconds);
    }

    //create document
    private Document createReminder(String userId, Long remindTime, String description, TimeUnit timeUnit) {
        MongoCollection<Document> reminder = mongoDb.getCollection("reminders");
        //create Document
        Document docToInsert = new Document()
                .append("userId", userId)
                .append("remindTime", remindTime)
                .append("timeUnit", timeUnit.toString())
                .append("description", description);
        reminder.insertOne(docToInsert);
        return docToInsert;
    }

    //reminder message
    public void remind(User author, String description) {
        EmbedBuilder reminder = new EmbedBuilder()
                .setAuthor(author.getName(), null, author.getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().blue)
                .addField("\u23F0 │ reminder", description, false);
        author.openPrivateChannel().queue((channel ->
                channel.sendMessage(reminder.build()).queue()));
    }

    public void onReady(ReadyEvent event) {
        //for each document
        for (Document doc : mongoDb.getCollection("reminders").find()) {
            //get remind time
            Long remindTime = doc.getLong("remindTime");
            //get user
            User user = event.getJDA().retrieveUserById(doc.getString("userId")).complete();
            //if no user can be found
            if (user == null) return;
            //if remind time is already reached
            if (remindTime < System.currentTimeMillis()) {
                //send reminder
                remind(user, doc.getString("description"));
                //delete document
                mongoDb.getCollection("reminders").deleteOne(doc);
                continue;
            }

            //delay
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //send reminder
                    remind(user, doc.getString("description"));
                    //delete document

                    mongoDb.getCollection("reminders").deleteOne(doc);

                }
            }, doc.getLong("remindTime") - System.currentTimeMillis());
        }
    }
}