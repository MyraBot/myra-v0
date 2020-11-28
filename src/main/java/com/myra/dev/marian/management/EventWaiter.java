package com.myra.dev.marian.management;

import com.myra.dev.marian.commands.economy.Buy;
import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.database.allMethods.GetMember;
import com.myra.dev.marian.database.documents.ShopRolesDocument;
import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class EventWaiter {

    /**
     * @WaiterOf Buy
     */
    public void buy(GuildMessageReceivedEvent event) {
        if (!Buy.eventWaiter.containsKey(event.getMember().getId())) return; // Check if bot is waiting for a respond

        final String message = event.getMessage().getContentRaw(); // Get message
        final ShopRolesDocument role = Buy.eventWaiter.get(event.getMember().getId()); // Get role document
        // Sell role
        if (message.equalsIgnoreCase("yes") || message.equalsIgnoreCase("y")) { // Check if message is 'yes' or 'y'
            // Remove role
            try {
                event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(role.getId())).queue();
            }catch (Exception e) {
                // Role to buy is higher than bot
                if (e.toString().startsWith("net.dv8tion.jda.api.exceptions.HierarchyException: Can't modify a role with higher or equal highest role than yourself!")) {
                    Utilities.getUtils().error(event.getChannel(), "buy", "\uD83D\uDED2", "Couldn't remove this role", "I need to be higher the role", event.getAuthor().getEffectiveAvatarUrl());
                    return;
                } else e.printStackTrace();
            }
            EmbedBuilder sell = new EmbedBuilder()
                    .setAuthor("buy", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Utilities.getUtils().blue)
                    .setDescription("Sold " + event.getGuild().getRoleById(role.getId()).getAsMention() + " for " + role.getPrice() / 2 + new Database(event.getGuild()).getNested("economy").get("currency"));
            event.getChannel().sendMessage(sell.build()).queue();
            // Add money
            GetMember memberDb = new Database(event.getGuild()).getMembers().getMember(event.getMember()); // Get member in database
            memberDb.setBalance(memberDb.getBalance() + role.getPrice() / 2); // Add half of the price of the role to members balance
        }
        // Cancel selling
        else {
            EmbedBuilder cancel = new EmbedBuilder()
                    .setAuthor("buy", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Utilities.getUtils().blue)
                    .setDescription("Canceled selling " + event.getGuild().getRoleById(role.getId()).getAsMention());
            event.getChannel().sendMessage(cancel.build()).queue();
        }
        Buy.eventWaiter.remove(event.getMember().getId()); // Remove member from queue
    }
}
