package com.myra.dev.marian.database.allMethods;

import com.myra.dev.marian.database.MongoDb;
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
        guildDocument = mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first();
    }

    /**
     * methods
     */
    //get member
    public GetMember getMember(Member member) {
        //variable
        Document memberDocument = null;
        //get members Document
        List<Document> membersDocument = guildDocument.getList("members", Document.class);
        //get member
        for (Document document : membersDocument) {
            if (document.containsKey(member.getId())) {
                memberDocument = (Document) document.get(member.getId());
            }
        }
        return new GetMember(mongoDb, guild, guildDocument, memberDocument);
    }

    //get sorted members
    public List<MemberDocument> getLeaderboard() {
        //get members Document
        List<Document> membersDocument = guildDocument.getList("members", Document.class);
        //create leaderboard
        List<MemberDocument> leaderboard = new ArrayList<>();
        //get every member
        for (Document document : membersDocument) {
            //get id of user
            String id = document.toString().split("=")[2].split(",")[0];
            //get member document
            Document memberDocument = (Document) document.get(id);
            //add xp to leaderboard
            leaderboard.add(new MemberDocument(memberDocument));
        }
        //sort list
        Collections.sort(leaderboard, Comparator.comparing(MemberDocument::getXp).reversed());
        return leaderboard;
    }
}
