package com.myra.dev.marian.utilities;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public class Permissions {

    public static boolean isAdministrator(Member member) {
        return member.hasPermission(Permission.ADMINISTRATOR);
    }

    public static boolean isModerator(Member member) {
        return member.hasPermission(Permission.VIEW_AUDIT_LOGS);
    }
}
