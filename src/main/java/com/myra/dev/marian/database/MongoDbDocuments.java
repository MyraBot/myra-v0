package com.myra.dev.marian.database;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.myra.dev.marian.Main;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import org.bson.Document;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MongoDbDocuments {

    public static void guild(Guild guild) {
        try {
            MongoDb db = new MongoDb();

            //get collections
            MongoCollection members = db.getCollection("members");
            MongoCollection<Document> guilds = db.getCollection("guilds");
            //for each member
            List<BasicDBObject> memberInformation = new ArrayList<>();
            for (Member member : guild.getMembers()) {
                if (!member.getUser().isBot()) {
                    BasicDBObject thing = new BasicDBObject(member.getUser().getId(), new BasicDBObject("id", member.getId()).append("name", member.getUser().getName() + "#" + member.getUser().getDiscriminator()).append("level", 0).append("xp", 0).append("invites", 0));
                    //if user isn't already in the database
                    if (members.find(eq("id", member.getUser().getId())).first() == null) {
                        Document memberDoc = new Document("id", member.getUser().getId()).append("name", member.getUser().getName() + "#" + member.getUser().getDiscriminator()).append("facebook", "not set").append("instagram", "not set").append("youtube", "not set").append("twitch", "not set").append("mixer", "not set").append("imgur", "not set").append("tiktok", "not set").append("steam", "not set").append("epic", "not set").append("twitter", "not set").append("origin", "not set").append("reddit", "not set").append("spotify", "not set").append("skype", "not set").append("xboxlive", "not set").append("teamspeak", "not set").append("mumble", "not set").append("stackoverflow", "not set").append("tumblr", "not set").append("giphy", "not set").append("github", "not set");
                        members.insertOne(memberDoc);
                    }
                    memberInformation.add(thing);
                }
            }
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
                        .append("members", memberInformation)
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

                //load Hashmap
                Prefix.load();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
