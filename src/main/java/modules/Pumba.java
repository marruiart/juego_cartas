package modules;

import java.util.ArrayList;
import java.util.HashMap;

public class Pumba {
    private CardsDeck drawPile;
    private ArrayList<Card> discardPile;
    private ArrayList<Player> players;
    private int numberOfPlayers;
    private int playDirection;
    private int turn;
    private int draw2;
    private int round;
    private String suit;
    public boolean isScoreRound;
    public boolean isSelectionRound;
    private Card playedCard;
    private Player prevPlayer;

    public Pumba(int _numberOfPlayers) {
        this(_numberOfPlayers, null, null, null, 1);
    }

    public Pumba(int _numberOfPlayers, ArrayList<Player> _players, CardsDeck drawPile, ArrayList<Card> discardPile,
            int _round) {
        this.numberOfPlayers = _numberOfPlayers;
        this.playDirection = 1;
        this.draw2 = 2;
        this.round = _round;
        this.suit = null;
        this.isScoreRound = false;
        this.isSelectionRound = false;
        this.playedCard = null;
        if (_round == 1) {
            this.drawPile = new CardsDeck();
            this.discardPile = new ArrayList<>();
            this.players = new ArrayList<>();
            PumbaUtilities.scoreCards(this.drawPile);
            PumbaUtilities.generatePlayers(this);
        } else {
            this.drawPile = drawPile;
            this.discardPile = discardPile;
            this.players = _players;
            for (Player p : players) {
                p.setGame(this);
            }
            PumbaUtilities.scoreCards(this.drawPile);
        }
        this.turn = PumbaUtilities.chooseManoPlayer(this);
        PumbaUtilities.dealCards(this.drawPile, this.players);
        this.prevPlayer = this.getPlayerOfTurn();
    }

    /* GETTERS Y SETTERS */

    public CardsDeck getDrawPile() {
        // System.out.println(" ------------------------- ");
        // System.out.printf("| Cartas en mazo: %2d |\n", this.drawPile.getCards().size());
        return this.drawPile;
    }

    public void setDrawPile(CardsDeck _drawPile) {
        this.drawPile = _drawPile;
    }

    public ArrayList<Card> getDiscardPile() {
        // System.out.printf("| Cartas en descartes: %2d |\n", this.discardPile.size());
        // System.out.println(" ------------------------- ");
        return this.discardPile;
    }

    public void setDiscardPile(ArrayList<Card> _discardPile) {
        this.discardPile = _discardPile;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public void setPlayers(ArrayList<Player> _players) {
        this.players = _players;
        this.numberOfPlayers = _players.size();
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

    public int setTurn(int _turn) {
        this.turn = _turn;
        return this.turn;
    }

    public Pumba setTurnModule() {
        this.turn += this.playDirection;
        this.turn %= this.numberOfPlayers;
        if (this.turn == -1)
            this.turn = this.numberOfPlayers - 1;
        System.out.println("-------SET: " + this.getPlayerOfTurn().getPlayerName().toUpperCase());
        return this;
    }

    public void setPreviousPlayer(Player _prevPlayer) {
        prevPlayer = _prevPlayer;
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

    public int getRound() {
        return this.round;
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

    /* ** JUGADAS ESPECIALES Y CHEQUEOS DE LA JUGADA ** */

    /**
     * Ejecuta el robo de cartas de los jugadores que "chupan" carta por el dos.
     * 
     * @param _player      el jugador en turno
     * @param _cardOnTable la carta en el centro de la mesa
     * @return el mensaje de esta jugada especial
     */
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
            if (droppedCard != null)
                this.draw2 = 2;
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
            setPreviousPlayer(player);
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
            setPreviousPlayer(this.getPlayerOfTurn());
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
            this.setTurn(prevPlayer.getNumber() - 1);
            System.out.println("REY - VUELVE A JUGAR: " + prevPlayer.getPlayerName());
            kingMessage = prevPlayer.isMachine() ? String.format(". Vuelve a jugar %s.", prevPlayer.getPlayerName())
                    : ". Vuelves a jugar.";
            if (prevPlayer.isMachine()) {
                kingMessage = String.format(". Vuelve a jugar %s.", prevPlayer.getPlayerName());
            } else {
                kingMessage = ". Vuelves a jugar.";
                PumbaUtilities.activatePumbaTime(prevPlayer, droppedCard);
            }
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
        else if (!this.playedCard.isDrawCard()) {
            droppedCard = player.dropCard(this.playedCard);
            if (this.draw2 == -1)
                this.draw2 = 2;
            else if (droppedCard.getNumber().equals("dos"))
                this.draw2 += 2;
        }
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
            if (!this.playedCard.isDrawCard()) {
                droppedCard = player.dropCard(this.playedCard);
                if (this.draw2 == -1 && droppedCard.getNumber().equals("dos"))
                    this.draw2 = 2;
            }
        } else {
            droppedCard = player.dropValidCard(cardOnTable, this.playedCard, this.suit);
        }
        if (this.suit != null && droppedCard != null) {
            System.out.println("-Reset cambio de palo");
            this.suit = null;
        }
        if (droppedCard == null) {
            if (cardOnTable.getNumber().equals("rey")) {
                player.setLastDroppedKing(false);
                System.out.println("--YA NO REPITE TURNO");
                playAgain = false;
            }
            player.drawCard();
            playMessage = " roba carta";
        } else {
            if (droppedCard.getNumber().equals("rey"))
                player.setLastDroppedKing(true);
            playMessage = String.format(" echa %s", droppedCard.getCardName());
        }
        returns.put("playMessage", playMessage);
        returns.put("droppedCard", droppedCard);
        returns.put("playAgain", playAgain);
        return returns;
    }

    /**
     * Hace que se finalice una jugada de pumba activo (al jugador le quedará una
     * carta en mano). Si es la máquina, se selecciona aleatoriamente si el jugador
     * dice o no "¡PUMBA!". Si es el jugador principal y no ha pulsado el botón se
     * ejecutará el else if.
     * 
     * @param prevPlayer  el jugador previo (el que inició jugando en la ronda)
     * @param _playedCard la carta que jugó el jugador principal o null.
     * @return el mensaje informando si se ha dicho o no "¡PUMBA!"
     */
    private String getPumbaMessage(Player prevPlayer, String _playedCard) {
        String pumbaMessage = "";
        if (prevPlayer.isPumbaTime() && prevPlayer.getNumber() != 1) {
            if (Math.random() < 0.9)
                pumbaMessage = String.format("%s: ¡PUMBA!<br/>", prevPlayer.getPlayerName(true));
            else {
                pumbaMessage = String.format("%s no ha dicho pumba, chupa 1 carta.<br/>",
                        prevPlayer.getPlayerName(true));
                prevPlayer.drawCard();
            }
            prevPlayer.setPumbaTime(false);
        }
        return pumbaMessage;
    }

    /* *** COMPROBACIONES CON EL JUGADOR PRINCIPAL *** */

    /**
     * Hace que se imprima posteriormente una etiqueta select para
     * elegir el palo al que se cambia o el jugador que roba al tornar true el
     * atributo isSelectionRound.
     * 
     * @param droppedCard la carta jugada en la ronda
     * @return el mensaje para indicar la elección al jugador principal
     */
    private String selectionRound(Card droppedCard) {
        String gameMessage;
        this.isSelectionRound = true;
        if (droppedCard.getNumber().equals("sota")) {
            System.out.println("-*- ELECCION DE PALO -*-");
            gameMessage = "Elige el cambio de palo.";
        } else {
            System.out.println("-*- ELECCION DE JUGADOR -*-");
            gameMessage = "Elige al jugador que chupa 1 carta.";
        }
        return gameMessage;
    }

    /**
     * Comprueba si el jugador ha presionado el botón ¡PUMBA! cuando debía.
     * 
     * @param _playedCard  un String que debe tener formato numero_palo
     * @param personPlayer el jugador principal
     * @return el mensaje con la información de si ha presionado o no el botón
     */
    private String checkPumbaButton(String _playedCard, Player personPlayer) {
        String pumbaMessage = "";
        if (_playedCard == null) {
            // Si la persona ha pulsado el botón ¡PUMBA! cuando debía
            personPlayer.setPumbaTime(false);
            System.out.println("\nPUMBA!");
            pumbaMessage = "¡PUMBA!";
        } else if (!isSelectionRound) {
            // Si la persona no ha pulsado el botón cuando debía
            System.out.println("\nNO HAS DICHO PUMBA!");
            pumbaMessage = "No has dicho pumba, chupas 1 carta.<br/>";
            personPlayer.drawCard();
            personPlayer.setPumbaTime(false);
        }
        return pumbaMessage;
    }

    public String checkEndOfGame(Player player) {
        PumbaUtilities.setPlayersScore(this);
        System.out.println("GANA " + player.getPlayerName().toUpperCase());
        if (this.numberOfPlayers == 2 && players.get(1).getScore() >= 100) {
            // Gana la partida la persona
            return "FIN DE LA PARTIDA, ¡ENHORABUENA, HAS GANADO!";
        } else {
            // Jugador se queda sin cartas y gana la ronda o la partida
            if (this.players.get(0).getScore() < 100)
                return String.format("¡RONDA TERMINADA! ¿Quieres jugar otra ronda?");
            else
                return String.format("¡RONDA TERMINADA! Lo siento, has sido eliminado del juego");
        }
    }

    /**
     * Función principal de ejecución de la jugada.
     * 
     * @param _playedCard la carta escogida por la persona o null
     * @param _suit       el palo escogido por la persona o null
     * @param _drawPlayer el jugador que roba escogido por la persona o null
     * @return un String con el mensaje que explica la jugada.
     */
    public String runPlay(String _playedCard, String _suit, String _drawPlayer) {
        Boolean playAgain = true;
        Card droppedCard = null;
        String pumbaMessage = null;
        String gameMessage = "";
        String playMessage = "";
        String turnMessage = "";
        String kingMessage = null;
        Player player = getPlayerOfTurn();

        System.out.println("\n<- Previo: " + prevPlayer.getPlayerName());
        System.out.println("* Jugada dos: " + this.draw2);
        System.out.println("* Es ronda de select: " + this.isSelectionRound);
        System.out.println("* Palo en juego: " + this.suit);
        System.out.printf("* Rey activo %s: %b\n", player.getPlayerName(), player.isLastDroppedKing());
        System.out.printf("* Rey activo %s: %b\n", prevPlayer.getPlayerName(), prevPlayer.isLastDroppedKing());

        if (player.getNumber() == 1 && player.isPumbaTime()) {
            pumbaMessage = checkPumbaButton(_playedCard, player);
            if (pumbaMessage.equals("¡PUMBA!"))
                return pumbaMessage;
        }
        if (_suit != null) {
            // Si el jugador principal ha cambiado de palo en el select
            PumbaUtilities.changeSuit(_suit, this);
            droppedCard = Card.formatPlayedCard(_playedCard);
            playMessage = String.format(" echa %s y cambia de palo a %s", droppedCard.getCardName(), getSuit());
            gameMessage = getPlayerOfTurn().getPlayerName(true) + playMessage;
            setPreviousPlayer(this.getPlayerOfTurn());
            turnMessage = String.format(". Turno del %s.", this.setTurnModule().getPlayerOfTurn().getPlayerName(true));
            this.isSelectionRound = false;
        } else if (_drawPlayer != null) {
            // Si el jugador principal ha elegido un jugador para que robe en el select
            droppedCard = Card.formatPlayedCard(_playedCard);
            _drawPlayer = _drawPlayer.replace("_", " ");
            playMessage = String.format(" echa %s. Elige a %s para que chupe 1 carta", droppedCard.getCardName(),
                    Utilities.firstToCapital(_drawPlayer));
            for (Player p : this.players) {
                if (p.getPlayerName().equals(_drawPlayer))
                    p.drawCard();
            }
            gameMessage = getPlayerOfTurn().getPlayerName(true) + playMessage;
            setPreviousPlayer(this.getPlayerOfTurn());
            turnMessage = String.format(". Turno del %s.", this.setTurnModule().getPlayerOfTurn().getPlayerName(true));
            this.isSelectionRound = false;
        } else {
            player = this.getPlayerOfTurn();
            System.out.println("\nTURNO: " + player.getPlayerName().toUpperCase());
            this.playedCard = checkPlayedCard(player, _playedCard);
            Card cardOnTable = this.getCardOnTable();
            if (cardOnTable == null) {
                // Jugada inicial, no hay carta en mesa. Abre la mano con cualquier carta
                System.out.println("--Mesa vacia");
                droppedCard = (this.playedCard != null) ? player.dropCard(this.playedCard) : player.dropCard();
                playMessage = String.format(" echa %s", droppedCard.getCardName());
            } else {
                System.out.println("--Carta en mesa: " + cardOnTable.getCardName());
                if (cardOnTable.getNumber().equals("dos")) {
                    // Jugada especial cuando hay un 2 en mesa
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
            // Comprobar fin de la partida
            if (player.getNumberOfCardsInHand() == 0)
                return checkEndOfGame(player);
            gameMessage = player.getPlayerName(true) + playMessage;
            if (droppedCard != null) {
                // Cuando el jugador juega una carta en su turno (carta especial o no)
                String specialMessage = checkSpecialDroppedCard(droppedCard);
                if (specialMessage != null)
                    gameMessage += specialMessage;
                else
                    gameMessage = null;
            }
            if (gameMessage != null) {
                // El jugador echó una carta especial (no si persona echa as o sota)
                if (droppedCard != null && !droppedCard.getNumber().equals("caballo"))
                    setPreviousPlayer(this.getPlayerOfTurn());
                player = this.setTurnModule().getPlayerOfTurn();
                if (player.isMachine())
                    turnMessage = String.format(". Turno del %s.", player.getPlayerName());
                else {
                    turnMessage = ". Es tu turno, elige tu jugada.";
                    player.printPlayer1ValidCards(droppedCard != null ? droppedCard : cardOnTable,
                            this.suit);
                }
                // Comprobar si la carta echada fue un rey para repetir turno o no
                kingMessage = checkKingCardDropped(droppedCard, playAgain);
            } else {
                // Si gameMessage == null, el jugador principal echó sota o as
                gameMessage = selectionRound(droppedCard);
            }
        }
        // Se obtiene el mensaje según si hay una jugada activa de ¡PUMBA!
        pumbaMessage = pumbaMessage == null ? getPumbaMessage(prevPlayer, _playedCard) : pumbaMessage;
        // Reseteo de última carta echada como rey en el jugador previo
        prevPlayer.setLastDroppedKing(false);
        return pumbaMessage + gameMessage + (kingMessage != null ? kingMessage : turnMessage);
    }
}
