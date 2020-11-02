package com.myra.dev.marian.database;

import com.mongodb.client.MongoCollection;
import com.myra.dev.marian.Main;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bson.Document;
import org.json.JSONArray;

import static com.mongodb.client.model.Filters.eq;

public class MongoDbDocuments {

    public static void guild(Guild guild) {
        try {
            MongoDb db = MongoDb.getInstance();
            //get collections
            MongoCollection<Document> guilds = db.getCollection("guilds");

            // Create members Document
            Document membersDocument = new Document();
            //for each member
            for (Member member : guild.getMembers()) {
                // If member isn't a bot
                if (!member.getUser().isBot()) {
                    // Create
                    Document memberDocument = member(member);
                    // Add to members
                    membersDocument.put(member.getId(), memberDocument);
                }
            }
            // Economy
            Document economy = new Document()
                    .append("currency", Manager.getUtilities().coin);
            // Leveling
            Document levelingDocument = new Document()
                    .append("boost", 1)
                    .append("roles", new Document());
            //commands
            Document commands = new Document
                    //general
                    ("help", true)
                    .append("commands", true)
                    .append("invite", true)
                    .append("support", true)
                    .append("ping", true)
                    .append("calculate", true)
                    .append("avatar", true)
                    .append("information", true)
                    .append("reminder", true)
                    // Leveling
                    .append("rank", true)
                    .append("leaderboard", true)
                    //fun
                    .append("meme", true)
                    //music
                    .append("music", true)
                    .append("join", true)
                    .append("disconnect", true)
                    .append("play", true)
                    .append("skip", true)
                    .append("clear queue", true)
                    .append("shuffle", true)
                    .append("track information", true)
                    .append("queue", true)
                    //moderation
                    .append("moderation", true)
                    .append("clear", true)
                    .append("nick", true)
                    .append("kick", true)
                    .append("mute", true)
                    .append("ban", true)
                    .append("unban", true);
            //listeners
            Document listeners = new Document(
                    //welcome
                    "welcomeImage", false)
                    .append("welcomeEmbed", false)
                    .append("welcomeDirectMessage", false)
                    //autorole
                    .append("autorole", false)
                    //suggestions
                    .append("suggestions", false);
            //streamers
            JSONArray streamers = new JSONArray();
            //welcome
            Document welcome = new Document()
                    .append("welcomeChannel", "not set")
                    .append("welcomeColour", String.format("0x%06X", (0xFFFFFF & Manager.getUtilities().blue)))
                    .append("welcomeImageBackground", "not set")
                    .append("welcomeImageFont", "default")
                    .append("welcomeEmbedMessage", "Welcome {user} to {server}! Enjoy your stay")
                    .append("welcomeDirectMessage", "Welcome {user} to {server}! Enjoy your stay");
            /**
             * insert document
             */
            if (guilds.find(eq("guildId", guild.getId())).first() == null) {
                //create Document
                Document guildDoc = new Document("guildId", guild.getId())
                        .append("guildName", guild.getName())
                        .append("prefix", Main.prefix)
                        .append("economy", economy)
                        .append("leveling", levelingDocument)
                        .append("members", membersDocument)
                        .append("notificationChannel", "not set")
                        .append("streamers", streamers)
                        .append("suggestionsChannel", "not set")
                        .append("logChannel", "not set")
                        .append("autoRole", "not set")
                        .append("muteRole", "not set")
                        .append("welcome", welcome)
                        .append("commands", commands)
                        .append("listeners", listeners);
                guilds.insertOne(guildDoc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Document member(Member member) {
        Document membersDocument = new Document()
                .append("id", member.getId())
                .append("name", member.getUser().getName() + "#" + member.getUser().getDiscriminator())
                .append("level", 0)
                .append("xp", 0)
                .append("balance", 0)
                .append("dailyStreak", 0)
                .append("lastClaim", System.currentTimeMillis())
                .append("rankBackground", "default")
                .append("invites", 0);
        return membersDocument;
    }
}
