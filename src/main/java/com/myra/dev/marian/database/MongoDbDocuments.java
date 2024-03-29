package com.myra.dev.marian.database;

import com.myra.dev.marian.Bot;
import com.myra.dev.marian.utilities.CustomEmote;
import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MongoDbDocuments {

    public static void guild(Guild guild) throws Exception {
        MongoDb mongoDb = MongoDb.getInstance();

        if (mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first() != null)
            return; // In database is already a guild document
        // Economy
        Document economy = new Document()
                .append("currency", Utilities.getUtils().getEmote(guild.getJDA(), CustomEmote.coin).getAsMention())
                .append("shop", new Document());
        // Leveling
        Document levelingDocument = new Document()
                .append("boost", 1)
                .append("roles", new Document())
                .append("channel", "not set");
        // Notification
        Document notificationsDocument = new Document()
                .append("channel", "not set")
                .append("twitch", new ArrayList<String>())
                .append("youtube", new ArrayList<String>());
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
                .append("premium", false)
                .append("economy", economy)
                .append("leveling", levelingDocument)
                .append("notifications", notificationsDocument)
                .append("suggestionsChannel", "not set")
                .append("logChannel", "not set")
                .append("reactionRoles", new ArrayList<>())
                .append("autoRole", "not set")
                .append("muteRole", "not set")
                .append("welcome", welcome)
                .append("commands", commands)
                .append("listeners", listeners);
        mongoDb.getCollection("guilds").insertOne(guildDoc);
    }

    public static Document createUserDocument(User user) {
        return new Document()
                .append("userId", user.getId())
                .append("birthday", "not set")
                .append("achievements", new Document());
    }

    public static Document createGuildMemberDocument(Member member) {
        return new Document()
                .append("level", 0)
                .append("xp", 0)
                .append("messages", 0)
                .append("voiceCallTime", Long.valueOf(0))
                .append("balance", 0)
                .append("dailyStreak", 0)
                .append("lastClaim", System.currentTimeMillis())
                .append("rankBackground", "default");
    }
}