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
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

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
    public void execute(CommandContext ctx) throws Exception {
        //connect to database
        if (ctx.getEvent().getMessage().getContentRaw().equals("~db.update") && ctx.getAuthor().getId().equals("639544573114187797")) {
            //for each document
            for (Document doc : mongoDb.getCollection("guilds").find()) {
                String guildId = doc.getString("guildId");
                String guildName = doc.getString("guildName");
                String prefix = doc.getString("prefix");
                Document economy = (Document) doc.get("economy");
                Document leveling = (Document) doc.get("leveling");
                Document members = (Document) doc.get("members");
                String notificationChannel = doc.getString("notificationChannel");
                List<String> streamers = doc.getList("streamers", String.class);
                String suggestionsChannel = doc.getString("suggestionsChannel");
                String logChannel = doc.getString("logChannel");
                String autoRole = doc.getString("autoRole");
                String muteRole = doc.getString("muteRole");
                Document welcome = (Document) doc.get("welcome");
                Document commands = (Document) doc.get("commands");
                Document listeners = (Document) doc.get("listeners");


                for (String memberId : members.keySet()) {
                    //get member document
                    Document memberDocument = (Document) members.get(memberId);

                    memberDocument.append("rankBackground", "default");
                }


                Document economyDocument = new Document()
                        .append("currency", Manager.getUtilities().coin);

                Document levelingDocument = new Document()
                        .append("boost", 1)
                        .append("roles", new Document());
                Document welcomeNested = new Document()
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

                        .append("rank", true)
                        .append("leaderboard", true)

                        .append("meme", true)
                        .append("textFormatter", true);
                Document listenersNested = new Document()
                        .append("welcomeImage", false)
                        .append("welcomeEmbed", false)
                        .append("welcomeDirectMessage", false)

                        .append("suggestions", false)

                        .append("autorole", false);
                //create Document
                Document guildDoc = new Document("guildId", guildId)
                        .append("guildName", guildName)
                        .append("prefix", prefix)
                        .append("economy", economy)
                        .append("leveling", leveling)
                        .append("members", members)
                        .append("notificationChannel", notificationChannel)
                        .append("streamers", streamers)
                        .append("suggestionsChannel", suggestionsChannel)
                        .append("logChannel", logChannel)
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
            if (ctx.getEvent().getMessage().getContentRaw().equals("db.create") && ctx.getAuthor().getId().equals("639544573114187797")) {
                MongoDbDocuments.guild(ctx.getGuild());
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
            // If guild isn't in the database yet
            if (mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first() == null) {
                // Add guild to database
                MongoDbDocuments.guild(guild);
            }

            for (Member member : guild.getMembers()) {
                //check if member is a bot
                if (member.getUser().isBot()) continue;
                //get current document
                Document members = (Document) mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first().get("members");
                //if member is already in guild document
                if (members.containsKey(member.getId())) continue;
                // Create new member document
                Document memberDocument = MongoDbDocuments.member(member);
                //add new document
                members.put(member.getId(), memberDocument);
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
        Document members = (Document) mongoDb.getCollection("guilds").find(eq("guildId", event.getGuild().getId())).first().get("members");
        // If member is already in guild document
        if (members.values().contains(event.getMember().getId())) return;
        //create new Member document
        Document member = MongoDbDocuments.member(event.getMember());
        // Add new document
        members.put(event.getMember().getId(), member);
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

    // User changes name
    public void userUpdateNameEvent(UserUpdateNameEvent event) {
        // For each guilds document
        for (Document guildDocument : mongoDb.getCollection("guilds").find()) {
            // Check if user is in guild
            if (!guildDocument.toString().contains(event.getUser().getId())) continue;
            // Get guild document
            Document updatedDocument = guildDocument;
            // Get members document
            Document members = (Document) updatedDocument.get("members");
            // Get member document
            Document member = (Document) members.get(event.getUser().getId());
            // Replace old name
            member.replace("name", event.getNewValue() + "#" + event.getUser().getDiscriminator());
            // Replace members
            updatedDocument.replace("members", members);
            // Update guild document
            mongoDb.getCollection("guilds").findOneAndReplace(guildDocument, updatedDocument);
        }
    }
}
