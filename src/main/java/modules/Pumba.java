package modules;

import java.util.ArrayList;
import modules.Enums.Suits;

public class Pumba {
    private CardsDeck drawPile;
    private ArrayList<Card> discardPile;
    private ArrayList<Player> players;
    private int turn;
    private int numberOfPlayers;
    private boolean isGameStarted;
    private int draw2;
    private int playDirection;
    private String suit;

    public Pumba(int _numberOfPlayers) {
        this.drawPile = new CardsDeck();
        this.discardPile = new ArrayList<>();
        this.players = new ArrayList<>();
        this.numberOfPlayers = _numberOfPlayers;
        this.isGameStarted = false;
        this.draw2 = 2;
        this.playDirection = 1;
        this.suit = null;
        scoreCards();
        generatePlayers();
        dealCards();
    }

    public ArrayList<Card> getDrawPile() {
        System.out.println("Cartas en mazo: " + this.drawPile.getCards().size());
        return this.drawPile.getCards();
    }

    public ArrayList<Card> getDiscardPile() {
        System.out.println("Cartas en descartes: " + this.discardPile.size());
        return this.discardPile;
    }

    public Card getTableCenter() {
        if (this.discardPile.size() == 0)
            return null;
        return this.discardPile.get(this.discardPile.size() - 1);
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    public void setNumberOfPlayers(int _numberOfPlayers) {
        this.numberOfPlayers = _numberOfPlayers;
    }

    public int getTurn() {
        return this.turn;
    }

    public Pumba setTurn() {
        this.turn += this.playDirection;
        this.turn %= this.numberOfPlayers;
        if (this.turn == -1)
            this.turn = this.numberOfPlayers - 1;
        System.out.println("-------SET: " + this.getPlayerOfTurn().getPlayerName().toUpperCase());
        return this;
    }

    public int getNextTurn() {
        int nextTurn = this.turn + this.playDirection;
        nextTurn %= this.numberOfPlayers;
        if (nextTurn == -1)
            nextTurn = this.numberOfPlayers - 1;
        System.out.println("Próximo jugador " + (nextTurn + 1));
        return nextTurn;
    }

    public boolean isStarted() {
        return this.isGameStarted;
    }

    public void setGameStarted(boolean _isGameStarted) {
        this.isGameStarted = _isGameStarted;
    }

    public Player getPlayerOfTurn() {
        return this.players.get(turn);
    }

    public void dropOnDiscardPile(Card _card) {
        float rotation = -0.004f + (float) (Math.random() * (0.004f + 0.004f));
        _card.setUncovered(true).rotateCard("center", Float.toString(rotation));
        discardPile.add(_card);
    }

    public Card drawCardFromDeck() {
        return drawCardsFromDeck(1).get(0);
    }

    public ArrayList<Card> drawCardsFromDeck(int n) {
        ArrayList<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Card c = drawPile.drawFirstCard();
            if (c == null) {
                this.flipDiscardsPile();
                c = drawPile.drawFirstCard();
            }
            if (c != null) {
                drawnCards.add(c);
            }
        }
        return drawnCards;
    }

    public void flipDiscardsPile() {
        System.out.println("VOLTEAR DESCARTES");
        Card lastCard = this.discardPile.remove(this.discardPile.size() - 1);
        drawPile.returnCards(this.discardPile);
        drawPile.shuffle();
        this.discardPile = new ArrayList<>();
        this.discardPile.add(lastCard);
    }

    public Pumba reverseDirection() {
        System.out.println("CAMBIO SENTIDO");
        this.playDirection *= -1;
        return this;
    }

    public String changeSuit(String _suitOnTable) {
        String suit;
        do {
            suit = Suits.getRandom().toString().toLowerCase();
        } while (suit.equals(_suitOnTable));
        return changeSuit(suit, _suitOnTable);
    }

    public String changeSuit(String _suit, String _suitOnTable) {
        this.suit = _suit;
        System.out.println("---CAMBIO DE PALO A " + _suit.toUpperCase());
        return this.suit;
    }

    public Player choosePlayer() {
        int n;
        do {
            n = (int) (Math.random() * this.getNumberOfPlayers());
        } while (this.getTurn() == n);
        Player chosenPlayer = players.get(n);
        System.out.println("--" + chosenPlayer.getPlayerName(true) + " elegido para robar");
        return chosenPlayer;
    }

    public Card special2Play(Player _player, Card _tableCenter) {
        Card droppedCard = null;
        if (this.draw2 == -1) {
            droppedCard = _player.dropValidCard(_tableCenter, _tableCenter.getSuit());
            if (droppedCard != null) {
                this.draw2 = 2;
            }
            return droppedCard;
        }
        if (this.draw2 != 8)
            droppedCard = _player.dropValidCard(_tableCenter);
        if (droppedCard == null) {
            System.out.println("---" + _player.getPlayerName() + " chupa: " + this.draw2 + " cartas---");
            _player.drawCards(draw2);
            this.draw2 = -1;
        } else {
            this.draw2 += 2;
        }
        return droppedCard;
    }

    public String runPlay(String _playedCard) {
        Player player = this.getPlayerOfTurn();
        System.out.println("\nTURNO: " + player.getPlayerName().toUpperCase());
        String game = "";
        Card droppedCard;
        String play = "";
        String turn = "";
        boolean playAgain = true;
        Card tableCenter = this.getTableCenter();
        System.out.println("Esta es la carta jugada: " + _playedCard);
        if (!this.isStarted()) {
            this.setGameStarted(true);
            droppedCard = player.dropCard();
            play = String.format(" echa %s", droppedCard.getStringCard());
        } else {
            if (tableCenter.getNumber().equals("dos")) {
                int numberOfCards = player.getNumberOfCardsInHand();
                droppedCard = special2Play(player, tableCenter);
                int drawnCards = player.getNumberOfCardsInHand() - numberOfCards;
                if ((this.draw2 == -1 && drawnCards > 1) || drawnCards > 1) {
                    play = String.format(" chupa %d cartas", drawnCards);
                } else {
                    if (droppedCard == null) {
                        player.drawCard();
                        play = " roba carta";
                    } else {
                        play = String.format(" echa %s", droppedCard.getStringCard());
                    }
                }
            } else {
                droppedCard = player.dropValidCard(tableCenter, this.suit);
                if (this.suit != null && droppedCard != null) {
                    System.out.println("-Reset palo");
                    this.suit = null;
                }
                if (droppedCard == null) {
                    if (tableCenter.getNumber().equals("rey")) {
                        System.out.println("--YA NO REPITE TURNO");
                        playAgain = false;
                    }
                    player.drawCard();
                    play = " roba carta";
                } else {
                    play = String.format(" echa %s", droppedCard.getStringCard());
                }
            }
        }
        if (player.getNumberOfCardsInHand() == 0) {
            System.out.println("GANA " + player.getPlayerName().toUpperCase());
            return String.format("FIN DE LA PARTIDA, ¡GANA EL JUGADOR %s!", player.getPlayerName().toUpperCase());
        }
        game = player.getPlayerName(true) + play;
        if (droppedCard != null) {
            if (droppedCard.getNumber().equals("siete")) {
                this.reverseDirection();
                game += ". Cambio de sentido";
            } else if (droppedCard.getNumber().equals("caballo")) {
                System.out.println("SALTA TURNO: no juega " + this.getPlayerOfTurn().getPlayerName());
                this.setTurn();
                game += String.format(". Salta el turno del %s", this.getPlayerOfTurn().getPlayerName());
            } else if (droppedCard.getNumber().equals("sota")) {
                game += String.format(" y cambia de palo a %s", this.changeSuit(droppedCard.getSuit()));
            } else if (droppedCard.getNumber().equals("as")) {
                Player chosenPlayer = this.choosePlayer();
                chosenPlayer.drawCard();
                game += String.format(". Elige al jugador %s para que robe carta", chosenPlayer.getPlayerName());
            }
        }
        player = this.setTurn().getPlayerOfTurn();
        turn = player.isMachine() ? String.format(". Turno del %s.", player.getPlayerName())
                : ". Es tu turno, elige carta.";
        if (droppedCard != null && playAgain && droppedCard.getNumber().equals("rey")) {
            player = reverseDirection().setTurn().getPlayerOfTurn();
            System.out.println("REY - VUELVE A JUGAR: " + this.getPlayerOfTurn().getPlayerName());
            turn = player.isMachine() ? String.format(". Vuelve a jugar el %s.", player.getPlayerName())
                    : ". Vuelves a jugar.";
        }
        return game + turn;
    }

    private void scoreCards() {
        for (Card c : drawPile.getCards()) {
            int score;
            switch (c.getNumber()) {
                case "as":
                    score = 1;
                    break;
                case "tres":
                    score = 3;
                    break;
                case "cuatro":
                    score = 4;
                    break;
                case "cinco":
                    score = 5;
                    break;
                case "seis":
                    score = 6;
                    break;
                case "siete":
                    score = 7;
                case "caballo":
                    score = 9;
                default:
                    score = 10;
            }
            ;
            c.setScore(score);
        }
    }

    private void generatePlayers() {
        this.turn = (int) (Math.random() * this.numberOfPlayers);
        for (int i = 0; i < this.numberOfPlayers; i++) {
            boolean isMano = (this.turn == i) ? true : false;
            players.add(new Player(this, i + 1, isMano));
        }
    }

    /**
     * Se reparte una mano de cartas a cada jugador. Las cartas repartidas se
     * retiran del mazo. Si el jugador es la persona, las cartas se colocan boca
     * arriba.
     */
    private void dealCards() {
        for (Player j : players) {
            int n = j.isMano() ? 5 : 4;
            ArrayList<Card> cards = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                Card c = drawPile.drawCard();
                if (!j.isMachine())
                    c.setUncovered(true);
                cards.add(c);
            }
            j.receiveHand(cards);
        }
    }

}
