package com.myra.dev.marian.commands.economy.blackjack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    // Values
    private int bet;
    private List<Card> leftCards;
    private List<Player> players = new ArrayList<>();

    // Constructor
    public Game(int bet, Player... player) {
        // Set bet
        this.bet = bet;
        // Add all cards to Array
        this.leftCards = Card.getAll();
        // Add all players
        this.players.addAll(Arrays.asList(player));
    }

    /**
     * @return Returns all cards, which aren't used yet.
     */
    public List<Card> getLeftCards() {
        return leftCards;
    }

    /**
     * @return Returns the bet money.
     */
    public Integer getBetMoney() {
        return bet;
    }

    /**
     * @return Returns all players in a List.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Remove a card from the 'leftCards' list
     *
     * @param card The card to remove.
     */
    public void removeCard(Card card) {
        leftCards.remove(card);
    }
}
