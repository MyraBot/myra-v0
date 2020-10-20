package com.myra.dev.marian.database.allMethods;

import com.myra.dev.marian.database.MongoDb;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class GetCommandManager {
    //variables
    private MongoDb mongoDb;
    private Guild guild;

    //constructor
    public GetCommandManager(MongoDb mongoDb, Guild guild) {
        this.mongoDb = mongoDb;
        this.guild = guild;
    }

    /**
     * methods
     */
    //check if listener is enabled
    public Boolean check(String command) throws Exception {
        //get listener object
        Document commands = (Document) mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first().get("commands");
        //return value of listener
        return commands.getBoolean(command);
    }

    //toggle command
    public void toggle(String command, GuildMessageReceivedEvent event) {
        //get guildDocument
        Document updatedDocument = mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first();
        //get listener object
        Document commands = (Document) updatedDocument.get("commands");
        //check if command exists
        if (commands.getBoolean(command) == null) {
            Manager.getUtilities().error(event.getChannel(), "toggle", "\uD83D\uDD11", "Couldn't find command", "The command doesn't exist", event.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        //get new value of listener
        boolean newValue = !commands.getBoolean(command);
        //replace String
        commands.replace(command, newValue);
        //replace guild Document
        mongoDb.getCollection("guilds").findOneAndReplace(mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first(), updatedDocument);
        //success information
        if (newValue) {
            Manager.getUtilities().success(event.getChannel(), "toggle", "\uD83D\uDD11", "`" + command + "` got toggled on", "Members can now use the command `" + command + "` again", event.getAuthor().getEffectiveAvatarUrl(), false, false, null);
        } else {
            Manager.getUtilities().success(event.getChannel(), "toggle", "\uD83D\uDD11", "`" + command + "` got toggled off", "From now on members can no longer use the command `" + command + "`", event.getAuthor().getEffectiveAvatarUrl(), false, false, null);
        }
    }
}
