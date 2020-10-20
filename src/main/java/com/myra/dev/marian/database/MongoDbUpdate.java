package com.myra.dev.marian.database;

import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.print.Doc;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@CommandSubscribe(
        name = "db.update"
)
public class MongoDbUpdate extends Events implements Command {
    //database
    private static MongoDb mongoDb;

    //set variable
    public static void setDb(MongoDb db) {
        mongoDb = db;
    }

    //update Database
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //connect to database
        if (event.getMessage().getContentRaw().equals("~db.update") && event.getAuthor().getId().equals("639544573114187797")) {
            //for each document
            for (Document doc : mongoDb.getCollection("guilds").find()) {
                String guildId = doc.getString("guildId");
                String guildName = doc.getString("guildName");
                String prefix = doc.getString("prefix");
                List<Document> members = doc.getList("members", Document.class);
                String notificationChannel = doc.getString("notificationChannel");
                List<String> streamers = doc.getList("streamers", String.class);
                String suggestionsChannel = doc.getString("suggestionsChannel");
                String logChannel = doc.getString("logChannel");
                String autoRole = doc.getString("autoRole");
                String muteRole = doc.getString("muteRole");
                Document welcome = (Document) doc.get("welcome");
                Document commands = (Document) doc.get("commands");
                Document listeners = (Document) doc.get("listeners");

                for (Document member : members) {
                    //get id of user
                    String id = member.toString().split("=")[2].split(",")[0];
                    //get member document
                    Document memberDocument = (Document) member.get(id);
                    //memberDocument.remove("invites");
                }

                /*Document welcomeNested = new Document()
                        .append("welcomeChannel", "not set")
                        .append("welcomeColour", Manager.getUtilities().blue)
                        .append("welcomeImageBackground", "not set")
                        .append("welcomeImageFont", "default")
                        .append("welcomeEmbedMessage", "Welcome {user} to {server}! Enjoy your stay")
                        .append("welcomeDirectMessage", "Welcome {user} to {server}! Enjoy your stay");
                Document commandsNested = new Document()
                        .append("commands", true)
                        .append("help", true)
                        .append("ping", true)
                        .append("invite", true)
                        .append("support", true)

                        .append("information", true)
                        .append("avatar", true)
                        .append("calculate", true)
                        .append("reminder", true)

                        .append("meme", true)
                        .append("textFormatter", true);
                Document listenersNested = new Document()
                        .append("welcomeImage", true)
                        .append("welcomeEmbed", true)
                        .append("welcomeDirectMessage", true)

                        .append("autorole", true);*/

                //create Document
                Document guildDoc = new Document("guildId", guildId)
                        .append("guildName", guildName)
                        .append("prefix", prefix)
                        .append("members", members)
                        .append("notificationChannel", notificationChannel)
                        .append("streamers", streamers)
                        .append("suggestionsChannel", suggestionsChannel)
                        .append("logsChannel", logChannel)
                        .append("autoRole", autoRole)
                        .append("muteRole", muteRole)
                        .append("welcome", welcome)
                        .append("commands", commands)
                        .append("listeners", listeners);
                //replace old one
                mongoDb.getCollection("guilds").findOneAndReplace(
                        mongoDb.getCollection("guilds").find(eq("guildId", guildDoc.getString("guildId"))).first(),
                        guildDoc
                );
            }
            //close database
            mongoDb.close();
            if (event.getMessage().getContentRaw().equals("db.create") && event.getAuthor().getId().equals("639544573114187797")) {
                MongoDbDocuments.guild(event.getGuild());
            }
        }
    }

    //add guild document
    public void guildJoinEvent(GuildJoinEvent event) {
        MongoDbDocuments.guild(event.getGuild());
    }

    //add missing members to the database
    @Override
    public void jdaReady(ReadyEvent event) throws Exception {
        for (Guild guild : event.getJDA().getGuilds()) {
            for (Member member : guild.getMembers()) {
                //check if member is a bot
                if (member.getUser().isBot()) continue;
                //get current document
                List<Document> members = mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first().getList("members", Document.class);
                //if member is already in guild document
                if (members.toString().contains(member.getId())) continue;
                //create new Member document
                Document memberDocument = new Document(
                        member.getId(),
                        new Document()
                                .append("id", member.getId())
                                .append("name", member.getUser().getName() + "#" + member.getUser().getDiscriminator())
                                .append("level", 0)
                                .append("xp", 0)
                                .append("invites", 0)
                );
                //add new document
                members.add(memberDocument);
                //update 'members' Object
                Document updatedDocument = mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first();
                updatedDocument.replace("members", members);
                //update database
                mongoDb.getCollection("guilds").findOneAndReplace(
                        mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first(),
                        updatedDocument
                );
            }
        }
    }


    //changed guild name
    @Override
    public void guildNameUpdated(GuildUpdateNameEvent event) {
        Document guildDoc = mongoDb.getCollection("guilds").find(eq("guildId", event.getGuild().getId())).first();
        Bson updateGuildDoc = new Document("$set", new Document("guildName", event.getNewValue()));
        mongoDb.getCollection("guilds").findOneAndUpdate(guildDoc, updateGuildDoc);
    }

    //add member to guild
    @Override
    public void memberJoined(GuildMemberJoinEvent event) {
        //check if member is a bot
        if (event.getUser().isBot()) return;
        //get current document
        List<Document> members = mongoDb.getCollection("guilds").find(eq("guildId", event.getGuild().getId())).first().getList("members", Document.class);
        //if member is already in guild document
        if (members.toString().contains(event.getMember().getId())) return;
        //create new Member document
        Document member = new Document(
                event.getMember().getId(),
                new Document()
                        .append("id", event.getMember().getId())
                        .append("name", event.getUser().getName() + "#" + event.getUser().getDiscriminator())
                        .append("level", 0)
                        .append("xp", 0)
                        .append("invites", 0)
        );
        //add new document
        members.add(member);
        //update 'members' Object
        Document updatedDocument = mongoDb.getCollection("guilds").find(eq("guildId", event.getGuild().getId())).first();
        updatedDocument.replace("members", members);
        //update database
        mongoDb.getCollection("guilds").findOneAndReplace(
                mongoDb.getCollection("guilds").find(eq("guildId", event.getGuild().getId())).first(),
                updatedDocument
        );
    }

    //delete document on guild leave
    @Override
    public void guildLeaveEvent(GuildLeaveEvent event) {
        mongoDb.getCollection("guilds").deleteOne(eq("guildId", event.getGuild().getId()));
    }
}
