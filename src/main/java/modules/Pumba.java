package modules;

import java.util.ArrayList;
import java.util.HashMap;
import modules.Enums.Suits;

public class Pumba {
    private CardsDeck drawPile;
    private ArrayList<Card> discardPile;
    private ArrayList<Player> players;
    private int turn;
    private int numberOfPlayers;
    private int draw2;
    private int playDirection;
    private String suit;
    private Card playedCard;
    private boolean isScoreRound;

    public Pumba(int _numberOfPlayers) {
        this.drawPile = new CardsDeck();
        this.discardPile = new ArrayList<>();
        this.players = new ArrayList<>();
        this.numberOfPlayers = _numberOfPlayers;
        this.draw2 = 2;
        this.playDirection = 1;
        this.suit = null;
        this.playedCard = null;
        isScoreRound = false;
        scoreCards();
        generatePlayers();
        dealCards();
    }

    public ArrayList<Card> getDrawPile() {
        System.out.println(" ------------------------- ");
        System.out.println(String.format("| Cartas en mazo:      %2d |", this.drawPile.getCards().size()));
        return this.drawPile.getCards();
    }

    public ArrayList<Card> getDiscardPile() {
        System.out.println(String.format("| Cartas en descartes: %2d |", this.discardPile.size()));
        System.out.println(" ------------------------- ");
        return this.discardPile;
    }

    public Card getCardOnTable() {
        if (this.discardPile.size() == 0) {
            System.out.println("Mesa vacía");
            return null;
        }
        Card cardOnTable = this.discardPile.get(this.discardPile.size() - 1);
        System.out.println("Carta en mesa: " + cardOnTable.getCardName());
        return cardOnTable;
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

    public Player getPlayerOfTurn() {
        Player player = this.players.get(turn);
        System.out.println("\nTURNO: " + player.getPlayerName().toUpperCase());
        return player;
    }

    public String getSuit() {
        return this.suit;
    }

    public String getSuitStr() {
        if (this.suit == null) {
            Card cardOnTable = getCardOnTable();
            if (cardOnTable != null)
                return cardOnTable.getSuit();
            return "";
        }
        return this.suit;
    }

    public String getSuitImg() {
        if (this.suit == null) {
            Card cardOnTable = getCardOnTable();
            if (cardOnTable != null) {
                String src = String.format("assets/img/%s.png", cardOnTable.getSuit());
                return Utilities.printImg(src, cardOnTable.getSuit());
            }
            return Utilities.printImg(null, null);
        }
        String suitStr = getSuitStr();
        String src = suitStr.equals("") ? "" : String.format("assets/img/%s.png", suitStr);
        return Utilities.printImg(src, suitStr);
    }

    public boolean isScoreRound() {
        return this.isScoreRound;
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

    private void flipDiscardsPile() {
        System.out.println("VOLTEAR DESCARTES");
        Card lastCard = this.discardPile.remove(this.discardPile.size() - 1);
        drawPile.returnCards(this.discardPile);
        drawPile.shuffle();
        this.discardPile = new ArrayList<>();
        this.discardPile.add(lastCard);
    }

    private Pumba reverseDirection() {
        System.out.println("CAMBIO SENTIDO");
        this.playDirection *= -1;
        return this;
    }

    private String chooseSuit(String _suitOnTable) {
        String suit;
        do {
            suit = Suits.getRandom().toString().toLowerCase();
        } while (suit.equals(_suitOnTable));
        return changeSuit(suit);
    }

    private String changeSuit(String _suit) {
        this.suit = _suit;
        System.out.println("---CAMBIO DE PALO A " + _suit.toUpperCase());
        return this.suit;
    }

    private Player choosePlayer() {
        int n;
        do {
            n = (int) (Math.random() * this.getNumberOfPlayers());
        } while (this.getTurn() == n);
        Player chosenPlayer = players.get(n);
        System.out.println("--" + chosenPlayer.getPlayerName(true) + " elegido para robar");
        return chosenPlayer;
    }

    private Card special2Play(Player _player, Card _cardOnTable) {
        Card droppedCard = null;
        if (this.draw2 == -1) {
            changeSuit(_cardOnTable.getSuit());
            if (this.playedCard != null && this.playedCard.isDrawCard())
                return null;
            else {
                droppedCard = _player.dropValidCard(_cardOnTable, this.playedCard, this.suit);
                if (droppedCard != null) {
                    if (droppedCard.getNumber().equals("dos"))
                        changeSuit(droppedCard.getSuit());
                    this.draw2 = 2;
                }
                return droppedCard;
            }
        }
        if (this.draw2 != 8)
            droppedCard = _player.dropValidCard(_cardOnTable, this.playedCard);
        if (droppedCard == null) {
            System.out.println("---" + _player.getPlayerName() + " chupa: " + this.draw2 + " cartas---");
            _player.drawCards(draw2);
            changeSuit(_cardOnTable.getSuit());
            this.draw2 = -1;
        } else {
            this.draw2 += 2;
        }
        return droppedCard;
    }

    private void setPlayersScore() {
        this.isScoreRound = true;
        for (Player p : this.players) {
            int score = 0;
            for (Card c : p.getMano().getCards())
                score += c.getScore();
            p.updateScore(score);
        }
    }

    /**
     * Comprueba la carta que se ha jugado en el turno. Si es el jugador principal,
     * comprueba si ha robado carta del mazo.
     * 
     * @param player
     * @param _playedCard
     * @return la carta que se ha jugado
     * @return si el jugador principal roba, retorna una carta especial donde
     *         isDrawCard es true
     * @return null si se ha robado carta o no hay cartas válidas que jugar
     */
    private Card checkPlayedCard(Player player, String _playedCard) {
        if (player.getNumber() == 1 && _playedCard != null) {
            if (_playedCard.equals("draw_card")) {
                System.out.println("Robas carta del mazo");
                return new Card();
            } else {
                Card card = player.getPlayedCard(_playedCard);
                System.out.println("Esta es la carta jugada: " + card.getCardName());
                return card;
            }
        } else
            return null;
    }

    private String checkSpecialDroppedCard(Card droppedCard) {
        String gameMessage = "";
        if (droppedCard.getNumber().equals("siete")) {
            this.reverseDirection();
            gameMessage = ". Cambio de sentido";
        } else if (droppedCard.getNumber().equals("caballo")) {
            this.setTurn();
            System.out.println("SALTA TURNO: no juega " + this.getPlayerOfTurn().getPlayerName());
            gameMessage = String.format(". Salta el turno del %s", this.getPlayerOfTurn().getPlayerName());
        } else if (droppedCard.getNumber().equals("sota")) {
            gameMessage = String.format(" y cambia de palo a %s", this.chooseSuit(droppedCard.getSuit()));
        } else if (droppedCard.getNumber().equals("as")) {
            Player chosenPlayer = this.choosePlayer();
            chosenPlayer.drawCard();
            gameMessage = String.format(". Elige al jugador %s para que robe carta", chosenPlayer.getPlayerName());
        }
        return gameMessage;
    }

    private HashMap<String, Object> playWith2OnTable(Card cardOnTable, Player player) {
        HashMap<String, Object> returns = new HashMap<>();
        String playMessage = "";
        int numberOfCards = player.getNumberOfCardsInHand();
        Card droppedCard = (this.playedCard == null || this.playedCard.isDrawCard())
                ? special2Play(player, cardOnTable)
                : player.dropCard();
        int drawnCards = player.getNumberOfCardsInHand() - numberOfCards;
        if ((this.draw2 == -1 && drawnCards > 1) || drawnCards > 1) {
            playMessage = String.format(" chupa %d cartas", drawnCards);
        } else {
            if (droppedCard == null) {
                player.drawCard();
                playMessage = " roba carta";
            } else {
                playMessage = String.format(" echa %s", droppedCard.getCardName());
            }
        }
        returns.put("playMessage", playMessage);
        returns.put("droppedCard", droppedCard);
        return returns;
    }

    private HashMap<String, Object> playWithAnyCardOnTable(Card cardOnTable, Player player) {
        HashMap<String, Object> returns = new HashMap<>();
        String playMessage = "";
        Card droppedCard = null;
        Boolean playAgain = true;
        if (this.playedCard != null) {
            if (!this.playedCard.isDrawCard())
                droppedCard = player.dropCard(this.playedCard);
        } else {
            droppedCard = player.dropValidCard(cardOnTable, this.playedCard, this.suit);
        }
        if (this.suit != null && droppedCard != null) {
            System.out.println("-Reset cambio de palo");
            this.suit = null;
        }
        if (droppedCard == null) {
            if (cardOnTable.getNumber().equals("rey")) {
                System.out.println("--YA NO REPITE TURNO");
                playAgain = false;
            }
            player.drawCard();
            playMessage = " roba carta";
        } else {
            playMessage = String.format(" echa %s", droppedCard.getCardName());
        }
        returns.put("playMessage", playMessage);
        returns.put("droppedCard", droppedCard);
        returns.put("playAgain", playAgain);
        return returns;
    }

    private String checkKingCardDropped(Card droppedCard, Boolean playAgain, Player player) {
        String turnMessage = null;
        if (droppedCard != null && playAgain && droppedCard.getNumber().equals("rey")) {
            player = reverseDirection().setTurn().reverseDirection().getPlayerOfTurn();
            System.out.println("REY - VUELVE A JUGAR: " + this.getPlayerOfTurn().getPlayerName());
            turnMessage = player.isMachine() ? String.format(". Vuelve a jugar el %s.", player.getPlayerName())
                    : ". Vuelves a jugar.";
        }
        return turnMessage;
    }

    public String runPlay(String _playedCard) {
        Boolean playAgain = true;
        Card droppedCard = null;
        String gameMessage = "";
        String playMessage = "";
        String turnMessage = "";

        Player player = this.getPlayerOfTurn();
        this.playedCard = checkPlayedCard(player, _playedCard);
        Card cardOnTable = this.getCardOnTable();
        if (cardOnTable == null) {
            /* CAMBIAR: HAY QUE DESHABILITAR EL ENLACE DE ROBAR PARA QUE ESTO NO PETE */
            droppedCard = (this.playedCard != null) ? player.dropCard(this.playedCard) : player.dropCard();
            playMessage = String.format(" echa %s", droppedCard.getCardName());
        } else {
            if (cardOnTable.getNumber().equals("dos")) {
                HashMap<String, Object> returns = playWith2OnTable(cardOnTable, player);
                playMessage = (String) returns.get("playMessage");
                droppedCard = (Card) returns.get("droppedCard");
            } else {
                HashMap<String, Object> returns = playWithAnyCardOnTable(cardOnTable, player);
                playMessage = (String) returns.get("playMessage");
                droppedCard = (Card) returns.get("droppedCard");
                playAgain = (Boolean) returns.get("playAgain");
            }
        }
        if (player.getNumberOfCardsInHand() == 0) {
            System.out.println("GANA " + player.getPlayerName().toUpperCase());
            setPlayersScore();
            /* CAMBIAR: DAR LA VUELTA A TODAS LAS CARTAS */
            return String.format("FIN DE LA PARTIDA, ¡GANA EL JUGADOR %s!", player.getPlayerName().toUpperCase());
        }
        gameMessage = player.getPlayerName(true) + playMessage;
        if (droppedCard != null)
            gameMessage += checkSpecialDroppedCard(droppedCard);
        player = this.setTurn().getPlayerOfTurn();
        turnMessage = player.isMachine() ? String.format(". Turno del %s.", player.getPlayerName())
                : ". Es tu turno, elige tu jugada.";
        String kingMessage = checkKingCardDropped(droppedCard, playAgain, player);
        return gameMessage + (kingMessage != null ? kingMessage : turnMessage);
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
        // this.turn = 0; /* QUITAR */
        System.out.println("MANO --- jugador " + (this.turn + 1));
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
