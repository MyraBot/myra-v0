package com.myra.dev.marian.database.allMethods;

import com.myra.dev.marian.database.MongoDb;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.Document;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class GetMember {
    //variables
    private MongoDb mongoDb;
    private Guild guild;
    private Document guildDocument;
    private Document memberDocument;

    //constructor
    public GetMember(MongoDb mongoDb, Guild guild, Document guildDocument, Document memberDocument) {
        this.mongoDb = mongoDb;
        this.guild = guild;
        this.guildDocument = guildDocument;
        this.memberDocument = memberDocument;
    }

    /**
     * methods
     */
    //addXp
    public void addXp(int xpToAdd) {
        //update xp
        memberDocument.replace("xp", memberDocument.getInteger("xp") + xpToAdd);
        //update Document
        mongoDb.getCollection("guilds").findOneAndReplace(
                mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first(),
                guildDocument
        );
    }

    //get xp
    public int getXp() {
        return memberDocument.getInteger("xp");
    }

    //get level
    public int getLevel() {
        return memberDocument.getInteger("level");
    }

    //change level
    public void setLevel(int level) {
        //update level
        memberDocument.replace("level", level);
        //update xp
        double xp;
        //parabola
        double squaredNumber = Math.pow(level, 2);
        double exactXp = squaredNumber * 5;
        //round off
        DecimalFormat f = new DecimalFormat("###");
        xp = Double.parseDouble(f.format(exactXp));
        //round down number
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        //convert to int and remove the '.0'
        int newXp = Integer.parseInt(String.valueOf(xp).replace(".0", ""));
        //replace xp
        memberDocument.replace("xp", newXp);
        //update Document
        mongoDb.getCollection("guilds").findOneAndReplace(
                mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first(),
                guildDocument
        );
    }

    //get rank
    public int getRank() {
        //create leaderboard
        List<MemberDocument> leaderboard = new ArrayList<>();
        //get every member
        Document membersDocument = (Document) guildDocument.get("members");
        // For every member document
        for (Object document : membersDocument.values()) {
            // Parse Object to Document
            Document member = (Document) document;
            // Add member to leaderboard
            leaderboard.add(new MemberDocument(member));
        }
        // Sort list
        Collections.sort(leaderboard, Comparator.comparing(MemberDocument::getXp).reversed());
        // Get rank
        int rank = 0;
        // Search for member
        for (MemberDocument doc : leaderboard) {
            if (doc.getId().equals(memberDocument.getString("id"))) {
                rank = leaderboard.indexOf(doc) + 1;
                break;
            }
        }
        // Return rank
        return rank;
    }

    //get balance
    public int getBalance() {
        return memberDocument.getInteger("balance");
    }

    // Set balance
    public void setBalance(int balance) {
        //replace balance
        memberDocument.replace("balance", balance);
        //update Document
        mongoDb.getCollection("guilds").findOneAndReplace(
                mongoDb.getCollection("guilds").find(eq("guildId", guild.getId())).first(),
                guildDocument
        );
    }
}
