package com.myra.dev.marian.database;

import com.myra.dev.marian.Bot;
import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bson.Document;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MongoDbDocuments {

    public static void guild(Guild guild) throws Exception {
        MongoDb mongoDb = MongoDb.getInstance();

        if (mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first() != null)
            return; // In database is already a guild document
        // Economy
        Document economy = new Document()
                .append("currency", Utilities.getUtils().coin);
        // Leveling
        Document levelingDocument = new Document()
                .append("boost", 1)
                .append("roles", new Document());
        //commands
        Document commands = new Document()
                .append("calculate", true)
                .append("avatar", true)
                .append("information", true)
                .append("reminder", true)

                .append("rank", true)
                .append("leaderboard", true)
                .append("edit rank", true)

                .append("meme", true)
                .append("text formatter", true)

                .append("music", true)
                .append("join", true)
                .append("leave", true)
                .append("play", true)
                .append("skip", true)
                .append("clear queue", true)
                .append("shuffle", true)
                .append("music information", true)
                .append("queue", true)
                .append("music controller", true)

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
                .append("welcomeColour", String.format("0x%06X", (0xFFFFFF & Utilities.getUtils().blue)))
                .append("welcomeImageBackground", "not set")
                .append("welcomeImageFont", "default")
                .append("welcomeEmbedMessage", "Welcome {user} to {server}! Enjoy your stay")
                .append("welcomeDirectMessage", "Welcome {user} to {server}! Enjoy your stay");
// Insert document
        List<Document> guildDocuments = new ArrayList<>();
        //create Document
        Document guildDoc = new Document("guildId", guild.getId())
                .append("guildName", guild.getName())
                .append("prefix", Bot.prefix)
                .append("economy", economy)
                .append("leveling", levelingDocument)
                .append("notificationChannel", "not set")
                .append("streamers", streamers)
                .append("suggestionsChannel", "not set")
                .append("logChannel", "not set")
                .append("autoRole", "not set")
                .append("muteRole", "not set")
                .append("welcome", welcome)
                .append("commands", commands)
                .append("listeners", listeners);
        mongoDb.getCollection("guilds").insertOne(guildDoc);
    }

    public static Document createGuildMemberDocument(Member member) {
        return new Document()
                .append("xp", 0)
                .append("level", 0)
                .append("balance", 0)
                .append("dailyStreak", 0)
                .append("lastClaim", System.currentTimeMillis())
                .append("rankBackground", "default");
    }
}