package com.myra.dev.marian.commands.economy.blackjack;

import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Player {
    // List of all cards of a user
    private List<Card> playersCards = new ArrayList<>();
    private User player;

    // Constructor
    public Player(User user) {
        this.player = user;
    }

    // Get player
    public User getPlayer() {
        return this.player;
    }

    /**
     * @return Returns all cards of a player.
     */
    public List<Card> getCards() {
        return this.playersCards;
    }

    /**
     * Switch the value of the ace card.
     */
    public void switchAce() {
        Iterator<Card> iterator = playersCards.iterator();
        // Search cards for an ace
        while (iterator.hasNext()) {
            Card card = iterator.next();

            if (card.getValue() == 11) {
                // Remove old card
                playersCards.remove(card);
                // Add new one
                playersCards.add(Card.setValueToOne(card));
                return; // Return, so only 1 ace changes their value
            }
        }
    }

    /**
     * @return Returns the value of the cards.
     */
    public Integer getValue() {
        int value = 0;
        for (Card card : playersCards) {
            value += card.getValue();
        }
        return value;
    }

    /**
     * Add card to 'cards'
     *
     * @param card The card to add.
     */
    public void add(Card... card) {
        this.playersCards.addAll(Arrays.asList(card));
    }
}
