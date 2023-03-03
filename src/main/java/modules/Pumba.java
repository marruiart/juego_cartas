//TODO Quitar limitación de sota a palo diferente al actual
//TODO Sota elige palo más favorecedor (no aleatorio)
//TODO pasar un HashMap en inlineStyle de printImg
//TODO jugar rondas y eliminar jugadores
//TODO Si caballo + palo caballo -> elegir esa
//TODO Si rey + palo rey -> elegir esa
//TODO cantar "¡PUMBA!"
//TODO not available cards when choosing suit
//TODO not available cards when choosing person to draw
//TODO assign players names
package modules;

import java.util.ArrayList;
import java.util.HashMap;
import modules.Enums.Suits;

public class Pumba {
    private CardsDeck drawPile;
    private ArrayList<Card> discardPile;
    private ArrayList<Player> players;
    private int numberOfPlayers;
    private int turn;
    private String suit;
    private boolean isScoreRound;
    private Card playedCard;
    private int playDirection;
    private int draw2;

    public Pumba(int _numberOfPlayers) {
        this.drawPile = new CardsDeck();
        this.discardPile = new ArrayList<>();
        this.players = new ArrayList<>();
        this.numberOfPlayers = _numberOfPlayers;
        this.draw2 = 2;
        this.playDirection = 1;
        this.suit = null;
        this.playedCard = null;
        this.isScoreRound = false;
        scoreCards();
        generatePlayers();
        dealCards();
    }

    /* GETTERS Y SETTERS */

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

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public ArrayList<String> getAllPlayersNames() {
        ArrayList<String> playersNames = new ArrayList<>();
        Player playerOfTurn = getPlayerOfTurn();
        for (Player p : this.players) {
            if (p != playerOfTurn)
                playersNames.add(p.getPlayerName(true));
        }
        return playersNames;
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

    public int getPlayDirection() {
        return this.playDirection;
    }

    public Player getPlayerOfTurn() {
        Player player = this.players.get(turn);
        return player;
    }

    public String getSuit() {
        return this.suit;
    }

    /**
     * Devuelve el String correspondiente al palo en juego en la partida.
     * 
     * @return un String con el palo en juego.
     */
    public String getSuitOnPlay() {
        if (this.suit == null) {
            Card cardOnTable = getCardOnTable();
            if (cardOnTable != null)
                return cardOnTable.getSuitStr();
            return "";
        }
        return this.suit.toString().toLowerCase();
    }

    /**
     * Devuelve el String con la imagen correspondiente al palo en juego en la
     * partida.
     * 
     * @return un String con la imagen del palo en juego.
     */
    public String getSuitImg() {
        if (this.suit == null) {
            Card cardOnTable = getCardOnTable();
            if (cardOnTable != null) {
                String src = String.format("assets/img/%s.png", cardOnTable.getSuitStr());
                return Utilities.printImg(src, cardOnTable.getSuitStr());
            }
            return Utilities.printImg("empty", null);
        }
        String suitStr = getSuitOnPlay();
        String src = suitStr.equals("") ? "" : String.format("assets/img/%s.png", suitStr);
        return Utilities.printImg(src, suitStr);
    }

    public boolean isScoreRound() {
        return this.isScoreRound;
    }

    /**
     * Devuelve la carta del centro de la mesa.
     * 
     * @return la carta en juego en el centro de la mesa.
     */
    public Card getCardOnTable() {
        return (this.discardPile.size() == 0) ? null : this.discardPile.get(this.discardPile.size() - 1);
    }

    /**** MOVIMIENTOS DE LOS MAZOS ****/

    /**
     * Suelta una carta concreta en la pila de descartes, dando una rotación para
     * simular desorden del mazo y colocando la carta boca arriba.
     * 
     * @param _card la carta soltada.
     */
    public void dropOnDiscardPile(Card _card) {
        float rotation = -0.004f + (float) (Math.random() * (0.004f + 0.004f));
        _card.setUncovered(true).rotateCard("center", Float.toString(rotation));
        discardPile.add(_card);
    }

    /**
     * Retira varias cartas del mazo de robo.
     * 
     * @param n el número de cartas robadas.
     * @return un ArrayList con las cartas robadas
     */
    public ArrayList<Card> drawCardsFromDeck(int n) {
        ArrayList<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Card c = drawPile.drawLastCard();
            if (c == null) {
                this.flipDiscardsPile();
                c = drawPile.drawLastCard();
            }
            if (c != null) {
                drawnCards.add(c);
            }
        }
        return drawnCards;
    }

    /**
     * Voltea la pila de descartes cuando se queda sin cartas.
     */
    private void flipDiscardsPile() {
        System.out.println("VOLTEAR DESCARTES");
        Card lastCard = this.discardPile.remove(this.discardPile.size() - 1);
        drawPile.returnCards(this.discardPile);
        drawPile.shuffle();
        this.discardPile = new ArrayList<>();
        this.discardPile.add(lastCard);
    }

    /*** CAMBIOS DURANTE LA PARTIDA ***/

    /**
     * Cambia el sentido de la ronda de juego.
     * 
     * @return devuelve un objeto Pumba de la partida en juego.
     */
    private Pumba reverseDirection() {
        System.out.println("CAMBIO SENTIDO");
        this.playDirection *= -1;
        return this;
    }

    /**
     * Elige un palo aleatoriamente, sin que sea el mismo que está en juego y cambia
     * el palo en juego.
     * 
     * @param _suitOnTable el palo en juego
     * @return un String con el palo escogido.
     */
    private String chooseSuit(String _suitOnTable) {
        String suit;
        do {
            suit = Suits.getRandom().toString().toLowerCase();
        } while (suit.equals(_suitOnTable));
        return changeSuit(suit);
    }

    /**
     * Cambia el palo en juego a aquel pasado por parámetros.
     * 
     * @param _suit el palo al que cambia el juego.
     * @return el palo al que cambia el juego.
     */
    private String changeSuit(String _suit) {
        this.suit = _suit;
        System.out.println("---CAMBIO DE PALO A " + _suit.toUpperCase());
        return _suit;
    }

    /**
     * Elige un jugador aleatorio, sin que sea el que está en turno.
     * 
     * @return el jugador escogido.
     */
    private Player choosePlayer() {
        int n;
        do {
            n = (int) (Math.random() * this.getNumberOfPlayers());
        } while (this.getTurn() == n);
        Player chosenPlayer = players.get(n);
        System.out.println("--" + chosenPlayer.getPlayerName(true) + " elegido para robar");
        return chosenPlayer;
    }

    private String mandatoryDraw2(Player _player, Card _cardOnTable) {
        System.out.println("---" + _player.getPlayerName() + " chupa: " + this.draw2 + " cartas---");
        String playMessage = String.format(" chupa %d cartas", this.draw2);
        _player.drawCards(this.draw2);
        changeSuit(_cardOnTable.getSuitStr());
        this.draw2 = -1;
        return playMessage;
    }

    /**
     * Esta función comprueba las cartas que puede echar un jugador que no sea el
     * principal cuando la carta en el centro de la mesa es un dos.
     * Si draw2 es -1, quiere decir que ese dos ya ha cumplido su función especial
     * antes (ha chupado cartas un jugador previo). Se establece el palo del dos
     * sobre la mesa y se comprueba si el jugador puede echar carta.
     * Si draw2 es distinto de 8, se comprueba si el jugador puede echar carta,
     * en cuyo caso suma 2 cartas a draw2. Si no puede echar carta, el jugador roba
     * tantas cartas como indique draw2.
     * 
     * @param _player      el jugador en turno
     * @param _cardOnTable la carta en el centro de la mesa
     * @return
     */
    private HashMap<String, Object> special2Play(Player _player, Card _cardOnTable) {
        HashMap<String, Object> returns = new HashMap<>();
        String playMessage = "";
        Card droppedCard = null;
        if (this.draw2 == -1) {
            changeSuit(_cardOnTable.getSuitStr());
            droppedCard = _player.dropValidCard(_cardOnTable, this.playedCard, this.suit);
            if (droppedCard != null) {
                if (droppedCard.getNumber().equals("dos"))
                    changeSuit(droppedCard.getSuitStr());
                this.draw2 = 2;
            }
            returns.put("droppedCard", droppedCard);
            returns.put("playMessage", playMessage);
            return returns;
        }
        if (this.draw2 != 8)
            droppedCard = _player.dropValidCard(_cardOnTable, this.playedCard);
        if (droppedCard == null) {
            playMessage = mandatoryDraw2(_player, _cardOnTable);
        } else {
            this.draw2 += 2;
        }
        returns.put("droppedCard", droppedCard);
        returns.put("playMessage", playMessage);
        return returns;
    }

    /*** JUGADAS ESPECIALES Y CHEQUEOS DE LA JUGADA ***/

    private Card getPlayedCard(String _playedCard) {
        String[] card = _playedCard.split("_");
        String number = card[0];
        String suit = card[1];
        return new Card(number, suit);
    }

    /**
     * Comprueba la carta ha jugado el jugador principal en su turno o bien si ha
     * robado carta del mazo.
     * 
     * @param player      el jugador en turno
     * @param _playedCard la carta que se ha jugado o null en caso de que no haya
     *                    ninguna
     * @return la carta que se ha jugado
     * @return si el jugador principal roba, retorna una carta especial en la que
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

    /**
     * Comprueba si la carta que se ha jugado es especial. Si lo es, ejecuta la
     * jugada especial.
     * Siete -> Cambio de sentido
     * Caballo -> Salta un turno
     * Sota -> Cambio de palo
     * As -> Elige un jugado para robar 1 carta
     * 
     * @param droppedCard la carta jugada en ese turno
     * @return retorna el mensaje para las cartas especiales
     * @return si no es una carta especial, retorna un String vacío.
     */
    private String checkSpecialDroppedCard(Card droppedCard) {
        String gameMessage = "";
        if (droppedCard.getNumber().equals("siete")) {
            this.reverseDirection();
            gameMessage = ". Cambio de sentido";
        } else if (droppedCard.getNumber().equals("caballo")) {
            this.setTurn();
            System.out.println("SALTA TURNO: no juega " + this.getPlayerOfTurn().getPlayerName());
            gameMessage = String.format(". Salta el turno de %s", this.getPlayerOfTurn().getPlayerName(true));
        } else if (droppedCard.getNumber().equals("sota")) {
            if (this.turn == 0)
                return null;
            gameMessage = String.format(" y cambia de palo a %s", this.chooseSuit(droppedCard.getSuitStr()));
        } else if (droppedCard.getNumber().equals("as")) {
            if (this.turn == 0)
                return null;
            Player chosenPlayer = this.choosePlayer();
            chosenPlayer.drawCard();
            gameMessage = String.format(". Elige a %s para que chupe 1 carta", chosenPlayer.getPlayerName(true));
        }
        return gameMessage;
    }

    /**
     * Comprueba si la carta jugada es rey. En caso de que lo sea, ejecuta la jugada
     * especial del rey para que el jugador repita turno y retorna el mensaje.
     * 
     * @param droppedCard carta jugada en ese turno
     * @param playAgain   true si la carta de rey está en juego, false si
     *                    se mantiene en mesa desde jugadas anteriores.
     * @param player      jugador del turno.
     * @return el mensaje especial de la carta del rey.
     * @return null si no se ejecuta la jugada especial del rey.
     */
    private String checkKingCardDropped(Card droppedCard, Boolean playAgain, Player player) {
        String kingMessage = null;
        if (droppedCard != null && playAgain && droppedCard.getNumber().equals("rey")) {
            player = reverseDirection().setTurn().reverseDirection().getPlayerOfTurn();
            System.out.println("REY - VUELVE A JUGAR: " + this.getPlayerOfTurn().getPlayerName());
            kingMessage = player.isMachine() ? String.format(". Vuelve a jugar el %s.", player.getPlayerName())
                    : ". Vuelves a jugar.";
        }
        return kingMessage;
    }

    /**
     * Ejecuta la jugada cuando se encuentra un dos en la mesa. Se puede dar que el
     * jugador juegue otro dos, que el jugador tenga que chupar cartas o bien que ya
     * se haya chupado las cartas un jugador previo, en cuyo caso se juega al
     * palo del dos.
     * 
     * Si es turno de la máquina (this.playedCard == null), se ejecuta special2Play
     * para ver qué carta se puede jugar. Si el jugador principal ha robado y hay un
     * dos activo en juego, chupa cartas. Si el jugador principal ha echado carta,
     * se suelta esa carta. Si no se puede jugar ninguna carta o el jugador
     * principal ha robado, se roba carta.
     * 
     * @param cardOnTable carta sobre la mesa
     * @param player      jugador del turno
     * @return devuelve un HashMap con el mensaje de la jugada y la carta echada
     */
    private HashMap<String, Object> playWith2OnTable(Card cardOnTable, Player player) {
        HashMap<String, Object> returns = new HashMap<>();
        String playMessage = "";
        Card droppedCard = null;
        if (this.playedCard == null) {
            HashMap<String, Object> specialReturns = special2Play(player, cardOnTable);
            droppedCard = specialReturns != null ? (Card) specialReturns.get("droppedCard") : null;
            playMessage = specialReturns != null ? (String) specialReturns.get("playMessage") : "";
        } else if (this.playedCard.isDrawCard() && this.draw2 != -1)
            playMessage = mandatoryDraw2(player, cardOnTable);
        else if (!this.playedCard.isDrawCard())
            droppedCard = player.dropCard(this.playedCard);
        if (droppedCard == null && playMessage.equals("")) {
            player.drawCard();
            playMessage = " roba carta";
        } else if (playMessage.equals("")) {
            playMessage = String.format(" echa %s", droppedCard.getCardName());
        }
        returns.put("playMessage", playMessage);
        returns.put("droppedCard", droppedCard);
        return returns;
    }

    /**
     * Ejecuta la jugada cuando se encuentra cualquier carta que no sea un dos en la
     * mesa. Si el jugador principal ha echado carta, se suelta esta. Si no, se
     * busca una válida que soltar.
     * Se resetea el cambio de palo que se haya arrastrado si jugadores anteriores
     * pasaron turno sin echar carta.
     * Si no hay carta válida, se roba carta.
     * Si sobre la mesa había un rey, se establece que ya no se repite más el turno.
     * 
     * @param cardOnTable carta sobre la mesa
     * @param player      jugador del turno
     * @return devuelve un HashMap con el mensaje de la jugada, la carta echada y un
     *         boolean que indica si la carta de rey está en juego.
     */
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

    /*** PUNTUACIONES ***/

    /**
     * Asigna la puntuación de la ronda a cada jugador en función de sus cartas en
     * mano.
     */
    private void setPlayersScore() {
        this.isScoreRound = true;
        for (Player p : this.players) {
            int score = 0;
            for (Card c : p.getCardHand().getCards())
                score += c.getScore();
            p.updateScore(score);
        }
    }

    /**
     * Función principal de ejecución de la jugada.
     * 
     * @param _playedCard la carta escogida por el jugador principal.
     * @return un String con el mensaje que explica la jugada.
     */
    public String runPlay(String _playedCard, String _suit, String _drawPlayer) {
        Boolean playAgain = true;
        Card droppedCard = null;
        String gameMessage = "";
        String playMessage = "";
        String turnMessage = "";
        String kingMessage = null;
        if (_suit != null) {
            changeSuit(_suit);
            droppedCard = getPlayedCard(_playedCard);
            playMessage = String.format(" echa %s y cambia de palo a %s", droppedCard.getCardName(), getSuit());
            Player player = getPlayerOfTurn();
            gameMessage = player.getPlayerName(true) + playMessage;
            player = this.setTurn().getPlayerOfTurn();
            turnMessage = String.format(". Turno del %s.", player.getPlayerName());
        } else if (_drawPlayer != null) {
            droppedCard = getPlayedCard(_playedCard);
            playMessage = String.format(" echa %s. Elige a %s para que chupe 1 carta", droppedCard.getCardName(),
                    Utilities.firstToCapital(_drawPlayer).replace("_", " "));
            Player player = getPlayerOfTurn();
            gameMessage = player.getPlayerName(true) + playMessage;
            turnMessage = String.format(". Turno del %s.", player.getPlayerName());
        } else {
            Player player = this.getPlayerOfTurn();
            System.out.println("\nTURNO: " + player.getPlayerName().toUpperCase());
            this.playedCard = checkPlayedCard(player, _playedCard);
            Card cardOnTable = this.getCardOnTable();
            System.out
                    .println(cardOnTable == null ? "--Mesa vacia" : ("--Carta en mesa: " + cardOnTable.getCardName()));
            if (cardOnTable == null) {
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
                // TODO DAR LA VUELTA A TODAS LAS CARTAS
                return String.format("FIN DE LA PARTIDA, ¡GANA %s!", player.getPlayerName().toUpperCase());
            }
            gameMessage = player.getPlayerName(true) + playMessage;
            if (droppedCard != null) {
                String specialMessage = checkSpecialDroppedCard(droppedCard);
                if (specialMessage != null)
                    gameMessage += specialMessage;
                else
                    gameMessage = null;
            }
            if (gameMessage != null) {
                player = this.setTurn().getPlayerOfTurn();
                if (player.isMachine())
                    turnMessage = String.format(". Turno del %s.", player.getPlayerName());
                else {
                    turnMessage = ". Es tu turno, elige tu jugada.";
                    CardHand cardHand = player.getCardHand();
                    cardHand.printPlayer1ValidCards(droppedCard != null ? droppedCard : cardOnTable, this.suit);
                }
                kingMessage = checkKingCardDropped(droppedCard, playAgain, player);
            } else {
                if (droppedCard.getNumber().equals("sota")) {
                    System.out.println("-*- ELECCION DE PALO -*-");
                    gameMessage = "Elige el cambio de palo.";
                } else {
                    System.out.println("-*- ELECCION DE JUGADOR -*-");
                    gameMessage = "Elige al jugador que chupa 1 carta.";
                }
            }
        }
        return gameMessage + (kingMessage != null ? kingMessage : turnMessage);
    }

    /*** FUNCIONES PARA INCIAR LA PARTIDA ***/

    /**
     * Establece la puntuación que tiene cada carta en función de su número y la
     * asigna al atributo score de la carta.
     */
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

    /**
     * Genera el número de jugadores que sean necesarios para la partida, eligiendo
     * aleatoriamente uno como Mano.
     */
    private void generatePlayers() {
        this.turn = (int) (Math.random() * this.numberOfPlayers);
        // this.turn = 0; /* CAMBIAR: DESCOMENTAR PARA QUE EL JUGADOR PRINCIPAL SEA MANO
        // */
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
