package com.myra.dev.marian;

import com.myra.dev.marian.utilities.management.Events;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;

public class InviteThanks extends Events {

    public void guildJoinEvent(GuildJoinEvent event) {
        // Get direct messages channel from the owner
        final PrivateChannel dms = event.getGuild().getOwner().getUser().openPrivateChannel().complete();
        // Create embed
        final EmbedBuilder thank = new EmbedBuilder()
                .setAuthor("Thank you for inviting me", null, event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                .setDescription("Thank you for inviting me to " + event.getGuild().getName() + ". I'm still in developing, so if you find any bugs please report it! For suggestions you can also join the " + Manager.getUtilities().hyperlink("support server", "https://discord.gg/nG4uKuB") + ".");
        dms.sendMessage(thank.build()).queue();
    }
}
