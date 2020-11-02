package com.myra.dev.marian.database.allMethods;

import com.myra.dev.marian.database.MongoDb;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class Database {
    // Database
    private final MongoDb mongoDb = MongoDb.getInstance();

    //variable
    private Guild guild;

    //constructor
    public Database(Guild guild) {
        this.guild = guild;
    }

    /**
     * methods
     */
    //get String
    public String get(String key) {
        return mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first().getString(key);
    }

    //replace String
    public void set(String key, String value) {
        // Replace value
        Document updatedDocument = mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first();
        updatedDocument.replace(key, value);
        // Update database
        mongoDb.getCollection("guilds").findOneAndReplace(mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first(), updatedDocument);
    }

    //get nested object
    public GetNested getNested(String nested) {
        return new GetNested(mongoDb, guild, nested);
    }

    //get members
    public GetMembers getMembers() {
        return new GetMembers(mongoDb, guild);
    }

    //get listeners
    public GetListenerManager getListenerManager() {
        return new GetListenerManager(mongoDb, guild);
    }

    //get commands
    public GetCommandManager getCommandManager() {
        return new GetCommandManager(mongoDb, guild);
    }

    //get notification manager
    public GetNotificationManager getNotificationManager() {
        return new GetNotificationManager(mongoDb, guild);
    }

    // Get Leveling
    public GetLeveling getLeveling() {
        return new GetLeveling(mongoDb, guild);
    }
}
