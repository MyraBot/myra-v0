package com.myra.dev.marian.database.documents;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bson.Document;

import java.util.List;

public class LevelingRolesDocument {
    // Variables
    private List<Document> roles;

    // Constructor
    public LevelingRolesDocument(Document levelingRoles) {
        // Add every role to HashMap
        for (String key : levelingRoles.keySet()) {
            // Get leveling role
            Document roleDocument = (Document) levelingRoles.get(key);
            // Create new Document
            Document role = new Document()
                    .append("level", roleDocument.getInteger("level"))
                    .append("role", roleDocument.getString("role"))
                    .append("remove", roleDocument.getString("remove"));
            // Add document to HashMap
            roles.add(role);
        }
    }

    public void checkForNewOnesOwO(int level, Member member, Guild guild) {
        // check for every role
        for (Document role : roles) {
            // If ur mini poopie level is to small :c
            if (level < role.getInteger("level")) continue;
            // Add role :3
            guild.addRoleToMember(member, guild.getRoleById(role.getString("role")));
            // Check for role to remove
            if (role.getString("remove").equals("not set")) continue;
            // Remove role ._.
            guild.removeRoleFromMember(member, guild.getRoleById(role.getString("role")));
        }
    }
}
