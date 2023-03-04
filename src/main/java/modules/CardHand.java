package modules;

import java.util.ArrayList;

public class CardHand {
    private Player player;
    private ArrayList<Card> cards = new ArrayList<Card>();

    public CardHand(Player _player, ArrayList<Card> _cards) {
        this.player = _player;
        this.cards = _cards;
    }

    /* GETTERS Y SETTERS */

    public Player getPlayer() {
        return this.player;
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public int getNumberOfCards() {
        return this.cards.size();
    }

    /*** CARTAS VÁLIDAS ***/

    /**
     * Devuelve las cartas de la mano que son válidas en función de la carta que hay
     * en el centro de la mesa y del palo en juego.
     * 
     * @param _cardOnTable la carta del centro de la mesa
     * @param _suit        el palo en juego
     * @return un ArrayList<Card> con las cartas válidas
     */
    public ArrayList<Card> getValidCards(Card _cardOnTable, String _suit) {
        ArrayList<Card> validCards = new ArrayList<Card>();
        boolean isSelectionRound = this.player.getGame().isSelectionRound();
        for (Card c : this.cards) {
            if (!isSelectionRound && c.isValid(_cardOnTable, _suit))
                validCards.add(c);
        }
        return validCards;
    }

    public ArrayList<Card> printPlayer1ValidCards(Card _cardOnTable, String _suit) {
        ArrayList<Card> validCards = getValidCards(_cardOnTable, _suit);
        System.out.println("\n** TUS CARTAS VALIDAS **");
        for (Card c : validCards)
            System.out.printf("   * %s\n", c.getCardName());
        System.out.println("************************");
        return validCards;
    }

    /**
     * Quita una carta de la mano de forma aleatoria.
     * 
     * @return la carta retirada.
     */
    public Card removeCard() {
        int n = (int) (Math.random() * cards.size());
        Card card = cards.remove(n);
        return card;
    }

    /**
     * Quita una carta concreta de la mano.
     * 
     * @param _card la carta a retirar
     * @return la carta retirada
     */
    public Card removeCard(Card _card) {
        cards.remove(_card);
        return _card;
    }

    /**
     * Añade una carta concreta a la mano.
     * 
     * @param _card la carta a añadir
     */
    public void addCard(Card _card) {
        if (_card != null) {
            cards.add(_card);
        }
    }

    /**
     * Establece la rotación con la que se pintará la carta en pantalla.
     * 
     * @param _card     la carta a rotar
     * @param i         la posición de la carta en la mano
     * @param _rotation los grados que se rotará la carta
     * @return retorna un float con la rotación que se asignará a la siguiente carta
     *         de la mano
     */
    private float setRotation(Card _card, int i, float _rotation) {
        float n = getNumberOfCards() / 2;
        if (_rotation == 0 && getNumberOfCards() % 2 == 0)
            _rotation = 0.02f;
        if ((i + 0.5) < n)
            _card.rotateCard("right", Float.toString(_rotation));
        else if ((i + 0.5) > n)
            _card.rotateCard("left", Float.toString(_rotation));
        else
            _card.rotateCard("center", "0");
        return (_rotation += 0.02f);
    }

    /**
     * Asigna la rotación a cada una de las cartas de la mano y, si es el jugador
     * principal, determina qué cartas son válidas para asignarles un enlace
     * clickeable en su turno, o de lo contrario que el enlace esté desactivado.
     * 
     * @return un String con la mano de cartas con las etiquetas HTML adecuadas.
     */
    private String getCardHandStr() {
        String cardHand = "";
        float rotation = (float) Math.floor(getNumberOfCards() / 2) * -0.02f;
        if (this.player.isMachine()) {
            for (int i = 0; i < getNumberOfCards(); i++) {
                Card card = this.cards.get(i);
                rotation = this.setRotation(card, i, rotation);
                cardHand += card.toString();
            }
        } else {
            Pumba game = this.player.getGame();
            if (game.getTurn() == 0) {
                Card cardOnTable = this.player.checkCardOnTable();
                String suit = game.getSuit();
                ArrayList<Card> validCards = this.getValidCards(cardOnTable, suit);
                for (int i = 0; i < getNumberOfCards(); i++) {
                    Card card = this.cards.get(i);
                    rotation = this.setRotation(card, i, rotation);
                    if (cardOnTable == null || validCards.contains(card))
                        cardHand += card.toStringAnchorTag(false);
                    else
                        cardHand += card.toStringAnchorTag(true);
                }
            } else {
                for (int i = 0; i < getNumberOfCards(); i++) {
                    Card card = this.cards.get(i);
                    rotation = this.setRotation(card, i, rotation);
                    cardHand += card.toStringAnchorTag(true);
                }
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
