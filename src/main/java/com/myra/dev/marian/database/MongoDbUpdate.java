package com.myra.dev.marian.database;


import com.mongodb.client.MongoCursor;
import com.myra.dev.marian.management.Startup;
import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MongoDbUpdate {
    //database
    private final MongoDb mongoDb = MongoDb.getInstance();

    //update Database
    public void update(ReadyEvent event) throws Exception {
        List<String> guildIds = new ArrayList<>();
        for (Guild guild : event.getJDA().getGuilds()) {
            guildIds.add(guild.getId());
        }

/*        // Guild update
        for (Document doc : mongoDb.getCollection("guilds").find()) {
            // Make backup
            mongoDb.getCollection("backup").insertOne(doc);

            // Get variables
            final String guildId = doc.getString("guildId");
            final String guildName = doc.getString("guildName");
            final String prefix = doc.getString("prefix");
            final Document economy = (Document) doc.get("economy");
            final Document leveling = (Document) doc.get("leveling");
            final Document notifications = (Document) doc.get("notifications");
            final String suggestionsChannel = doc.getString("suggestionsChannel");
            final String logChannel = doc.getString("logChannel");
            final String autoRole = doc.getString("autoRole");
            final String muteRole = doc.getString("muteRole");
            final Document welcome = (Document) doc.get("welcome");
            final Document commands = (Document) doc.get("commands");
            final Document listeners = (Document) doc.get("listeners");

            Document economyDocument = new Document()
                    .append("currency", economy.getString("currency"))
                    .append("shop", economy.get("shop"));
            Document levelingDocument = new Document()
                    .append("boost", 1)
                    .append("roles", leveling.get("roles"))
                    .append("channel", "not set");
            Document notificationDocument = new Document()
                    .append("channel", notifications.getString("channel"))
                    .append("twitch", notifications.getList("twitch", String.class))
                    .append("youtube", notifications.getList("youtube", String.class));
            Document welcomeNested = new Document()
                    .append("welcomeChannel",welcome.getString("welcomeChannel"))
                    .append("welcomeColour", welcome.getString("welcomeColour"))
                    .append("welcomeImageBackground", welcome.getString("welcomeImageBackground"))
                    .append("welcomeImageFont",  welcome.getString("welcomeImageFont"))
                    .append("welcomeEmbedMessage", welcome.getString("welcomeEmbedMessage"))
                    .append("welcomeDirectMessage", welcome.getString("welcomeDirectMessage"));
            Document commandsNested = new Document()
                    .append("calculate", commands.getBoolean("calculate"))
                    .append("avatar", commands.getBoolean("avatar"))
                    .append("information", commands.getBoolean("information"))
                    .append("reminder", commands.getBoolean("reminder"))

                    .append("rank", commands.getBoolean("rank"))
                    .append("leaderboard", commands.getBoolean("leaderboard"))
                    .append("edit rank", commands.getBoolean("edit rank"))

                    .append("meme", commands.getBoolean("meme"))
                    .append("text formatter", commands.getBoolean("text formatter"))

                    .append("music", commands.getBoolean("music"))
                    .append("join", commands.getBoolean("join"))
                    .append("leave", commands.getBoolean("leave"))
                    .append("play", commands.getBoolean("play"))
                    .append("skip", commands.getBoolean("skip"))
                    .append("clear queue", commands.getBoolean("clear queue"))
                    .append("shuffle", commands.getBoolean("shuffle"))
                    .append("music information", commands.getBoolean("music information"))
                    .append("queue", commands.getBoolean("queue"))
                    .append("music controller", commands.getBoolean("music controller"))

                    .append("moderation", commands.getBoolean("moderation"))
                    .append("clear", commands.getBoolean("clear"))
                    .append("nick", commands.getBoolean("nick"))
                    .append("kick", commands.getBoolean("kick"))
                    .append("mute", commands.getBoolean("mute"))
                    .append("ban", commands.getBoolean("ban"))
                    .append("unban", commands.getBoolean("unban"));
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
                    .append("premium", false)
                    .append("economy", economyDocument)
                    .append("leveling", levelingDocument)
                    .append("notifications", notificationDocument)
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
        }*/
        // Member update
        final MongoCursor<Document> iterator = mongoDb.getCollection("user").find().iterator();

        /*while (iterator.hasNext()) {
            final Document document = iterator.next(); // Get next document

            Document userDocument = new Document();
        }*/
    }

    //add guild document
    public void guildJoinEvent(GuildJoinEvent event) throws Exception {
        MongoDbDocuments.guild(event.getGuild());
    }

    /**
     * Add missing guild documents to database
     *
     * @param event The ReadyEvent.
     * @throws Exception
     */
    public void updateDatabase(ReadyEvent event) throws Exception {
        // Add missing guilds to the database
        for (Guild guild : event.getJDA().getGuilds()) {
            // Guild isn't in database yet
            if (mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first() == null) {
                MongoDbDocuments.guild(guild); // Create new guild document
            }
        }
        // Update the database itself (if necessary)
        update(event);
        Startup.next = !Startup.next; // Change next to true
    }

    /**
     * Change guild name.
     *
     * @param event The GuildUpdateNameEvent event.
     */
    public void guildNameUpdated(GuildUpdateNameEvent event) {
        Document guildDoc = mongoDb.getCollection("guilds").find(eq("guildId", event.getGuild().getId())).first();
        Bson updateGuildDoc = new Document("$set", new Document("guildName", event.getNewValue()));
        mongoDb.getCollection("guilds").findOneAndUpdate(guildDoc, updateGuildDoc);
    }

    //delete document on guild leave
    public void guildLeaveEvent(GuildLeaveEvent event) {
        mongoDb.getCollection("guilds").deleteOne(eq("guildId", event.getGuild().getId()));
    }
}
