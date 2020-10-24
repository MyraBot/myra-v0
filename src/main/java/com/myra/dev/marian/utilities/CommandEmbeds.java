package com.myra.dev.marian.utilities;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

public class CommandEmbeds {

    //commands list
    public static EmbedBuilder commands(String authorAvatar) {
        EmbedBuilder commands = new EmbedBuilder()
                .setAuthor("commands", null, authorAvatar)
                .setColor(Manager.getUtilities().gray)
                .addField("`general`", "\uD83C\uDF88 │ The main commands of the bot", false)
                .addField("`fun`", "\uD83D\uDD79 │ Commands to play around with", false)
                .addField("`leveling`", "\uD83C\uDFC6 │ Leveling", false)
                .addField("`music`", "\uD83D\uDCFB │ Music related commands", false)
                .addField("`moderation`", "\uD83D\uDD28 │ Commands for security", false)
                .addField("`administrator`", "\uD83D\uDD29 │ Server commands", false);
        return commands;
    }

    //general
    public static EmbedBuilder general(net.dv8tion.jda.api.entities.Guild guild, String authorAvatar, String botName) {
        EmbedBuilder general = new EmbedBuilder()
                .setAuthor("general", null, authorAvatar)
                .setColor(Manager.getUtilities().gray)
                .addField("`" + Prefix.getPrefix(guild) + "help`", "\uD83E\uDDF0 │ Opens a menu with several helpful links and lists", false)
                .addField("`" + Prefix.getPrefix(guild) + "commands`", "\uD83D\uDCC3 │ Shows this", false)
                .addField("`" + Prefix.getPrefix(guild) + "invite`", "\u2709\uFE0F │ Invite " + botName + " to your server", false)
                .addField("`" + Prefix.getPrefix(guild) + "support`", "\u26A0\uFE0F │ Join the support server to get help and report bugs", false)
                .addField("`" + Prefix.getPrefix(guild) + "ping`", "\uD83C\uDFD3 │ Check the ping of the bot", false)
                .addField("`" + Prefix.getPrefix(guild) + "calculate <number 1 <operator> <number 2>`", "\uD83E\uDDEE │ Let the bot calculate something for you", false)
                .addField("`" + Prefix.getPrefix(guild) + "avatar @user`", "\uD83D\uDDBC │ Gives you profile pictures of other people", false)
                .addField("`" + Prefix.getPrefix(guild) + "information`", "\uD83D\uDDD2 │ Gives you information", false)
                .addField("`" + Prefix.getPrefix(guild) + "suggest`", "\uD83D\uDDF3 │ Suggest something", false);
        return general;
    }

    //fun
    public static EmbedBuilder fun(net.dv8tion.jda.api.entities.Guild guild, String authorAvatar) {
        EmbedBuilder fun = new EmbedBuilder()
                .setAuthor("fun", null, authorAvatar)
                .setColor(Manager.getUtilities().gray)
                .addField("`" + Prefix.getPrefix(guild) + "meme`", "\uD83E\uDD2A │ Shows a random meme", false)
                .addField("`" + Prefix.getPrefix(guild) + "format <text>`", "\uD83D\uDDDA │ Change the font of your text", false)
                .addField("`" + Prefix.getPrefix(guild) + "would you rather`", " │ Play a round of would you rather", false);
        return fun;
    }

    //leveling
    public EmbedBuilder leveling(Guild guild, String authorAvatar) {
        EmbedBuilder fun = new EmbedBuilder()
                .setAuthor("leveling", null, authorAvatar)
                .setColor(Manager.getUtilities().gray)
                .addField("`" + Prefix.getPrefix(guild) + "rank <user>`", "\uD83C\uDFC5 │ Shows the rank of a user", false)
                .addField("`" + Prefix.getPrefix(guild) + "leaderboard`", "\uD83E\uDD47 │ Shows the leaderboard", false)
                .addField("`" + Prefix.getPrefix(guild) + "leveling background <url>`", "\uD83D\uDDBC │ Set a custom rank background", false);
        return fun;
    }

    //music
    public EmbedBuilder music(net.dv8tion.jda.api.entities.Guild guild, String authorAvatar) {
        EmbedBuilder music = new EmbedBuilder()
                .setAuthor("music", null, authorAvatar)
                .setColor(Manager.getUtilities().gray)
                .addField("`" + Prefix.getPrefix(guild) + "join`", "\uD83D\uDCE5 │ Let the bot join your voice channel", false)
                .addField("`" + Prefix.getPrefix(guild) + "disconnect`", "\uD83D\uDCE4 │ Kick the bot from your voice channel", false)
                .addField("`" + Prefix.getPrefix(guild) + "play <song>`", "\uD83D\uDCBF │ Add a song to the queue*", false)
                .addField("`" + Prefix.getPrefix(guild) + "skip`", "\u23ED\uFE0F │ Skip the current song", false)
                .addField("`" + Prefix.getPrefix(guild) + "clear queue`", "\uD83D\uDDD1 │ Clear the queue", false)
                .addField("`" + Prefix.getPrefix(guild) + "shuffle`", "\uD83C\uDFB2 │ Jumble the current queue", false)
                .addField("`" + Prefix.getPrefix(guild) + "track information`", "\uD83D\uDDD2 │ Get information about the current song", false)
                .addField("`" + Prefix.getPrefix(guild) + "queue`", "\uD83D\uDCC3 │ Shows the queue", false)
                .addField("`" + Prefix.getPrefix(guild) + "music controller`", "\uD83C\uDF9A️ │ opens the music reactions controller", false)
                .setFooter("supported platforms: YoutTube, SoundCloud, Bandcamp, Vimeo, Twitch streams");
        return music;
    }

    //moderation
    public static EmbedBuilder moderation(net.dv8tion.jda.api.entities.Guild guild, String authorAvatar) {
        EmbedBuilder moderation = new EmbedBuilder()
                .setAuthor("moderation", null, authorAvatar)
                .setColor(Manager.getUtilities().gray)
                .addField("`" + Prefix.getPrefix(guild) + "moderation`", "\uD83D\uDD28 │ Display all moderation commands", false)
                .addField("`" + Prefix.getPrefix(guild) + "clear <amount>`", "\uD83D\uDDD1 │ Clear a certain amount of messages", false)
                .addField("`" + Prefix.getPrefix(guild) + "kick <user>`", "\uD83D\uDCE4 │ Kick a user", false)
                .addField("`" + Prefix.getPrefix(guild) + "nick <user>`", "\uD83D\uDD75 │ Change a users nickname", false)
                .addField("`" + Prefix.getPrefix(guild) + "mute role <role>`", "\uD83D\uDCDD │ Change the mute role", false)
                .addField("`" + Prefix.getPrefix(guild) + "unmute <user>`", "\uD83D\uDD08 │ Unmute a user", false)
                .addField("`" + Prefix.getPrefix(guild) + "tempmute <user> <duration><time unit> <reason>`", "\u23F1\uFE0F │ Mute a user for a certain amount of time", false)
                .addField("`" + Prefix.getPrefix(guild) + "mute <user>`", "\uD83D\uDD07 │ Mute a user", false)
                .addField("`" + Prefix.getPrefix(guild) + "unban <user>`", "\uD83D\uDD13 │ Unban a user", false)
                .addField("`" + Prefix.getPrefix(guild) + "tempban <user> <duration><time unit> <reason>`", "\u23F1\uFE0F │ Ban a user for a certain amount of time", false)
                .addField("`" + Prefix.getPrefix(guild) + "ban <user> <reason>`", "\uD83D\uDD12 │ Ban a user", false);
        return moderation;
    }

    //administrator
    public EmbedBuilder administrator(net.dv8tion.jda.api.entities.Guild guild, String authorAvatar) {
        EmbedBuilder administrator = new EmbedBuilder()
                .setAuthor("administrator", null, authorAvatar)
                .setColor(Manager.getUtilities().gray)
                .addField("`" + Prefix.getPrefix(guild) + "prefix <prefix>`", "\uD83D\uDCCC │ Change the prefix of the bot", false)
                .addField("`" + Prefix.getPrefix(guild) + "toggle <command>`", "\uD83D\uDD11 │ Toggle commands on or off", false)
                .addField("`" + Prefix.getPrefix(guild) + "say <message>`", "\uD83D\uDCAC │ Let the bot say something", false)
                .addField("`@someone`", "\uD83C\uDFB2 │ Replace in your message `@someone` with a random mention of a member", false)
                .addField("`" + Prefix.getPrefix(guild) + "autorole @role`", "\uD83D\uDCDD │ Give a new joined member automatic a certain role", false)
                .addField("`" + Prefix.getPrefix(guild) + "welcome`", "\uD83D\uDC4B │ Welcome new users", false)
                .addField("`" + Prefix.getPrefix(guild) + "notification`", "\uD83D\uDD14 │ set automatic notifications for you favorite streamers", false)
                .addField("`" + Prefix.getPrefix(guild) + "suggestions`", "\uD83D\uDDF3 │ Set up the suggestions", false)
                .addField("`" + Prefix.getPrefix(guild) + "leveling`", "\uD83C\uDFC6 │ Change some settings of leveling", false);
        return administrator;
    }


    //support server
    public EmbedBuilder supportServer(JDA jda, String authorAvatar) {
        EmbedBuilder invite = new EmbedBuilder()
                .setAuthor("support", "https://discord.gg/nG4uKuB", authorAvatar)
                .setColor(Manager.getUtilities().blue)
                .setThumbnail(jda.getGuildById("642809436515074053").getIconUrl())
                .setDescription("\u26A0\uFE0F │ " + Manager.getUtilities().hyperlink("Report", "https://discord.gg/nG4uKuB") + " bugs and get " + Manager.getUtilities().hyperlink("help", "https://discord.gg/nG4uKuB") + "!");
        return invite;
    }

    //invite bot
    public EmbedBuilder inviteJda(JDA jda, String authorAvatar) {
        EmbedBuilder invite = new EmbedBuilder()
                .setAuthor("invite", Manager.getUtilities().inviteJda(jda), authorAvatar)
                .setColor(Manager.getUtilities().blue)
                .setThumbnail(jda.getSelfUser().getEffectiveAvatarUrl())
                .setDescription("[\u2709\uFE0F │ Invite me to your server!](" + Manager.getUtilities().inviteJda(jda) + " \"bot invite link\")");
        return invite;
    }
}