package com.myra.dev.marian.commands.general.information;

import com.myra.dev.marian.utilities.Utilities;
import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CommandSubscribe(
        command = "information",
        name = "information user",
        aliases = {"info user", "information member", "info member"}
)
public class InformationUser implements Command {

    @Override
    public void execute(CommandContext ctx) throws Exception {
        //get utilities
        Utilities utilities = Manager.getUtilities();
        Member user;
        String roleNames = "*this user has no roles*";
// Get user
        //yourself information
        if (ctx.getArguments().length == 0) {
            user = ctx.getEvent().getMember();
        }
        //get given member
        else {
            //if user isn't in the guild
            if (ctx.getGuild().getMember(Manager.getUtilities().getUser(ctx.getEvent(), ctx.getArguments()[0], "information user", "\uD83D\uDC64")) == null) {
                utilities.error(ctx.getChannel(), "information user", "\uD83D\uDC64", "No user found", "For this command the user has to be on this server", ctx.getAuthor().getEffectiveAvatarUrl());
                return;
            }
            user = ctx.getGuild().getMember(Manager.getUtilities().getUser(ctx.getEvent(), ctx.getArguments()[0], "information user", "\uD83D\uDC64"));
        }

        List<Role> roles = user.getRoles();
        if (!roles.isEmpty()) {
            roleNames = "";
            //role names
            for (Role role : roles) {
                roleNames += role.getAsMention() + " ";
            }
        }
        //users status
        OnlineStatus status = user.getOnlineStatus();
        String StringStatus = status.toString().replace("OFFLINE", utilities.offline + " │ offline").replace("IDLE", utilities.idle + " │ idle").replace("DO_NOT_DISTURB", utilities.doNotDisturb + " │ do not distrub").replace("ONLINE", utilities.online + " │ online");
        //badges
        String badges = getBadges(user, utilities);
        /**
         * build embed
         */
        EmbedBuilder userInformation = new EmbedBuilder()
                .setAuthor(" │ " + user.getUser().getAsTag(), user.getUser().getEffectiveAvatarUrl(), user.getUser().getEffectiveAvatarUrl())
                .setThumbnail(user.getUser().getEffectiveAvatarUrl())
                .setColor(Manager.getUtilities().getMemberRoleColour(user))
                .addField("\uD83C\uDF9F │ user id", user.getId(), true);
        //nickname
        if (user.getNickname() != null)
            userInformation.addField("\uD83D\uDD75 │ nickname", "\uD83C\uDFF7 │ " + user.getNickname(), true);
        //status
        userInformation.addField("\uD83D\uDCE1 │ status", StringStatus, false);
        //activity
        if (!user.getActivities().isEmpty())
            userInformation.addField("\uD83C\uDFB2 │ activity", user.getActivities().get(0).getName(), true);
        //badges
        if (!badges.equals("")) userInformation.addField("\uD83C\uDFC5 │ badges", badges, false);
        //join time
        userInformation.addField("\uD83D\uDCC5 │ joined server", user.getTimeJoined().atZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy , hh:mm")), true);
        //booster
        if (ctx.getGuild().getBoosters().contains(user))
            userInformation.addField(utilities.nitroBoost + " │ is boosting", "since: " + user.getTimeBoosted().atZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy , hh:mm")), true);
        //permissions
        if (user.hasPermission(Permission.ADMINISTRATOR)) {
            userInformation.addField("\uD83D\uDD11 │ Permissions", "Administrator", false);
        } else if (user.hasPermission(Permission.VIEW_AUDIT_LOGS)) {
            userInformation.addField("\uD83D\uDD11 │ Permissions", "Moderator", false);
        }
        userInformation.addField("\uD83D\uDCC3 │ roles", roleNames, false);

        ctx.getChannel().sendMessage(userInformation.build()).queue();
    }

    //return badges
    private String getBadges(Member user, Utilities utilities) {
        String badges = "";
        //bug hunter
        if (user.getUser().getFlags().toString().contains("BUG_HUNTER_LEVEL_1"))
            badges += utilities.bugHunter + " ";
        //bug hunter level 2
        if (user.getUser().getFlags().toString().contains("BUG_HUNTER_LEVEL_2"))
            badges += utilities.bugHunterLvl2 + " ";
        if (user.getUser().getFlags().toString().contains("EARLY_SUPPORTER")) {
        }
        if (user.getUser().getFlags().toString().contains("HYPESQUAD")) {
        }
        //hypeSquad balance
        if (user.getUser().getFlags().toString().contains("HYPESQUAD_BALANCE")) badges += utilities.balance + " ";
        //hypeSquad bravery
        if (user.getUser().getFlags().toString().contains("HYPESQUAD_BRAVERY")) badges += utilities.bravery + " ";
        //hypeSquad brilliance
        if (user.getUser().getFlags().toString().contains("HYPESQUAD_BRILLIANCE"))
            badges += utilities.brilliance + " ";
        if (user.getUser().getFlags().toString().contains("PARTNER")) {
            badges += utilities.partner + " ";
        }
        if (user.getUser().getFlags().toString().contains("STAFF")) {
            badges += utilities.staff + " ";
        }
        if (user.getUser().getFlags().toString().contains("SYSTEM")) {
        }
        if (user.getUser().getFlags().toString().contains("UNKNOWN")) {
        }
        if (user.getUser().getFlags().toString().contains("VERIFIED_BOT")) {
        }
        if (user.getUser().getFlags().toString().contains("VERIFIED_DEVELOPER")) {
            badges += utilities.verifiedDeveloper + " ";
        }
        return badges;
    }
}