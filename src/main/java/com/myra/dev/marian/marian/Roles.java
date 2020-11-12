package com.myra.dev.marian.marian;

import com.myra.dev.marian.Bot;
import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Roles {

    public void jdaReady(ReadyEvent event) {
        // Get my server
        final Guild guild = event.getJDA().getGuildById(Bot.marianServer);

        unicorn(guild);
        designer(guild);
    }

    /**
     * Change every 15 minutes the colour of the unicorn role.
     *
     * @param marianServer My discord server.
     */
    private void unicorn(Guild marianServer) {
        // Get role role
        final Role role = marianServer.getRoleById("774210055259947008");
        Utilities.TIMER.scheduleAtFixedRate(() -> {
            // Get high saturated colour
            Random random = new Random();
            final float hue = random.nextFloat();
            final float saturation = 0.9f; //1.0 for brilliant, 0.0 for dull
            final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
            Color colour = Color.getHSBColor(hue, saturation, luminance);
            // Update colour
            role.getManager().setColor(colour).queue();
        }, 30, 15, TimeUnit.MINUTES);
    }

    /**
     * Give all members, who use Myra in their server the 'exclusive' role on my discord server.
     *
     * @param event The GuildMemberJoinEvent event.
     */
    public void exclusive(GuildMemberJoinEvent event) {
        // Wrong server
        if (!event.getGuild().getId().equals(Bot.marianServer)) return;
        // Get exclusive role
        final Role exclusiveRole = event.getGuild().getRoleById("774210055259947008");

        // Go trough every guild
        for (Guild guild : event.getJDA().getGuilds()) {
            // Member is owner of a server Myra is in
            if (guild.getOwner().equals(event.getUser())) {
                // Add exclusive role
                event.getGuild().addRoleToMember(event.getMember(), exclusiveRole).queue();
            }
        }
    }

    private void designer(Guild guild) {
        final Role DESIGNER = guild.getRoleById("775647035315322891");

        final Role legend = guild.getRoleById("698884600654594108");
        final Role god = guild.getRoleById("698884548783636500");
        final Role veteran = guild.getRoleById("698884462460534835");
        final Role advanced = guild.getRoleById("698884227059417128");
        final Role experienced = guild.getRoleById("698883418070450177");
        final Role designer = guild.getRoleById("698883364773429258");
        final Role lil = guild.getRoleById("698883381966143568");

        Role[] designerRoles = {legend, god, veteran, advanced, experienced, designer, lil};

        for (Member member : guild.getMembers()) {
            if (Arrays.stream(designerRoles).anyMatch(member.getRoles()::contains)) {
                guild.addRoleToMember(member, DESIGNER).queue();
            }
        }
    }
}
