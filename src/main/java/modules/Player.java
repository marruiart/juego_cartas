package modules;

import java.util.ArrayList;

public class Player {
    private Pumba game;
    private int number;
    private Mano handCards;
    private boolean isMano;
    private boolean isMachine;

    public Player(Pumba _game, int _number, boolean _isMano) {
        this.game = _game;
        this.number = _number;
        this.isMano = _isMano;
        this.isMachine = _number == 1 ? false : true;
    }

    public Pumba getGame() {
        return this.game;
    }

    public int getNumber() {
        return this.number;
    }

    public Mano getMano() {
        return this.handCards;
    }

    public int getNumberOfCardsInHand() {
        return this.handCards.getNumberOfCards();
    }

    public String getPlayerName() {
        return getPlayerName(false);
    }

    public String getPlayerName(boolean _firstCapital) {
        if (_firstCapital)
            return String.format("Jugador %d", number);
        else
            return String.format("jugador %d", number);
    }

    public boolean isMano() {
        return this.isMano;
    }

    public boolean isMachine() {
        return this.isMachine;
    }

    public Card checkTableCenter() {
        return game.getTableCenter();
    }

    public Card dropValidCard(Card centroMesa) {
        return dropValidCard(centroMesa, null);
    }

    public Card dropValidCard(Card _tableCenter, String _changedSuit) {
        ArrayList<Card> validCards = this.handCards.getValidCards(_tableCenter, _changedSuit);
        if (validCards.size() == 0)
            return null;
        int n = (int) (Math.random() * validCards.size());
        return this.dropCard(validCards.get(n));
    }

    public Card dropCard() {
        return this.dropCard(null);
    }

    public Card dropCard(Card c) {
        Card card = (c == null) ? this.handCards.removeCard() : this.handCards.removeCard(c);
        System.out.println("Suelta " + card.getStringCard());
        card.setUncovered(true).setLink(null);
        game.dropOnDiscardPile(card);
        return card;
    }

    public void drawCard() {
        this.drawCards(1);
    }

    public void drawCards(int n) {
        System.out.printf("%s roba %d carta%s\n", this.getPlayerName(), n, n == 1 ? "" : "s");
        ArrayList<Card> drawnCards = game.drawCardsFromDeck(n);
        for (Card c : drawnCards) {
            if (!this.isMachine) {
                String link = String.format(
                        "<a href='http://localhost:8080/juegos_cartas/pumba.jsp?start=0&card=%s'>",
                        c.getCardNameLink());
                c.setUncovered(true).setLink(link);
            }
            handCards.addCard(c);
        }
    }

    public Mano receiveHand(Player jugador, ArrayList<Card> cartas) {
        this.handCards = new Mano(this, cartas);
        return this.handCards;
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
        return String.format(
                "<div class='%s %s'><div class='name'>%s %s%s</div>%s</div>", player, seat,
                this.getPlayerName(true), isManoTxt, isMachineTxt, this.handCards.toString());
    }
}
