package com.myra.dev.marian.database.allMethods;

import org.bson.Document;

public class MemberDocument {
    private String id;
    private String name;
    private int level;
    private int xp;
    private int invites;

    public MemberDocument(Document memberDocument) {
        this.id = memberDocument.getString("id");
        this.name = memberDocument.getString("name");
        this.level = memberDocument.getInteger("level");
        this.xp = memberDocument.getInteger("xp");
        this.invites = memberDocument.getInteger("invites");
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return name;
    }

    public String getName() {
        return name.substring(0, name.length() - 5);
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public int getInvites() {
        return invites;
    }
}
