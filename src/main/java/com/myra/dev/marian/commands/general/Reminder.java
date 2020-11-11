package com.myra.dev.marian.commands.general;

import com.mongodb.client.MongoCollection;
import com.myra.dev.marian.database.MongoDb;

import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import org.bson.Document;

import java.util.List;
import java.util.concurrent.TimeUnit;

@CommandSubscribe(
        name = "reminder",
        aliases = {"remind"}
)
public class Reminder  implements Command {
    //database
    private final MongoDb mongoDb = MongoDb.getInstance();

    @Override
    public void execute(CommandContext ctx) throws Exception {
        //usage
        if (ctx.getArguments().length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("reminder", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Utilities.getUtils().gray)
                    .addField("`" + ctx.getPrefix() + "reminder <duration><time unit> <description>`", "\u23F0 │ Reminds you after a specific amount of time", false)
                    .setFooter("Accepted time units: seconds, minutes, hours, days");
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
// Set reminder
        //get arguments
        String durationRaw = ctx.getArguments()[0];
        StringBuilder reason = new StringBuilder();
        for (int i = 1; i < ctx.getArguments().length; i++) {
            reason.append(ctx.getArguments()[i]).append(" ");
        }
        //remove last space
        reason = new StringBuilder(reason.substring(0, reason.length() - 1));
        //if the string is not (NumberLetters)
        if (!durationRaw.matches("[0-9]+[a-zA-z]+")) {
            Utilities.getUtils().error(ctx.getChannel(), "reminder", "\u23F0", "Invalid time", "please note: `<time><time unit>`", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //return duration as a list
        List<String> durationList = Utilities.getUtils().getDuration(durationRaw);
        String duration = durationList.get(0);
        long durationInMilliseconds = Long.parseLong(durationList.get(1));
        TimeUnit timeUnit = TimeUnit.valueOf(durationList.get(2));
        //reminder info
        EmbedBuilder reminderInfo = new EmbedBuilder()
                .setAuthor("reminder", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(Utilities.getUtils().blue)
                .setDescription("Im gonna remind you in " + duration + " " + timeUnit.toString().toLowerCase() + "!");
        ctx.getChannel().sendMessage(reminderInfo.build()).queue();
        //create reminder document
        Document document = createReminder(ctx.getAuthor().getId(), durationInMilliseconds + System.currentTimeMillis(), reason.toString(), timeUnit);
        //delay
        String finalReason = reason.toString();
        Utilities.TIMER.schedule(new Runnable() {
            @Override
            public void run() {
                //send reminder
                remind(ctx.getAuthor(), finalReason);
                //delete document
                mongoDb.getCollection("reminders").deleteOne(document);
            }
        }, durationInMilliseconds, TimeUnit.MILLISECONDS);
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
                .setColor(Utilities.getUtils().blue)
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
            Utilities.TIMER.schedule(new Runnable() {
                @Override
                public void run() {
                    //send reminder
                    remind(user, doc.getString("description"));
                    //delete document

                    mongoDb.getCollection("reminders").deleteOne(doc);
                }
            }, doc.getLong("remindTime") - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }
    }
}