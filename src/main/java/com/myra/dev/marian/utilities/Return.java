package com.myra.dev.marian.utilities;

import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Return {

    //return user
    public User user(GuildMessageReceivedEvent event, String providedUser, String command, String commandEmoji) {
        //if user isn't given by mention / tag / id
        if (!(providedUser.startsWith("<@!") || providedUser.matches("[a-zA-z]+[#]+[0-9]+") || providedUser.matches("\\d+"))) {
            Manager.getUtilities().error(event.getChannel(), command, commandEmoji, "No user given", "Please enter the id, tag or mention the user", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        //if user is given by tag
        if (providedUser.matches("[a-zA-z]+[#]+[0-9]+")) {
            //return user
            return event.getJDA().getUserByTag(providedUser);
        }
        //get user
        User user = event.getJDA().getUserById(providedUser.replaceAll("[<@!>]", ""));
        //if no user found
        if (user == null) {
            Manager.getUtilities().error(event.getChannel(), command, commandEmoji, "No user found", "I couldn't find the specified user", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        return user;
    }

    //return modified user
    public User userModified(GuildMessageReceivedEvent event, String[] sentMessage, String command, String commandEmoji, int providedUser) {
        //if user isn't given by mention / tag / id
        if (!(sentMessage[1].startsWith("<@!") || sentMessage[providedUser].matches("[a-zA-z]+[#]+[0-9]+") || sentMessage[providedUser].matches("\\d+"))) {
            Manager.getUtilities().error(event.getChannel(), command, commandEmoji, "No user given", "Please enter the id, tag or mention the user", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        //if user is given by tag
        if (sentMessage[providedUser].matches("[a-zA-z]+[#]+[0-9]+")) {
            return event.getJDA().getUserByTag(sentMessage[providedUser]);
        }
        //get user
        User user = event.getJDA().getUserById(sentMessage[providedUser].replaceAll("[<@!>]", ""));
        //if no user if found
        if (user == null) {
            Manager.getUtilities().error(event.getChannel(), command, commandEmoji, "No user found", "I couldn't find the specified user", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        //if member isn't in the guild
        if (event.getGuild().getMember(user) == null) {
            Manager.getUtilities().error(event.getChannel(), command, commandEmoji, "No user found", "The user you mentioned isn't on this server", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        //can't modify yourself
        if (user.getId().equals(event.getAuthor().getId())) {
            Manager.getUtilities().error(event.getChannel(), command, commandEmoji, "Can't " + command + " the mentioned user", "You can't " + command + " yourself", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        //can't modify the owner
        else if (event.getGuild().getMember(user).isOwner()) {
            Manager.getUtilities().error(event.getChannel(), command, commandEmoji, "Can't " + command + " the mentioned user", "You can't " + command + " the owner of the server", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        //if user has a higher or equal role than you
        if (!event.getGuild().getMember(user).getRoles().isEmpty()) {
            if (event.getGuild().getMember(user).getRoles().get(0).getPosition() > event.getGuild().getMember(event.getJDA().getSelfUser()).getRoles().get(0).getPosition()) {
                Manager.getUtilities().error(event.getChannel(), command, commandEmoji, "Can't " + command + " " + user.getName(), "I can't " + command + " a member with a higher or equal role than me", event.getAuthor().getEffectiveAvatarUrl());
                return null;
            }
        }
        return user;
    }

    //return text channel
    public TextChannel textChannel(GuildMessageReceivedEvent event, String[] sentMessage, int providedChannel, String command, String commandEmoji) {
        //if no channel is given
        if (!(sentMessage[providedChannel].startsWith("<#") || sentMessage[providedChannel].matches("\\d+"))) {
            Manager.getUtilities().error(event.getChannel(), command, commandEmoji, "No channel given", "Please enter the id or mention the channel", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        //get channel
        TextChannel channel = event.getGuild().getTextChannelById(sentMessage[providedChannel].replaceAll("[<#>]", ""));
        //no channel found
        if (channel == null) {
            Manager.getUtilities().error(event.getChannel(), command, commandEmoji, "No channel found", "The given channel doesn't exist", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        return channel;
    }

    //return role
    public Role role(GuildMessageReceivedEvent event, String[] sentMessage, int providedRole, String command, String commandEmoji) {
        //no role given
        if (!(sentMessage[providedRole].startsWith("<@&") || sentMessage[providedRole].matches("\\d+"))) {
            Manager.getUtilities().error(event.getChannel(), command, commandEmoji, "No role given", "Please enter a id or mention a role", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        Role role = event.getGuild().getRoleById(sentMessage[providedRole].replaceAll("[<@&>]", ""));
        //no role found
        if (role == null) {
            Manager.getUtilities().error(event.getChannel(), command, commandEmoji, "No role found", "I couldn't find the specified role", event.getAuthor().getEffectiveAvatarUrl());
            return null;
        }
        return role;
    }
}
