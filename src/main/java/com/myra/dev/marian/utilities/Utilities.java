package com.myra.dev.marian.utilities;

import com.myra.dev.marian.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.OkHttpClient;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Utilities {
    public final static ScheduledExecutorService TIMER = Executors.newScheduledThreadPool(5);
    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private final static Utilities getUtils = new Utilities();

    public static Utilities getUtils() {
        return getUtils;
    }

    public void loadEmotes(JDA JDA) {
        final JDA jda = JDA;
        // Emotes
        this.offline = getEmote(jda, "Offline");
        this.idle = getEmote(jda, "Idle");
        this.doNotDisturb = getEmote(jda, "DoNotDisturb");
        this.online = getEmote(jda, "Online");

        this.nitroBoost = getEmote(jda, "NitroBoost");
        this.coin = getEmote(jda, "Coin");
        // Badges
        this.bugHunter = getEmote(jda, "BugHunter");
        this.bugHunterLvl2 = getEmote(jda, "BugHunterLvl2");

        this.bravery = getEmote(jda, "Bravery");
        this.brilliance = getEmote(jda, "Brilliance");
        this.balance = getEmote(jda, "Balance");

        this.partner = getEmote(jda, "Partner");
        this.verifiedDeveloper = getEmote(jda, "VerifiedDeveloper");
        this.staff = getEmote(jda, "Staff");
    }

    //colours
    public final int red = 0xFF0055;
    public final int blue = 0xccd9f0;
    public final int green = 0xffcc;
    public final int gray = 0x282c34;
    //keys
    public final String youTubeKey = "AIzaSyAOJVth0U1loodJ9ShNjocc1eKMZr-Xxsg";
    public final String twitchClientId = "2ns4hcj4kkd6vj3armlievqsw8znd3";
    public final String twitchClientSecret = "kbvqhnosdqrezqhy8zuly9hapzeapn";
    public final String twitchRedirect_uri = "http://localhost";
    public final String giphyKey = "nV9Hhe5WbaVli6jg8Nlo2VcIB1kq5Ekq";
    public final String HypixelKey = "6cb5b7e7-66ab-477d-9d18-4f029e676d37";
    public final String spotifyClientId = "f19bf0a7cb204c098dbdaaeedf47f842";
    public final String spotifyClientSecret = "d4d48b2e4b474d098fa440a6d01ece42";
    public final String topGgKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjcxODQ0NDcwOTQ0NTYzMjEyMiIsImJvdCI6dHJ1ZSwiaWF0IjoxNjA0MzMwMTg3fQ.-zX8YHLdiH9w6pmDceN0fHDjTAJd9FbDiNXM2sftoA4";
    //emotes
    public String offline;
    public String idle;
    public String doNotDisturb;
    public String online;

    public String nitroBoost;
    public String coin;
    // Badges
    public String bugHunter;
    public String bugHunterLvl2;

    public String bravery;
    public String brilliance;
    public String balance;

    public String partner;
    public String verifiedDeveloper;
    public String staff;

    /**
     * @param name The name of the emote.
     * @return Returns an emote from Myra's Server.
     */
    public String getEmote(JDA jda, String name) {
        if (jda.getGuildById(Bot.myraServer).getEmotesByName(name, true).isEmpty()) return null;
        return jda.getGuildById(Bot.myraServer).getEmotesByName(name, true).get(0).getAsMention();
    }

    /**
     * Get a clickable message, which redirects you to a link.
     *
     * @param message The shown message.
     * @param link    The link if you click on the message.
     * @return Returns a hyperlink as a String.
     */
    public String hyperlink(String message, String link) {
        return "[" + message + "](" + link + ")";
    }

    /**
     * Get an array as a full sentence.
     *
     * @param array The array, which should be put together.
     * @return Returns the Strings of the array as one String.
     */
    public String getString(String[] array) {
        StringBuilder string = new StringBuilder();
        for (String s : array) {
            string.append(s).append(" ");
        }
        //Remove last space
        string = new StringBuilder(string.substring(0, string.length() - 1));
        return string.toString();
    }

    /**
     * Get the colour of a member.
     *
     * @param member The User you want the colour from.
     * @return The colour as a Color Object.
     */
    public Color getMemberRoleColour(Member member) {
        // Get all roles of the member
        List<Role> roles = member.getRoles();
        // If member doesn't have any roles
        if (roles.isEmpty()) {
            return new Color(gray);
        } else
            // Loop through every roles
            for (Role role : roles) {
                // When the role hasn't the default color
                if (role.getColorRaw() != 536870911) { //RGB int value of default color
                    return role.getColor();
                }
            }
        // If none of the roles aren't the default colour
        return new Color(gray);
    }

    /**
     * Get the duration from a String.
     *
     * @param providedInformation The String with all information.
     * @return Returns a String List, which contains the given duration, the duration in milliseconds and the Time Unit.
     */
    public List<String> getDuration(String providedInformation) {
        //get time unit
        TimeUnit timeUnit = null;
        switch (providedInformation.replaceAll("\\d+", "")) {
            case "s":
            case "sec":
            case "second":
            case "seconds":
                timeUnit = TimeUnit.SECONDS;
                break;
            case "m":
            case "min":
            case "minute":
            case "minutes":
                timeUnit = TimeUnit.MINUTES;
                break;
            case "h":
            case "hour":
            case "hours":
                timeUnit = TimeUnit.HOURS;
                break;
            case "d":
            case "day":
            case "days":
                timeUnit = TimeUnit.DAYS;
                break;
        }
        //get duration
        long duration = Long.parseLong(providedInformation.replaceAll("[^\\d.]", ""));
        long durationInMilliseconds = timeUnit.toMillis(duration);
        //return as a list
        List time = new ArrayList();
        time.add(duration);
        time.add(durationInMilliseconds);
        time.add(timeUnit);
        return time;
    }


    public String formatTime(long timeInMillis) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeInMillis),
                TimeUnit.MILLISECONDS.toMinutes(timeInMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMillis)),
                TimeUnit.MILLISECONDS.toSeconds(timeInMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMillis)));
    }

    /**
     * Generate a new invite link for the bot.
     *
     * @param jda The bot.
     * @return Returns a bot invite.
     */
    public String inviteJda(JDA jda) {
        return jda.getInviteUrl(Permission.ADMINISTRATOR);
    }

    //error message
    public void error(TextChannel textChannel, String command, String commandEmoji, String errorHeader, String error, String authorAvatar) {
        textChannel.sendMessage(new EmbedBuilder()
                .setAuthor(command, null, authorAvatar)
                .setColor(Utilities.getUtils().red)
                .addField("\uD83D\uDEA7 │ " + errorHeader, error, false)
                .build())
                .queue();
    }

    //success info
    public void success(TextChannel textChannel, String command, String commandEmoji, String successHeader, String success, String authorAvatar, boolean deleteAfter5Seconds, String imageUrl) {
        if (deleteAfter5Seconds) {
            textChannel.sendMessage(new EmbedBuilder()
                    .setAuthor(command, null, authorAvatar)
                    .setColor(Utilities.getUtils().green)
                    .addField("\uD83C\uDFC1 │ " + successHeader, success, false)
                    .setImage(imageUrl)
                    .build()
            ).queue((message -> message.delete().queueAfter(5, TimeUnit.SECONDS)));
        } else {
            textChannel.sendMessage(new EmbedBuilder()
                    .setAuthor(command, null, authorAvatar)
                    .setColor(Utilities.getUtils().green)
                    .addField("\uD83C\uDFC1 │ " + successHeader, success, false)
                    .setImage(imageUrl)
                    .build()
            ).queue();
        }
    }

    /**
     * Get member from message.
     *
     * @param event        The GuildMessageReceivedEvent
     * @param providedUser The String the user should be in
     * @param command      The command name
     * @param commandEmoji The emoji of the command
     * @return
     */
    public Member getMember(GuildMessageReceivedEvent event, String providedUser, String command, String commandEmoji) {
        Member member = null;

        // Role given by id or mention
        if (providedUser.startsWith("<@") || providedUser.matches("\\d+")) {
            member = event.getGuild().getMemberById(providedUser.replaceAll("[<@!>]", ""));
        }
        // Role given by name
        if (!event.getGuild().getMembersByEffectiveName(providedUser, true).isEmpty()) {
            member = event.getGuild().getMembersByEffectiveName(providedUser, true).get(0);
        }
        // No role given
        if (member == null) {
            error(event.getChannel(), command, commandEmoji, "No user given", "Please enter the id or mention the user", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        return member;
    }

    /**
     * Get a user.
     *
     * @param event        The GuildMessageReceivedEvent.
     * @param providedUser The String the user is given.
     * @param command      The name of the command.
     * @param commandEmoji The Emoji of the command.
     * @return Returns the user as a User object.
     */
    public User getUser(GuildMessageReceivedEvent event, String providedUser, String command, String commandEmoji) {
        User user;
        final JDA jda = event.getJDA();

        // Role given by id or mention
        if (providedUser.startsWith("<@") || providedUser.matches("\\d+")) {
            user = jda.getUserById(providedUser.replaceAll("[<@!>]", ""));
        }
        // Role given by name
        else if (!jda.getUsersByName(providedUser, true).isEmpty()) {
            user = jda.getUsersByName(providedUser, true).get(0);
        }
        // No role given
        else {
            error(event.getChannel(), command, commandEmoji, "No user given", "Please enter the id or mention the user", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        return user;
    }

    /**
     * Get a user, who will be modified.
     *
     * @param event        The GuildMessageReceivedEvent.
     * @param providedUser The String the user is given.
     * @param command      The name of the command.
     * @param commandEmoji The Emoji of the command.
     * @return Returns the user as a User object.
     */
    public User getModifiedUser(GuildMessageReceivedEvent event, String providedUser, String command, String commandEmoji) {
        Member member;

        // Role given by id or mention
        if (providedUser.startsWith("<@") || providedUser.matches("\\d+")) {
            member = event.getGuild().getMemberById(providedUser.replaceAll("[<@>]", ""));
        }
        // Role given by name
        else if (!event.getGuild().getMembersByEffectiveName(providedUser, true).isEmpty()) {
            member = event.getGuild().getMembersByEffectiveName(providedUser, true).get(0);
        }
        // No role given
        else {
            error(event.getChannel(), command, commandEmoji, "No user found", "Be sure the user is in the server", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }

        //can't modify yourself
        if (member.equals(event.getMember())) {
            error(event.getChannel(), command, commandEmoji, "Can't " + command + " the mentioned user", "You can't " + command + " yourself", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        //can't modify the owner
        else if (member.isOwner()) {
            error(event.getChannel(), command, commandEmoji, "Can't " + command + " the mentioned user", "You can't " + command + " the owner of the server", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        //if user has a higher or equal role than you
        if (!member.getRoles().isEmpty()) {
            if (member.getRoles().get(0).getPosition() > event.getGuild().getMember(event.getJDA().getSelfUser()).getRoles().get(0).getPosition()) {
                error(event.getChannel(), command, commandEmoji, "Can't " + command + " " + member.getEffectiveName(), "I can't " + command + " a member with a higher or equal role than me", event.getAuthor().getEffectiveAvatarUrl());
                return null;
            }
        }
        return member.getUser();
    }

    /**
     * Get a text channel.
     *
     * @param event           The GuildMessageReceivedEvent.
     * @param providedChannel The String the channel should be in.
     * @param command         The command name.
     * @param commandEmoji    The command emoji.
     * @return Returns a channel as a TextChannel Object.
     */
    public TextChannel getTextChannel(GuildMessageReceivedEvent event, String providedChannel, String command, String commandEmoji) {
        TextChannel channel;

        // Role given by id or mention
        if (providedChannel.startsWith("<#") || providedChannel.matches("\\d+")) {
            channel = event.getGuild().getTextChannelById(providedChannel.replaceAll("[<#>]", ""));
        }
        // Role given by name
        else if (!event.getGuild().getTextChannelsByName(providedChannel, true).isEmpty()) {
            channel = event.getGuild().getTextChannelsByName(providedChannel, true).get(0);
        }
        // No role given
        else {
            error(event.getChannel(), command, commandEmoji, "No channel given", "Please enter the id or mention the channel", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }

        // No role found
        if (channel == null) {
            error(event.getChannel(), command, commandEmoji, "No channel found", "The given channel doesn't exist", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        return channel;
    }

    /**
     * Get a role.
     *
     * @param event        The GuildMessageReceivedEvent.
     * @param providedRole The String the role should be in.
     * @param command      The command name.
     * @param commandEmoji The command Emoji.
     * @return Returns a role as a Role Object.
     */
    public Role getRole(GuildMessageReceivedEvent event, String providedRole, String command, String commandEmoji) {
        Role role;

        // Role given by id or mention
        if (providedRole.startsWith("<@&") || providedRole.matches("\\d+")) {
            role = event.getGuild().getRoleById(providedRole.replaceAll("[<@&>]", ""));
        }
        // Role given by name
        else if (!event.getGuild().getRolesByName(providedRole, true).isEmpty()) {
            role = event.getGuild().getRolesByName(providedRole, true).get(0);
        }
        // No role given
        else {
            error(event.getChannel(), command, commandEmoji, "No role given", "Please enter a id or mention a role", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }

        // No role found
        if (role == null) {
            error(event.getChannel(), command, commandEmoji, "No role found", "I couldn't find the specified role", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        return role;
    }
}
