package modules;

import java.util.ArrayList;

public class Mano {
    private Player player;
    private ArrayList<Card> cards = new ArrayList<Card>();
    private ArrayList<Card> validCards;

    public Mano(Player _player, ArrayList<Card> _cards) {
        this.player = _player;
        this.cards = _cards;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public int getNumberOfCards() {
        return this.cards.size();
    }

    public ArrayList<Card> getValidCards(Card _tableCenter, String _suit) {
        validCards = new ArrayList<Card>();
        for (Card c : cards) {
            if (c.isValid(_tableCenter, _suit))
                validCards.add(c);
        }
        return this.validCards;
    }

    public Card removeCard() {
        int n = (int) (Math.random() * cards.size());
        Card card = cards.remove(n);
        return card;
    }

    public Card removeCard(Card c) {
        cards.remove(c);
        return c;
    }

    public void addCard(Card _card) {
        if (_card != null) {
            cards.add(_card);
        }
    }

    @Override
    public String toString() {
        String player = "hand" + getPlayer().getNumber();
        String hand = String.format("<div class='%s'>", player);
        float n = getNumberOfCards() / 2;
        float rotation = (float)Math.floor(n) * -0.02f;
        for (int i = 0; i < getNumberOfCards(); i++) {
            rotation = (getNumberOfCards() % 2 == 0 && rotation == 0) ? 0.02f : rotation;
            if ((i + 0.5) < n) {
                this.cards.get(i).rotateCard("right", Float.toString(rotation));
            } else if ((i + 0.5) > n) {
                this.cards.get(i).rotateCard("left", Float.toString(rotation));
            } else {
                this.cards.get(i).rotateCard("center", "0");
            }
            hand += this.cards.get(i);
            rotation += 0.02f; 
        }
        return hand + "</div>";
    }
}
