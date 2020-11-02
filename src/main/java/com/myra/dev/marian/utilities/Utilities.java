package com.myra.dev.marian.utilities;

import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Utilities {
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
    //emotes
    public final String offline = "<:Offline:749616594916868098>";
    public final String idle = "<:Idle:749616593973018665>";
    public final String doNotDisturb = "<:DoNotDisturb:749616593939726346>";
    public final String online = "<:Online:749616597647491154>";

    public final String nitroBoost = "<:NitroBoost:726467129217646634>";
    public final String coin = "<:coin:768420796157919232>";
    // Badges
    public final String bugHunter = "<:BugHunter:751101984756465805>";
    public final String bugHunterLvl2 = "<:BugHunterLvl2:751101984769310732>";

    public final String bravery = "<:Bravery:751102172158099600>";
    public final String brilliance = "<:Brilliance:751102172640444477>";
    public final String balance = "<:balance:751102171092615219>";

    public final String partner = "<:Partner:748984597584412883>";
    public final String verifiedDeveloper = "<:VerifiedDeveloper:726467094413181019>";
    public final String staff = "<:staff:751105613831995454>";

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
                .setColor(Manager.getUtilities().red)
                .addField("\uD83D\uDEA7 │ " + errorHeader, error, false)
                .build())
                .queue();
    }

    //success info
    public void success(TextChannel textChannel, String command, String commandEmoji, String successHeader, String success, String authorAvatar, boolean deleteAfter5Seconds, String imageUrl) {
        if (deleteAfter5Seconds) {
            textChannel.sendMessage(new EmbedBuilder()
                    .setAuthor(command, null, authorAvatar)
                    .setColor(Manager.getUtilities().green)
                    .addField("\uD83C\uDFC1 │ " + successHeader, success, false)
                    .setImage(imageUrl)
                    .build()
            ).queue((message -> message.delete().queueAfter(5, TimeUnit.SECONDS)));
        } else {
            textChannel.sendMessage(new EmbedBuilder()
                    .setAuthor(command, null, authorAvatar)
                    .setColor(Manager.getUtilities().green)
                    .addField("\uD83C\uDFC1 │ " + successHeader, success, false)
                    .setImage(imageUrl)
                    .build()
            ).queue();
        }
    }

    /**
     * Get a user.
     *
     * @param event        The GuildMessageReceivedEvent.
     * @param userRaw      The String the user is given.
     * @param command      The name of the command.
     * @param commandEmoji The Emoji of the command.
     * @return Returns the user as a User object.
     */
    public User getUser(GuildMessageReceivedEvent event, String userRaw, String command, String commandEmoji) {
        User user = null;
        //get jda
        JDA jda = event.getJDA();
        // If user is given by mention
        if (userRaw.startsWith("<@")) {
            user = event.getMessage().getMentionedMembers().get(0).getUser();
        }
        // If user is given by id
        if (user == null && userRaw.matches("\\d+")) {
            user = jda.getUserById(userRaw);
        }
        // If no user is given
        if (user == null) {
            error(event.getChannel(), command, commandEmoji, "No user given", "Please enter the id or mention the user", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        return user;
    }

    //return modified user
    public User getModifiedUser(GuildMessageReceivedEvent event, String userRaw, String command, String
            commandEmoji) {
        User user = null;
        //get jda
        JDA jda = event.getJDA();
        // If user is given by mention
        if (userRaw.startsWith("<@!")) {
            user = event.getMessage().getMentionedMembers().get(0).getUser();
        }
        // If user is given by id
        if (user == null && userRaw.matches("\\d+")) {
            user = jda.getUserById(userRaw);
        }
        // If no user is given
        if (user == null) {
            error(event.getChannel(), command, commandEmoji, "No user given", "Please enter the id, tag or mention the user", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        //if member isn't in the guild
        if (event.getGuild().getMember(user) == null) {
            error(event.getChannel(), command, commandEmoji, "No user found", "The user you mentioned isn't on this server", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        //can't modify yourself
        if (user.equals(event.getAuthor())) {
            error(event.getChannel(), command, commandEmoji, "Can't " + command + " the mentioned user", "You can't " + command + " yourself", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        //can't modify the owner
        else if (event.getGuild().getMember(user).isOwner()) {
            error(event.getChannel(), command, commandEmoji, "Can't " + command + " the mentioned user", "You can't " + command + " the owner of the server", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        //if user has a higher or equal role than you
        if (!event.getGuild().getMember(user).getRoles().isEmpty()) {
            if (event.getGuild().getMember(user).getRoles().get(0).getPosition() > event.getGuild().getMember(event.getJDA().getSelfUser()).getRoles().get(0).getPosition()) {
                error(event.getChannel(), command, commandEmoji, "Can't " + command + " " + user.getName(), "I can't " + command + " a member with a higher or equal role than me", event.getAuthor().getEffectiveAvatarUrl());
                return null;
            }
        }
        return user;
    }

    //return text channel
    public TextChannel getTextChannel(GuildMessageReceivedEvent event, String providedChannel, String
            command, String commandEmoji) {
        //if no channel is given
        if (!(providedChannel.startsWith("<#") || providedChannel.matches("\\d+"))) {
            error(event.getChannel(), command, commandEmoji, "No channel given", "Please enter the id or mention the channel", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        //get channel
        TextChannel channel = event.getGuild().getTextChannelById(providedChannel.replaceAll("[<#>]", ""));
        //no channel found
        if (channel == null) {
            error(event.getChannel(), command, commandEmoji, "No channel found", "The given channel doesn't exist", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        return channel;
    }

    //return role
    public Role getRole(GuildMessageReceivedEvent event, String providedRole, String command, String commandEmoji) {
        //no role given
        if (!(providedRole.startsWith("<@&") || providedRole.matches("\\d+"))) {
            error(event.getChannel(), command, commandEmoji, "No role given", "Please enter a id or mention a role", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        Role role = event.getGuild().getRoleById(providedRole.replaceAll("[<@&>]", ""));
        //no role found
        if (role == null) {
            error(event.getChannel(), command, commandEmoji, "No role found", "I couldn't find the specified role", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        return role;
    }
}
