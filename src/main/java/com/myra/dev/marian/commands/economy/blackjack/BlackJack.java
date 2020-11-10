package com.myra.dev.marian.commands.economy.blackjack;

import com.myra.dev.marian.database.allMethods.Database;
import com.myra.dev.marian.database.allMethods.GetMember;
import com.myra.dev.marian.management.Events;
import com.myra.dev.marian.management.commands.Command;
import com.myra.dev.marian.management.commands.CommandContext;
import com.myra.dev.marian.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.HashMap;

@CommandSubscribe(
        name = "blackjack",
        aliases = {"bj"}
)
public class BlackJack extends Events implements Command {
    private static HashMap<String, Game> games = new HashMap<>();

    /**
     * Start game.
     *
     * @param ctx The command context.
     * @throws Exception
     */
    @Override
    public void execute(CommandContext ctx) throws Exception {
        // Usage
        if (ctx.getArguments().length != 1) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("blackjack", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Utilities.getUtils().gray)
                    .addField("`" + ctx.getPrefix() + "blackjack <bet>`", "\uD83C\uDCCF │ Play blackjack against " + ctx.getEvent().getJDA().getSelfUser().getName(), false);
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
// Errors
        // Search user in games
        boolean isPlaying = false;
        for (Game game : games.values()) {
            for (Player player : game.getPlayers()) {
                if (player.getPlayer().equals(ctx.getAuthor())) isPlaying = true;
            }
        }
        // If user has already started a game
        if (isPlaying) {
            Utilities.getUtils().error(ctx.getChannel(), "blackjack", "\uD83C\uDCCF", "You already started a game", "Please finish the game you started first", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // Invalid amount of money
        if (!ctx.getArguments()[0].matches("\\d+")) {
            Utilities.getUtils().error(ctx.getChannel(), "blackjack", "\uD83C\uDCCF", "Invalid number", "Make sure you only use digits", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
        // Not enough money
        if (new Database(ctx.getGuild()).getMembers().getMember(ctx.getMember()).getBalance() < Integer.parseInt(ctx.getArguments()[0])) {
            Utilities.getUtils().error(ctx.getChannel(), "blackjack", "\uD83C\uDCCF", "You don't have enough money", "The bank doesn't want to lend you money anymore", ctx.getAuthor().getEffectiveAvatarUrl());
            return;
        }
// Blackjack
        // Get all players
        final Player player = new Player(ctx.getAuthor());
        final Player dealer = new Player(ctx.getEvent().getJDA().getSelfUser());
        // Create a new Game
        final Game game = new Game(Integer.parseInt(ctx.getArguments()[0]), player, dealer);

        // Add new Cards to player
        player.add(game.getRandomCard(), game.getRandomCard());
        // Add new Cards to dealer
        dealer.add(game.getRandomCard(), game.getRandomCard());

        // If player's value is more than 21
        if (player.getValue() > 21) {
            // Switch ace
            player.switchAce();
        }
        // If dealer's value is more than 21
        if (dealer.getValue() > 21) {
            // Switch ace
            dealer.switchAce();
        }
        // Send match message
        final Message message = ctx.getChannel().sendMessage(getEmbed(player, dealer, game, ctx.getGuild()).build()).complete();

        // Game continues
        if (message.getEmbeds().get(0).getFooter().getText().equals("Hit or stay?")) {
            // Add reactions
            message.addReaction("\u23CF").queue(); // Hit
            message.addReaction("\u23F8").queue(); // Stay
            // Add game to 'games'
            games.put(message.getId(), game);
        }
    }


    public void guildMessageReactionAddEvent(GuildMessageReactionAddEvent event) {
        // Wrong user reacted to the message
        if (!games.get(event.getMessageId()).getPlayers().get(0).getPlayer().equals(event.getUser())) return;
        // Get game
        final Game game = games.get(event.getMessageId());
        // Get players
        final Player player = game.getPlayers().get(0);
        final Player dealer = game.getPlayers().get(1);
// Hit
        if (event.getReactionEmote().getEmoji().equals("\u23CF")) {
            // Add a new card to player
            player.add(game.getRandomCard());
            // If player's value is more than 21
            if (player.getValue() > 21) {
                // Switch ace value
                player.switchAce();
            }
            // Update match message
            final Message message = event.getChannel().editMessageById(event.getMessageId(), getEmbed(player, dealer, game, event.getGuild()).build()).complete();

            // gamed continues
            if (message.getEmbeds().get(0).getFooter().getText().equals("Hit or stay?")) {
                // Remove reaction
                event.getReaction().removeReaction(event.getUser()).queue();
            }
            // Game ended
            else {
                // Clear reaction
                event.getChannel().clearReactionsById(event.getMessageId()).queue();
                // Remove game
                games.remove(event.getMessageId());
            }

        }
//Stay
        if (event.getReactionEmote().getEmoji().equals("\u23F8")) {
            // Get database
            final GetMember dbMember = new Database(event.getGuild()).getMembers().getMember(event.getMember());
            // Add cards to the dealer until his card value is at least 17
            while (dealer.getValue() <= 17) {
                dealer.add(game.getRandomCard());
            }

            String footer = "";
// Won
            // Player has higher value than dealer and player's value is not more than 21
            if (player.getValue() > dealer.getValue() && player.getValue() <= 21) {
                // Set footer
                footer = "You won +" + game.getBetMoney() * 2 + "!";
                // Add money
                dbMember.setBalance(dbMember.getBalance() + game.getBetMoney());
            }
            // Dealer's value is more than 21
            else if (dealer.getValue() > 21) {
                // Set footer
                footer = "You won +" + game.getBetMoney() * 2 + "!";
                // Add money
                dbMember.setBalance(dbMember.getBalance() + game.getBetMoney());
            }
// Lost
            // Dealer has higher value than player and dealer's value is not more than 21
            if (dealer.getValue() > player.getValue() && dealer.getValue() <= 21) {
                // Set footer
                footer = "The dealer won!";
                // Remove money
                dbMember.setBalance(dbMember.getBalance() - game.getBetMoney());
            }
            // If dealer and player have the same value
            else if (player.getValue() == dealer.getValue()) {
                // Set footer
                footer = "The dealer won!";
                // Remove money
                dbMember.setBalance(dbMember.getBalance() - game.getBetMoney());
            }
            // Player's value is more than 21
            else if (player.getValue() > 21 && dealer.getValue() <= 21) {
                // Set footer
                footer = "The dealer won!";
                // Remove money
                dbMember.setBalance(dbMember.getBalance() - game.getBetMoney());
            }
            // Create match message
            EmbedBuilder match = new EmbedBuilder()
                    .setAuthor("blackjack", null, player.getPlayer().getEffectiveAvatarUrl())
                    .setColor(Utilities.getUtils().getMemberRoleColour(event.getMember()))
                    // Player cards
                    .addField("Your cards: " + player.getValue(), getPlayerCards(player, event.getJDA()), false)
                    // Dealer cards
                    .addField("Dealer cards: " + dealer.getValue(), getDealerCards(dealer, event.getJDA()), false)
                    .setFooter(footer);
            // Update message
            event.getChannel().editMessageById(event.getMessageId(), match.build()).queue();
            // Clear reaction
            event.getChannel().clearReactionsById(event.getMessageId()).queue();
            // Remove game
            games.remove(event.getMessageId());
        }
    }

    /**
     * @param player The player.
     * @param dealer The dealer.
     * @param game   The game.
     * @param guild  The guild.
     * @return Returns an embed for the match.
     */
    private EmbedBuilder getEmbed(Player player, Player dealer, Game game, Guild guild) {
        // Create embed
        EmbedBuilder match = new EmbedBuilder()
                .setAuthor("blackjack", null, player.getPlayer().getEffectiveAvatarUrl())
                .setColor(Utilities.getUtils().getMemberRoleColour(guild.getMember(player.getPlayer())))
                // Player cards
                .addField("Your cards: " + player.getValue(), getPlayerCards(player, guild.getJDA()), false);
        // Get member in database
        final GetMember dbMember = new Database(guild).getMembers().getMember(guild.getMember(player.getPlayer()));
// Lost
        // Dealer and player have a value of 21
        if (dealer.getValue() == player.getValue() && dealer.getValue() == 21) {
            // Remove balance
            dbMember.setBalance(dbMember.getBalance() - game.getBetMoney());
            match
                    .addField("Dealer cards: " + dealer.getValue(), getDealerCards(dealer, guild.getJDA()), false)
                    .setFooter("The dealer won!");
        }
        // Dealer's value is 21
        else if (dealer.getValue() == 21) {
            // Remove balance
            dbMember.setBalance(dbMember.getBalance() - game.getBetMoney());
            match
                    .addField("Dealer cards: " + dealer.getValue(), getDealerCards(dealer, guild.getJDA()), false)
                    .setFooter("The dealer won!");
        }
        // If player's value is more than 21
        else if (player.getValue() > 21) {
            // Remove balance
            dbMember.setBalance(dbMember.getBalance() - game.getBetMoney());
            match
                    .addField("Dealer cards: " + dealer.getValue(), getDealerCards(dealer, guild.getJDA()), false)
                    .setFooter("The dealer won!");
        }
// Won
        // Player's value is 21
        else if (player.getValue() == 21) {
            // Add balance
            dbMember.setBalance(dbMember.getBalance() + game.getBetMoney() * 2);
            match
                    .addField("Dealer cards: " + dealer.getValue(), getDealerCards(dealer, guild.getJDA()), false)
                    .setFooter("You won! +" + game.getBetMoney() * 2);
        }
        // Continue game
        else {
            match
                    .addField("Dealer shows:", getDealerCards(dealer, guild.getJDA()), false)
                    .setFooter("Hit or stay?");
        }
        return match;
    }

    /**
     * @param player The player.
     * @param jda    The jda entity.
     * @return Returns a String with the cards of the player as emotes.
     */
    private String getPlayerCards(Player player, JDA jda) {
        // Get cards of player as emotes
        String playerCards = "";
        for (Card playerCard : player.getCards()) {
            playerCards += playerCard.getEmote(jda) + " ";
        }
        return playerCards;
    }

    /**
     * @param dealer The dealer.
     * @param jda    The jda entity.
     * @return Returns a String with the cards of the dealer as emotes.
     */
    private String getDealerCards(Player dealer, JDA jda) {
        // Get cards of dealer as emotes
        String dealerCards = "";
        for (Card dealerCard : dealer.getCards()) {
            dealerCards += dealerCard.getEmote(jda) + " ";
        }
        return dealerCards;
    }
}
