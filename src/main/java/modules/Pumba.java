//TODO compatibilizar PUMBA con cartas de select
package modules;

import java.util.ArrayList;
import java.util.HashMap;

public class Pumba {
    private CardsDeck drawPile;
    private ArrayList<Card> discardPile;
    private ArrayList<Player> players;
    private int numberOfPlayers;
    private int turn;
    private String suit;
    private boolean isScoreRound;
    private boolean isSelectionRound;
    private Card playedCard;
    private int playDirection;
    private int draw2;
    private boolean activePumba;

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
        this.isSelectionRound = false;
        this.activePumba = false;
        PumbaUtilities.scoreCards(this.drawPile);
        PumbaUtilities.generatePlayers(this);
        PumbaUtilities.dealCards(this.drawPile, this.players);
    }

    /* GETTERS Y SETTERS */

    public CardsDeck getDrawPile() {
        System.out.println(" ------------------------- ");
        System.out.printf("| Cartas en mazo:      %2d |\n", this.drawPile.getCards().size());
        return this.drawPile;
    }

    public void setDrawPile(CardsDeck _drawPile) {
        this.drawPile = _drawPile;
    }

    public ArrayList<Card> getDiscardPile() {
        System.out.printf("| Cartas en descartes: %2d |\n", this.discardPile.size());
        System.out.println(" ------------------------- ");
        return this.discardPile;
    }

    public void setDiscardPile(ArrayList<Card> _discardPile) {
        this.discardPile = _discardPile;
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

    public void setTurn(int _turn) {
        this.turn = _turn;
    }

    public Pumba setTurnModule() {
        this.turn += this.playDirection;
        this.turn %= this.numberOfPlayers;
        if (this.turn == -1)
            this.turn = this.numberOfPlayers - 1;
        System.out.println("-------SET: " + this.getPlayerOfTurn().getPlayerName().toUpperCase());
        return this;
    }

    public Player getPreviousPlayer() {
        int prevTurn = this.turn - this.playDirection;
        prevTurn %= this.numberOfPlayers;
        if (prevTurn == -1)
            prevTurn = this.numberOfPlayers - 1;
        return this.players.get(prevTurn);
    }

    public int getPlayDirection() {
        return this.playDirection;
    }

    public void setPlayDirection(int _playDirection) {
        this.playDirection = _playDirection;
    }

    public Player getPlayerOfTurn() {
        return this.players.get(turn);
    }

    public boolean isScoreRound() {
        return this.isScoreRound;
    }

    public boolean isSelectionRound() {
        return this.isSelectionRound;
    }

    public boolean getActivePumba() {
        return this.activePumba;
    }

    public void setActivePumba(boolean active) {
        this.activePumba = active;
    }

    public int getDraw2() {
        return this.draw2;
    }

    public String getSuit() {
        return this.suit;
    }

    public void setSuit(String _suit) {
        this.suit = _suit;
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
        return this.suit.toLowerCase();
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

    /**
     * Devuelve la carta del centro de la mesa.
     * 
     * @return la carta en juego en el centro de la mesa.
     */
    public Card getCardOnTable() {
        return (this.discardPile.size() == 0) ? null : this.discardPile.get(this.discardPile.size() - 1);
    }

    /*** JUGADAS ESPECIALES Y CHEQUEOS DE LA JUGADA ***/

    private String mandatoryDraw2(Player _player, Card _cardOnTable) {
        System.out.println("---" + _player.getPlayerName() + " chupa: " + draw2 + " cartas---");
        String playMessage = String.format(" chupa %d cartas", draw2);
        _player.drawCards(draw2);
        PumbaUtilities.changeSuit(_cardOnTable.getSuitStr(), this);
        draw2 = -1;
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
     * @return un HashMap que contiene la carta echada y el mensaje de la jugada.
     */
    private HashMap<String, Object> special2Play(Player _player, Card _cardOnTable) {
        HashMap<String, Object> returns = new HashMap<>();
        String playMessage = "";
        Card droppedCard = null;
        if (this.draw2 == -1) {
            PumbaUtilities.changeSuit(_cardOnTable.getSuitStr(), this);
            droppedCard = _player.dropValidCard(_cardOnTable, this.playedCard, this.suit);
            if (droppedCard != null) {
                if (droppedCard.getNumber().equals("dos"))
                    PumbaUtilities.changeSuit(droppedCard.getSuitStr(), this);
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
     * @return la carta que se ha jugado. Si el jugador principal roba, retorna una
     *         carta especial en la que
     *         isDrawCard es true. null si se ha robado carta o no hay cartas
     *         válidas que jugar
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
     * @return retorna el mensaje para las cartas especiales. Si no es una carta
     *         especial, retorna un String vacío.
     */
    private String checkSpecialDroppedCard(Card droppedCard) {
        String gameMessage = "";
        if (droppedCard.getNumber().equals("siete")) {
            PumbaUtilities.reverseDirection(this);
            gameMessage = ". Cambio de sentido";
        } else if (droppedCard.getNumber().equals("caballo")) {
            this.setTurnModule();
            System.out.println("SALTA TURNO: no juega " + this.getPlayerOfTurn().getPlayerName());
            gameMessage = String.format(". Salta el turno de %s", this.getPlayerOfTurn().getPlayerName(true));
        } else if (droppedCard.getNumber().equals("sota")) {
            if (this.turn == 0)
                return null;
            gameMessage = String.format(" y cambia de palo a %s", PumbaUtilities.chooseSuit(this));
        } else if (droppedCard.getNumber().equals("as")) {
            if (this.turn == 0)
                return null;
            Player chosenPlayer = PumbaUtilities.choosePlayer(this);
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
     * @return el mensaje especial de la carta del rey. null si no se ejecuta la
     *         jugada especial del rey.
     */
    private String checkKingCardDropped(Card droppedCard, Boolean playAgain) {
        String kingMessage = null;
        if (droppedCard != null && playAgain && droppedCard.getNumber().equals("rey")) {
            PumbaUtilities.reverseDirection(this);
            setTurnModule();
            PumbaUtilities.reverseDirection(this);
            Player player = getPlayerOfTurn();
            System.out.println("REY - VUELVE A JUGAR: " + this.getPlayerOfTurn().getPlayerName());
            kingMessage = player.isMachine() ? String.format(". Vuelve a jugar %s.", player.getPlayerName())
                    : ". Vuelves a jugar.";
        }
        return kingMessage;
    }

    /**
     * Ejecuta la jugada cuando se encuentra un dos en la mesa. Se puede dar que el
     * jugador juegue otro dos, que el jugador tenga que chupar cartas o bien que ya
     * se haya chupado las cartas un jugador previo, en cuyo caso se juega al
     * palo del dos.
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
        boolean playAgain = true;
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

    /**
     * Función principal de ejecución de la jugada.
     * 
     * @param _playedCard la carta escogida por el jugador principal.
     * @return un String con el mensaje que explica la jugada.
     */
    public String runPlay(String _playedCard, String _suit, String _drawPlayer) {
        Boolean playAgain = true;
        Card droppedCard = null;
        String pumbaMessage = "";
        String gameMessage = "";
        String playMessage = "";
        String turnMessage = "";
        String kingMessage = null;
        if (this.activePumba && this.turn == 0 && _playedCard == null) {
            this.activePumba = false;
            return "¡PUMBA!";
        }
        if (_suit != null) {
            PumbaUtilities.changeSuit(_suit, this);
            droppedCard = getPlayedCard(_playedCard);
            playMessage = String.format(" echa %s y cambia de palo a %s", droppedCard.getCardName(), getSuit());
            gameMessage = getPlayerOfTurn().getPlayerName(true) + playMessage;
            turnMessage = String.format(". Turno del %s.", this.setTurnModule().getPlayerOfTurn().getPlayerName(true));
            this.isSelectionRound = false;
        } else if (_drawPlayer != null) {
            droppedCard = getPlayedCard(_playedCard);
            _drawPlayer = _drawPlayer.replace("_", " ");
            playMessage = String.format(" echa %s. Elige a %s para que chupe 1 carta", droppedCard.getCardName(),
                    Utilities.firstToCapital(_drawPlayer));
            for (Player p : this.players) {
                if (p.getPlayerName().equals(_drawPlayer))
                    p.drawCard();
            }
            gameMessage = getPlayerOfTurn().getPlayerName(true) + playMessage;
            turnMessage = String.format(". Turno del %s.", this.setTurnModule().getPlayerOfTurn().getPlayerName(true));
            this.isSelectionRound = false;
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
                PumbaUtilities.setPlayersScore(this.players, this.isScoreRound);
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
            if (this.activePumba && this.turn != 0) {
                if (Math.random() < 0.1)
                    pumbaMessage = String.format("%s: ¡PUMBA!<br/>", player.getPlayerName(true));
                else {
                    pumbaMessage = String.format("%s no ha dicho pumba, chupa 1 carta.<br/>",
                            player.getPlayerName(true));
                    player.drawCard();
                }
                this.activePumba = false;
            } else if (this.activePumba && _playedCard != null) {
                pumbaMessage = "No has dicho pumba, chupas 1 carta.<br/>";
                player.drawCard();
                this.activePumba = false;
            }
            if (gameMessage != null) {
                player = this.setTurnModule().getPlayerOfTurn();
                if (player.isMachine())
                    turnMessage = String.format(". Turno del %s.", player.getPlayerName());
                else {
                    turnMessage = ". Es tu turno, elige tu jugada.";
                    CardHand cardHand = player.getCardHand();
                    ArrayList<Card> validCards = cardHand
                            .printPlayer1ValidCards(droppedCard != null ? droppedCard : cardOnTable, this.suit);
                    if (player.getNumberOfCardsInHand() == 2 && validCards.size() >= 1) {
                        this.activePumba = true;
                        System.out.println("-*- PUMBA ACTIVO -*-");
                    }
                }
                kingMessage = checkKingCardDropped(droppedCard, playAgain);
            } else {
                this.isSelectionRound = true;
                if (droppedCard.getNumber().equals("sota")) {
                    System.out.println("-*- ELECCION DE PALO -*-");
                    gameMessage = "Elige el cambio de palo.";
                } else {
                    System.out.println("-*- ELECCION DE JUGADOR -*-");
                    gameMessage = "Elige al jugador que chupa 1 carta.";
                }
            }
        }
        return pumbaMessage + gameMessage + (kingMessage != null ? kingMessage : turnMessage);
    }

}
