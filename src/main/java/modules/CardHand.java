package modules;

import java.util.ArrayList;
import java.util.Arrays;

public class CardHand {
    private Player player;
    private ArrayList<Card> cards = new ArrayList<Card>();

    public CardHand(Player _player, ArrayList<Card> _cards) {
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

    public ArrayList<Card> getValidCards(Card _cardOnTable, String _suit) {
        ArrayList<Card> validCards = new ArrayList<Card>();
        for (Card c : cards) {
            if (c.isValid(_cardOnTable, _suit)) {
                validCards.add(c);
            }
        }
        return validCards;
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

    private float setRotation(Card card, int i, float rotation) {
        float n = getNumberOfCards() / 2;
        if (rotation == 0 && getNumberOfCards() % 2 == 0)
            rotation = 0.02f;
        if ((i + 0.5) < n)
            card.rotateCard("right", Float.toString(rotation));
        else if ((i + 0.5) > n)
            card.rotateCard("left", Float.toString(rotation));
        else
            card.rotateCard("center", "0");
        return (rotation += 0.02f);
    }

    private String getCardHandStr() {
        String cardHand = "";
        float rotation = (float) Math.floor(getNumberOfCards() / 2) * -0.02f;
        for (int i = 0; i < getNumberOfCards(); i++) {
            Card card = this.cards.get(i);
            rotation = this.setRotation(card, i, rotation);
            if (this.player.isMachine())
                cardHand += card.toString();
            else {
                Card cardOnTable = this.player.checkCardOnTable();
                String suit = this.player.getGame().getSuit();
                ArrayList<Card> validCards = this.getValidCards(cardOnTable, suit);
                if (this.player.getGame().getTurn() == 0 && (cardOnTable == null || validCards.contains(card)))
                    cardHand += card.toStringAnchorTag(false);
                else
                    cardHand += card.toStringAnchorTag(true);
            }
        }
        return cardHand;
    }

    @Override
    public String toString() {
        String handClass = "hand" + getPlayer().getNumber();
        return Utilities.printDiv(getCardHandStr(), handClass);
    }
}
