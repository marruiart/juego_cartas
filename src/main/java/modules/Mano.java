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

    public ArrayList<Card> getValidCards(Card _cardOnTable, String _suit) {
        validCards = new ArrayList<Card>();
        for (Card c : cards) {
            if (c.isValid(_cardOnTable, _suit))
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
            if (this.player.getGame().getTurn() == 0 && !this.player.isMachine()
                    && card.isValid(player.checkCardOnTable()))
                cardHand += card.toStringAnchorTag(false);
            else if (!this.player.isMachine())
                cardHand += card.toStringAnchorTag(true);
            else
                cardHand += card.toString();
        }
        return cardHand;
    }

    @Override
    public String toString() {
        String handClass = "hand" + getPlayer().getNumber();
        return String.format("<div class='%s'>%s</div>", handClass, getCardHandStr());
    }
}
