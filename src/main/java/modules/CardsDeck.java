package modules;

import java.util.*;
import modules.Enums.*;

public class CardsDeck {
    private ArrayList<Card> cards = new ArrayList<Card>();

    public CardsDeck() {
        generateDeck();
        shuffle();
    }

    public ArrayList<Card> getCards() {
        for (Card c : this.cards)
            c.setUncovered(false).setLink(null);
        return this.cards;
    }

    public Card drawCard() {
        int n = (int) (Math.random() * cards.size());
        return drawCard(n);
    }

    public Card drawCard(int _position) {
        if (cards.size() == 0) {
            return null;
        }
        Card card = cards.remove(_position);
        return card;
    }

    public Card drawFirstCard() {
        if (cards.size() == 0) {
            return null;
        }
        Card card = cards.remove(0);
        return card;
    }

    public Card drawLastCard() {
        if (this.cards.size() == 0) {
            return null;
        }
        Card card = this.cards.remove(this.cards.size() - 1);
        return card;
    }

    public void returnCard(Card _card) {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(_card);
        returnCards(cards);
    }

    public void returnCards(ArrayList<Card> _cards) {
        for (Card c : _cards) {
            c.setUncovered(false).setLink(null);
            this.cards.add(c);
        }
    }

    public void generateDeck() {
        Suits[] suits = Suits.values();
        Numbers[] numbers = Numbers.values();
        for (Suits p : suits) {
            for (Numbers n : numbers) {
                cards.add(new Card(n, p));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }
}
