package com.myra.dev.marian.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class Prefix {
    /**
     * HashMap
     */
    //define HashMaps
    public static HashMap<String, HashMap<String, String>> prefix = new HashMap<>();

    //load HashMaps
    public static void load() {
        MongoDb mongoDb = new MongoDb();

        JSONParser parser = new JSONParser();

        MongoCollection<Document> guilds = mongoDb.getCollection("guilds");
        FindIterable<Document> iterable = guilds.find();
        MongoCursor<Document> cursorPrefix = iterable.iterator();
        /**
         * prefix
         */
        try {
            prefix.clear();

            while (cursorPrefix.hasNext()) {
                JSONObject obj = (JSONObject) parser.parse(cursorPrefix.next().toJson());
                HashMap<String, String> data = new HashMap<String, String>();

                data.put("prefix", obj.get("prefix").toString());

                prefix.put(obj.get("guildId").toString(), data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //close everything

        finally {
            //close cursors
            cursorPrefix.close();
        }
    }

    /**
     * Database
     */
    //get Prefix
    public static String getPrefix(Guild guild) {
//        db.getCollection("guilds").find(eq("guildId", guild.getId())).first().getString("prefix");
        HashMap<String, String> map = prefix.get(guild.getId());
        return map.get("prefix");
    }

    //change Prefix
    public static void setPrefix(Guild guild, String newPrefix) {
        MongoDb db = new MongoDb();
//        db.getCollection("guilds").find(eq("guildId", guild.getId())).first().replace("prefix", newPrefix);
        //MongoDb
        Document guildDoc = db.getCollection("guilds").find(eq("guildId", guild.getId())).first();
        Bson updateGuildDoc = new Document("$set", new Document("prefix", newPrefix));
        db.getCollection("guilds").findOneAndUpdate(guildDoc, updateGuildDoc);

        //save in HashMap
        HashMap<String, String> map = prefix.get(guild.getId());
        map.put("prefix", newPrefix);
        prefix.put(guild.getId(), map);
    }
}
