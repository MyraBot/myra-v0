package com.myra.dev.marian.database.allMethods;

import com.myra.dev.marian.database.MongoDb;
import com.myra.dev.marian.database.documents.MemberDocument;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class GetMembers {
    //variables
    private MongoDb mongoDb;
    private Guild guild;
    private Document guildDocument;

    //constructor
    public GetMembers(MongoDb mongoDb, Guild guild) {
        this.mongoDb = mongoDb;
        this.guild = guild;
        this.guildDocument = mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first();
    }

    /**
     * methods
     */
    //get member
    public GetMember getMember(Member member) {
        // Get document with all members
        Document membersDocument = (Document) guildDocument.get("members");
        // Get member document
        Document memberDocument = (Document) membersDocument.get(member.getId());
        return new GetMember(mongoDb, guild, guildDocument, memberDocument);
    }

    //get sorted members
    public List<MemberDocument> getLeaderboard() {
        //create leaderboard
        List<MemberDocument> leaderboard = new ArrayList<>();
        // Get document with all members
        Document membersDocument = (Document) guildDocument.get("members");
        //get every member
        for (Object document : membersDocument.values()) {
            // Parse Object to Document
            Document memberDocument = (Document) document;
            // Add member document to leaderboard
            leaderboard.add(new MemberDocument(memberDocument));
        }
        //sort list
        Collections.sort(leaderboard, Comparator.comparing(MemberDocument::getXp).reversed());
        return leaderboard;
    }
}
