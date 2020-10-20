package com.myra.dev.marian.database.allMethods;

import com.myra.dev.marian.database.MongoDb;
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

    /**
     * methods
     */
    //get String
    public String get(String key) {
        //get nested object
        Document nestedDocument = (Document) mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first().get(nested);
        //return String
        return nestedDocument.getString(key);
    }

    //set String
    public void set(String key, String value) {
        //get guildDocument
        Document guildDocument = mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first();
        //get nested object
        Document nestedDocument = (Document) guildDocument.get(nested);
        //replace String
        nestedDocument.replace(key, value);
        //replace guild Document
        mongoDb.getCollection("guilds").findOneAndReplace(mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first(), guildDocument);
    }
}
