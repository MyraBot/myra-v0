package com.myra.dev.marian.database.allMethods;

import com.myra.dev.marian.database.MongoDb;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class GetNested {
    //variables
    private MongoDb mongoDb;
    private Guild guild;
    private String nested;

    //constructor
    public GetNested(MongoDb mongoDb, Guild guild, String nested) {
        this.mongoDb = mongoDb;
        this.guild = guild;
        this.nested = nested;
    }

    // Get String
    public Object get(String key) {
        //get nested object
        Document nestedDocument = (Document) mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first().get(nested);
        //return String
        return nestedDocument.get(key);
    }

    // Set Object
    public void set(String key, Object value, Manager.type type) {
        // Get guildDocument
        Document guildDocument = mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first();
        //get nested object
        Document nestedDocument = (Document) guildDocument.get(nested);
// Get variable type
        // String
        if (type.equals(Manager.type.STRING)) {
            // Replace String
            nestedDocument.replace(key, (String) value);
        }
        if (type.equals(Manager.type.INTEGER)) {
            // Replace String
            nestedDocument.replace(key, (Integer) value);
        }
        if (type.equals(Manager.type.BOOLEAN)) {
            // Replace String
            nestedDocument.replace(key, (Boolean) value);
        }
        // Replace guild Document
        mongoDb.getCollection("guilds").findOneAndReplace(mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first(), guildDocument);
    }
}
