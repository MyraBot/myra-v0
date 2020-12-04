package com.myra.dev.marian.commands.administrator.notifications;

import com.myra.dev.marian.APIs.YouTube;
import com.myra.dev.marian.database.managers.NotificationsYoutubeManager;
import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.Permissions;
import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.JSONObject;

import java.net.URL;

@CommandSubscribe(
        name = "notification youtube",
        aliases = {"notifications youtube", "notification youtuber", "notifications youtuber"},
        requires = Permissions.ADMINISTRATOR
)
public class YouTuber implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Usage
        if (ctx.getArguments().length == 0) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("notification youtube");
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }

        final String channel = Utilities.getUtils().getString(ctx.getArguments()); // Get the arguments as one string

        JSONObject channelInformation;
        // Get channel by url
        try {
            new URL(channel); // Try making a url out of it
            channelInformation = YouTube.getInstance().getChannelByUrl(channel); // Get channel information
        }
        // Get channel by name
        catch (Exception e) {
            channelInformation = YouTube.getInstance().getChannelByName(channel); // Get channel information
        }

        final String channelId = channelInformation.getString("channelId"); // get channel id
        final String channelName = channelInformation.getString("title"); // Get youtube channel name
        final String profilePicture = channelInformation.getJSONObject("thumbnails").getJSONObject("medium").getString("url"); // Get profile picture

        // Remove youtuber
        if (NotificationsYoutubeManager.getInstance().getYoutubers(ctx.getGuild()).contains(channelId)) {
            NotificationsYoutubeManager.getInstance().removeYoutuber(channelId, ctx.getGuild()); // Remove youtuber from notifications list

            EmbedBuilder success = new EmbedBuilder()
                    .setAuthor("notification youtube", "https://www.youtube.com/channel/" + channelId, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Utilities.getUtils().blue)
                    .setThumbnail(profilePicture)
                    .setDescription("Removed **" + channelName + "** from the notifications");
            ctx.getChannel().sendMessage(success.build()).queue(); // Send success message
        } else {
            NotificationsYoutubeManager.getInstance().addYoutuber(channelId, ctx.getGuild()); // Add youtuber to notifications list

            EmbedBuilder success = new EmbedBuilder()
                    .setAuthor("notification youtube", "https://www.youtube.com/channel/" + channelId, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Utilities.getUtils().blue)
                    .setThumbnail(profilePicture)
                    .setDescription("Added **" + channelName + "** to the notifications");
            ctx.getChannel().sendMessage(success.build()).queue(); // Send success message
        }
    }
}
