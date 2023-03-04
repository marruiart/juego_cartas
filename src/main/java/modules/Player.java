package modules;

import java.util.ArrayList;

public class Player {
    private String name;
    private Pumba game;
    private int number;
    private CardHand cardHand;
    private boolean isMano;
    private boolean isMachine;
    private Integer score;

    public Player(Pumba _game, int _number, boolean _isMano) {
        this(null, _game, _number, _isMano);
    }

    public Player(String _name, Pumba _game, int _number, boolean _isMano) {
        this.game = _game;
        this.number = _number;
        this.name = _name == null ? String.format("jugador %d", this.number) : _name;
        this.isMano = _isMano;
        this.isMachine = _number == 1 ? false : true;
        this.score = 0;
    }

    /* GETTERS Y SETTERS */

    public String getPlayerName() {
        return getPlayerName(false);
    }

    public String getPlayerName(boolean _firstCapital) {
        if (_firstCapital)
            return Utilities.firstToCapital(this.name);
        else
            return String.format(this.name.toLowerCase());
    }

    public Pumba getGame() {
        return this.game;
    }

    public int getNumber() {
        return this.number;
    }

    public CardHand getCardHand() {
        return this.cardHand;
    }

    public int getNumberOfCardsInHand() {
        return this.cardHand.getNumberOfCards();
    }

    public boolean isMano() {
        return this.isMano;
    }

    public boolean isMachine() {
        return this.isMachine;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int _score) {
        this.score = _score;
    }

    public void updateScore(int _score) {
        this.score += _score;
    }

    /**
     * Convierte en un objeto Card el String pasado por parámetros.
     * 
     * @param _playedCard un String que debe tener formato numero_palo
     * @return una Card correspondiente al palo y al número del String.
     * @return en caso de que _playedCard no sea válido o sea null, se devuelve
     *         null.
     */
    public Card getPlayedCard(String _playedCard) {
        if (_playedCard == null)
            return null;
        String[] card = _playedCard.split("_");
        if (card.length != 2)
            return null;
        String number = card[0];
        String suit = card[1];
        Card tmp = new Card(number, suit);
        for (Card c : this.cardHand.getCards())
            if (c.equals(tmp))
                return c;
        return null;
    }

    /**
     * El jugador comprueba la carta que hay en el centro de la mesa.
     * 
     * @return la carta en el centro de la mesa
     */
    public Card checkCardOnTable() {
        return game.getCardOnTable();
    }

    /**
     * El jugador suelta una carta que cumpla con las condiciones del juego, en
     * general, con mismo palo o mismo número que hay en el centro de la mesa. La
     * carta se elimina de la mano del jugador.
     * 
     * @param _cardOnTable carta en el centro de la mesa.
     * @return la carta soltada
     */
    public Card dropValidCard(Card _cardOnTable, Card _playedCard) {
        return dropValidCard(_cardOnTable, _playedCard, null);
    }

    /**
     * El jugador suelta una carta que cumpla el palo que hay en juego. La carta se
     * elimina de la mano del jugador.
     * 
     * @param _cardOnTable carta en el centro de la mesa.
     * @param _changedSuit palo al que se juega
     * @return la carta soltada
     */
    public Card dropValidCard(Card _cardOnTable, Card _playedCard, String _changedSuit) {
        ArrayList<Card> validCards = this.cardHand.getValidCards(_cardOnTable, _changedSuit);
        if (validCards.size() == 0)
            return null;
        if (_playedCard == null) {
            if (this.isMachine()) {
                if (this.getNumberOfCardsInHand() == 2 && validCards.size() >= 1) {
                    this.getGame().setActivePumba(true);
                    System.out.println("--- PUMBA ACTIVO ---");
                }
            }
            return this.dropCard(validCards.get((int) (Math.random() * validCards.size())));
        } else
            return this.dropCard(_playedCard);
    }

    /**
     * El jugador suelta una carta aleatoria en la pila de descartes. La carta se
     * elimina de la mano del jugador.
     * 
     * @return la carta soltada
     */
    public Card dropCard() {
        return this.dropCard(null);
    }

    /**
     * El jugador suelta una carta concreta en la pila de descartes. La carta se
     * elimina de la mano del jugador.
     * 
     * @param c la carta a soltar
     * @return la carta soltada
     */
    public Card dropCard(Card c) {
        Card card = (c == null) ? this.cardHand.removeCard() : this.cardHand.removeCard(c);
        System.out.println("-> Suelta ** " + card.getCardName().toUpperCase() + " **");
        PumbaUtilities.dropOnDiscardPile(card, this.getGame().getDiscardPile());
        return card;
    }

    /**
     * El jugador roba una carta del mazo y la pone en su mano.
     */
    public void drawCard() {
        this.drawCards(1);
    }

    /**
     * El jugador roba n cartas del mazo y las pone en su mano.
     * 
     * @param n número de cartas robadas
     */
    public void drawCards(int n) {
        Pumba game = this.getGame();
        System.out.printf("%s roba %d carta%s\n", this.getPlayerName(), n, n == 1 ? "" : "s");
        ArrayList<Card> drawnCards = PumbaUtilities.drawCardsFromDeck(n, game.getDiscardPile(), game.getDrawPile());
        for (Card c : drawnCards) {
            if (!this.isMachine) {
                c.setUncovered(true);
            }
            cardHand.addCard(c);
        }
    }

    /**
     * El jugador recibe un conjunto de cartas que se añaden a su mano.
     * 
     * @param _cards ArrayList de las cartas recibidas
     * @return Mano del jugador con las cartas recibidas
     */
    public CardHand receiveHand(ArrayList<Card> _cards) {
        this.cardHand = new CardHand(this, _cards);
        return this.cardHand;
    }

    @Override
    public String toString() {
        int[][] seats = { { 2 }, { 1, 3 }, { 0, 2, 4 }, { 0, 1, 3, 4 }, { 0, 1, 2, 3, 4 } };
        String seat = "";
        if (this.isMachine()) {
            seat = "seat" + seats[game.getPlayers().size() - 2][this.getNumber() - 2];
        }
        String isManoTxt = isMano ? " (Mano)" : "";
        String isMachineTxt = isMachine ? "" : " (Tú)";
        String player = this.getPlayerName().replace(" ", "");
        String name = (this.getPlayerName(true) + isManoTxt + isMachineTxt);
        String score = game.isScoreRound() ? Utilities.printDiv(Integer.toString(this.score), "score" + this.number)
                : "";
        String content = score + Utilities.printDiv(name, "name") + this.cardHand.toString();
        return Utilities.printDiv(content, player, seat);
    }
}
