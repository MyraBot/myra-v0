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
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

@CommandSubscribe(
        name = "blackjack",
        aliases = {"bj"}
)
public class BlackJack extends Events implements Command {
    // Games
    private final static HashMap<String, Game> games = new HashMap<>();
    // Reactions
    private final static HashMap<String, User> reaction = new HashMap<>();

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
// Game
        // Search user in games
        boolean isPlaying = false;
        for (Game game : games.values()) {
            for (Player player : game.getPlayers()) {
                if (player.getPlayer().equals(ctx.getAuthor())) isPlaying = true;
            }
        }

        // User hasn't started a game yet
        if (!isPlaying) {
            if (!ctx.getArguments()[0].matches("\\d+")) {
                Utilities.getUtils().error(ctx.getChannel(), "blackjack", "\uD83C\uDCCF", "Invalid number", "Make sure you only use digits", ctx.getAuthor().getEffectiveAvatarUrl());
                return;
            }
            // Not enought money
            if (new Database(ctx.getGuild()).getMembers().getMember(ctx.getMember()).getBalance() < Integer.parseInt(ctx.getArguments()[0])) {
                Utilities.getUtils().error(ctx.getChannel(), "blackjack", "\uD83C\uDCCF", "You don't have enough money", "The bank doesn't want to lend you money anymore", ctx.getAuthor().getEffectiveAvatarUrl());
                return;
            }
            // Create new Object of a player
            final Player player = new Player(ctx.getAuthor());
            // Create new Object of a dealer
            final Player dealer = new Player(ctx.getEvent().getJDA().getSelfUser());
            // Create a game
            final Game game = new Game(Integer.parseInt(ctx.getArguments()[0]), player, dealer);

            // Get first cards
            final Card playerCard1 = getRandomCard(game);
            final Card playerCard2 = getRandomCard(game);
            final Card dealerCard1 = getRandomCard(game);
            final Card dealerCard2 = getRandomCard(game); // isn't shown

            // Add cards to players
            player.add(playerCard1, playerCard2);
            dealer.add(dealerCard1, dealerCard2);

// Get total values
            // If any total value is more than 21
            if (player.getValue() > 21 || dealer.getValue() > 21) {
                // If players cards are more than 21
                if (player.getValue() > 21) {
                    // If player has an ace, switch value
                    player.switchAce();
                }
                // If dealer cards are more than 21
                if (dealer.getValue() > 21) {
                    // If dealer has an ace, switch value
                    dealer.switchAce();
                }
            }
// Get cards as emotes
            // Get cards of player as emotes
            String playerCards = "";
            for (Card playerCard : player.getCards()) {
                playerCards += playerCard.getEmote(ctx.getEvent().getJDA()) + " ";
            }

            // Get cards of dealer as emotes
            String dealerCards = "";
            for (Card dealerCard : dealer.getCards()) {
                dealerCards += dealerCard.getEmote(ctx.getEvent().getJDA()) + " ";
            }

            // Get footer
            String footer = getFooterStart(game, player, dealer, ctx.getEvent());
            // Create embed
            EmbedBuilder match = new EmbedBuilder()
                    .setAuthor("blackjack", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Utilities.getUtils().blue)
                    // Value of player cards
                    .addField("Your cards: " + player.getValue(),
                            // Player cards
                            playerCards,
                            false)
                    // Value of dealer cards
                    .addField("Dealer shows:",
                            // Player cards
                            dealerCards,
                            false);
            // Update message
            Message message = ctx.getChannel().sendMessage(match.build()).complete();
            // Add reactions
            message.addReaction("\u23CF").queue(); // Hit [⏏]
            message.addReaction("\u23F8").queue(); // Stay [⏸]
            // Create a new game and add all players
            games.put(message.getId(), game);
            // Add message to 'reaction' HashMap
            reaction.put(message.getId(), ctx.getAuthor());
        }
        // User plays a already existing game
        else {
            Utilities.getUtils().error(ctx.getChannel(), "blackjack", "", "Can't start a new game", "You're already playing a game lol", ctx.getAuthor().getEffectiveAvatarUrl());
        }
    }


    private String getFooterStart(Game game, Player player, Player dealer, GuildMessageReceivedEvent event) {
        // Get member form database
        final GetMember dbMember = new Database(event.getGuild()).getMembers().getMember(event.getMember());
        // Get current balance
        final int balance = dbMember.getBalance();
// Lost game
        // If both values are 21
        if (player.getValue() == dealer.getValue() && player.getValue() == 21) {
            // Remove money from member
            dbMember.setBalance(balance - game.getBetMoney());
            return "The dealer won!";
        }
        // If dealer's value is 21
        if (dealer.getValue() == 21) {
            // Remove money from member
            dbMember.setBalance(balance + game.getBetMoney());
            return "The dealer won!";
        }
// Won game
        if (player.getValue() == 21) {
            // Add money to member
            dbMember.setBalance(balance + game.getBetMoney() * 2);
            return "You won! +" + game.getBetMoney() * 2;
        }
// Continue game
        return "Hit or stay?";
    }

    public void guildMessageReactionAddEvent(GuildMessageReactionAddEvent event) throws Exception {
        // Wrong message
        if (reaction.get(event.getMessageId()) == null) return;
        // Wrong user
        if (!reaction.get(event.getMessageId()).equals(event.getUser())) return;

        // Get current game
        Game game = games.get(event.getMessageId());
        // Get players
        Player player = null;
        Player dealer = null;
        for (Player user : game.getPlayers()) {
            // Get player
            if (!user.getPlayer().isBot()) player = user;
            // Get dealer
            if (user.getPlayer().isBot()) dealer = user;
        }

        // Hit
        if (event.getReactionEmote().getEmoji().equals("\u23CF")) { // Hit [⏏]
            hit(game, player, dealer, event);
        }
        // Stay
        if (event.getReactionEmote().getEmoji().equals("\u23F8")) {// Stay [⏸]
            stay(game, player, dealer, event);
        }
    }

    /**
     * Hit method.
     *
     * @param game
     * @param player
     * @param dealer
     * @param event
     */
    private void hit(Game game, Player player, Player dealer, GuildMessageReactionAddEvent event) {
        // Create new Card
        Card card = getRandomCard(game);
        // Add new card to player
        player.add(card);

        // If player's card values are more than 21
        if (player.getValue() > 21) {
            // Switch the value of the ace card
            player.switchAce();
        }
// Update message
        // Get footer
        String footer = getFooterHit(game, player, dealer, event);
        // Update message
        updateMessage(event.getUser(), player, dealer, event.getJDA(), event.getChannel(), event.getMessageId(), event.getReaction(), footer);
    }

    private String getFooterHit(Game game, Player player, Player dealer, GuildMessageReactionAddEvent event) {
        // Get member form database
        final GetMember dbMember = new Database(event.getGuild()).getMembers().getMember(event.getMember());
        // Get current balance
        final int balance = dbMember.getBalance();
// Lost game
        // If player's value is more than 21
        if (player.getValue() > 21) {
            // Remove money from member
            dbMember.setBalance(balance - game.getBetMoney());
            return "The dealer won!";
        }
        // If both dealer's value is 21
        if (dealer.getValue() == 21) {
            // Remove money from member
            dbMember.setBalance(balance + game.getBetMoney());
            return "The dealer won!";
        }
// Won game
        if (player.getValue() == 21) {
            // Add money to member
            dbMember.setBalance(balance + game.getBetMoney() * 2);
            return "You won! +" + game.getBetMoney() * 2;
        }
// Continue game
        return "Hit or stay?";
    }

    /**
     * Stay method.
     *
     * @param game
     * @param player
     * @param dealer
     * @param event
     */
    private void stay(Game game, Player player, Player dealer, GuildMessageReactionAddEvent event) {
        // If dealer's value is below 17
        while (dealer.getValue() < 17) {
            // Get new card
            Card card = getRandomCard(game);
            // Add card to dealer
            dealer.add(card);
        }
// End game
        // Get footer
        String footer = getFooterStay(game, player, dealer, event);
        // Update message
        updateMessage(event.getUser(), player, dealer, event.getJDA(), event.getChannel(), event.getMessageId(), event.getReaction(), footer);
        // Remove game
        games.remove(event.getMessageId());
        reaction.remove(event.getMessageId());
    }

    private String getFooterStay(Game game, Player player, Player dealer, GuildMessageReactionAddEvent event) {
        // Get member form database
        final GetMember dbMember = new Database(event.getGuild()).getMembers().getMember(event.getMember());
        // Get current balance
        final int balance = dbMember.getBalance();
// Lost game
        // If both dealer's value more than player's value and not more 21
        if (dealer.getValue() > player.getValue() && dealer.getValue() <= 21) {
            // Remove money from member
            dbMember.setBalance(balance + game.getBetMoney());
            return "The dealer won!";
        }
// Won game
        if (dealer.getValue() > 21) {
            // Add money to member
            dbMember.setBalance(balance + game.getBetMoney() * 2);
            return "You won! +" + game.getBetMoney() * 2;
        }
// Continue game
        return "Hit or stay?";
    }

    private Message updateMessage(User author, Player player, Player dealer, JDA jda, TextChannel channel, String messageId, MessageReaction messageReaction, String footer) {
        // Get cards of player as emotes
        String playerCards = "";
        for (Card playerCard : player.getCards()) {
            playerCards += playerCard.getEmote(jda) + " ";
        }

        // Get cards of dealer as emotes
        String dealerCards = "";
        for (Card dealerCard : dealer.getCards()) {
            dealerCards += dealerCard.getEmote(jda) + " ";
        }

        EmbedBuilder match = new EmbedBuilder()
                .setAuthor("blackjack", null, author.getEffectiveAvatarUrl())
                .setColor(Utilities.getUtils().blue)
// Value of player cards
                .addField("Your cards: " + player.getValue(),
                        // Player cards
                        playerCards,
                        false);
// Value of dealer cards
        // Game continues
        if (footer.equals("Hit or stay?")) {
            match.addField("Dealer shows:",
                    // Dealer cards
                    dealerCards,
                    false);
            // Reset reaction
            messageReaction.removeReaction(author).queue();
        }
        // Game ends
        else {
            match.addField("Dealer cards: " + dealer.getValue(),
                    // Dealer cards
                    dealerCards,
                    false);
            // Clear reactions
            messageReaction.clearReactions().queue();
            // End game
            games.remove(messageId);
            reaction.remove(messageId);
        }
// Footer
        match.setFooter(footer);
        // Update message
        return channel.editMessageById(messageId, match.build()).complete();
    }

    private Card getRandomCard(Game game) {
        // Get left cards
        List<Card> leftCards = game.getLeftCards();
        // Generate a random number
        int random = new Random().nextInt(leftCards.size() - 1);
        // Remove the cart from the game
        game.removeCard(leftCards.get(random));
        // Return a card
        return leftCards.get(random);
    }
}
