package com.myra.dev.marian.database;


import com.myra.dev.marian.management.Startup;
import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@CommandSubscribe(
        name = "db.update"
)
public class MongoDbUpdate implements Command {
    //database
    private final MongoDb mongoDb = MongoDb.getInstance();

    //update Database
    @Override
    public void execute(CommandContext ctx) throws Exception {
        //connect to database
        if (ctx.getEvent().getMessage().getContentRaw().equals("~db.update") && ctx.getAuthor().getId().equals("639544573114187797")) {
            // For each document
            for (Document doc : mongoDb.getCollection("guilds").find()) {
                // Make backup
//                mongoDb.getCollection("backup").insertOne(doc);

                // Get variables
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
                    //memberDocument.append("rankBackground", "default");
                }

                Document economyDocument = new Document()
                        .append("currency", Utilities.getUtils().coin);
                Document levelingDocument = new Document()
                        .append("boost", 1)
                        .append("roles", new Document());
                Document welcomeNested = new Document()
                        .append("welcomeChannel", "not set")
                        .append("welcomeColour", Utilities.getUtils().blue)
                        .append("welcomeImageBackground", "not set")
                        .append("welcomeImageFont", "default")
                        .append("welcomeEmbedMessage", "Welcome {user} to {server}! Enjoy your stay")
                        .append("welcomeDirectMessage", "Welcome {user} to {server}! Enjoy your stay");
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
        }
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
        // Check every guild
        for (Guild guild : event.getJDA().getGuilds()) {
            // Guild isn't in database yet
            if (mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first() == null) {
                MongoDbDocuments.guild(guild); // Create new guild document
            }
        }
        Startup.next = !Startup.next; // Change next to true
    }

    /**
     * Change guild name.
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
