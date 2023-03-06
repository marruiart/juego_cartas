package modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PumbaUtilities {
    private static int winner = 101;

    /* ** FUNCIONES PARA INCIAR LA PARTIDA ** */

    /**
     * Establece la puntuación que tiene cada carta en función de su número y la
     * asigna al atributo score de la carta.
     * 
     * @param drawPile la pila de robo de la partida
     */
    public static void scoreCards(CardsDeck drawPile) {
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
                    break;
                case "caballo":
                    score = 9;
                    break;
                default:
                    score = 10;
            }
            c.setScore(score);
        }
    }

    /**
     * Genera el número de jugadores que sean necesarios para la partida, eligiendo
     * aleatoriamente uno como Mano.
     * 
     * @param game la partida en ejecución
     */
    public static void generatePlayers(Pumba game) {
        int mano = chooseManoPlayer(game);
        for (int i = 0; i < game.getNumberOfPlayers(); i++) {
            boolean isMano = (mano == i);
            game.getPlayers().add(new Player(game, i + 1, isMano));
        }
    }

    /**
     * Elección aleatoria del jugador que será mano Mano.
     * 
     * @param game la partida en ejecución
     * @return int con el turno del jugador que es mano
     */
    private static int chooseManoPlayer(Pumba game) {
        int mano = game.setTurn((int) (Math.random() * game.getNumberOfPlayers()));
        // game.setTurn(0); /* DESCOMENTAR PARA QUE EL JUGADOR PRINCIPAL SEA MANO */
        System.out.println("MANO --- jugador " + (mano + 1));
        return mano;
    }

    /**
     * Se reparte una mano de cartas a cada jugador. Las cartas repartidas se
     * retiran del mazo. Si el jugador es la persona, las cartas se colocan boca
     * arriba.
     * 
     * @param drawPile la pila de robo de la partida
     * @param players  un ArrayList de los jugadores en la partida
     */
    public static void dealCards(CardsDeck drawPile, ArrayList<Player> players) {
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

    /* ** FUNCIONES PARA INCIAR OTRA RONDA DE JUEGO ** */

    /**
     * Resetea la ronda de juego, devolviendo todas las cartas al mazo de robo,
     * eliminando los jugadores que tengan igual o más de 100 puntos, eligiendo un
     * nuevo jugador Mano y repartiendo nuevamente cartas a los jugadores.
     * 
     * @param game la partida en ejecución
     * @return un String con el listado de jugadores que han sido eliminados
     */
    public static String restartRound(Pumba game) {
        PumbaUtilities.resetDrawPile(game);
        String removed = PumbaUtilities.remove100ScorePlayers(game);
        PumbaUtilities.chooseManoPlayer(game);
        PumbaUtilities.dealCards(game.getDrawPile(), game.getPlayers());
        return removed;
    }

    /**
     * Elimina a aquellos jugadores que tienen igual o más de 100 puntos.
     * 
     * @param game la partida en ejecución
     * @return un String con el listado de jugadores que han sido eliminados
     */
    private static String remove100ScorePlayers(Pumba game) {
        ArrayList<Player> players = (ArrayList<Player>) game.getPlayers().clone();
        String removed = "Se eliminan: <br/><ul>";
        int count = 0;
        for (Player p : game.getPlayers()) {
            if (p.getScore() >= 100) {
                System.out.println("XXXX Eliminado " + p.getPlayerName() + " XXXX");
                removed += String.format("<li>%s</li>", p.getPlayerName(true));
                players.remove(p);
                count++;
            }
        }
        if (count == 0)
            return "No se elimina a ningún jugador.<br/>";
        game.setPlayers(players);
        resetPlayersNumbers(game.getPlayers());
        return (removed + "</ul><br/>");
    }

    /**
     * Asigna un nuevo número a cada jugador en función del número de jugadores que
     * han quedado en juego.
     * 
     * @param players los jugadores que han quedado en juego
     */
    private static void resetPlayersNumbers(ArrayList<Player> players) {
        for (int p = 1; p < players.size(); p++) {
            Player player = players.get(p);
            int n = player.getNumber();
            player.setNumber(n < p + 1 ? n : p + 1);
        }
    }

    /**
     * Devuelve todas las cartas que hay en la pila de descartes y en las manos de
     * los jugadores a la pila de robo, colocando de nuevo las cartas derechas.
     * 
     * @param game la partida en ejecución
     */
    private static void resetDrawPile(Pumba game) {
        ArrayList<Card> discardPile = game.getDiscardPile();
        CardsDeck drawPile = game.getDrawPile();
        ArrayList<Player> players = game.getPlayers();
        System.out.println("\nCOLOCAR PILA DE ROBO");
        drawPile.returnCards(discardPile);
        System.out.println("DEVOLVER MANOS A LA PILA");
        for (Player p : players)
            drawPile.returnCards(p.getCardHand().getCards());
        drawPile.shuffle();
        for (Card c : drawPile.getCards())
            c.rotateCard("center", "transform-origin: bottom center; transform: scale(1) rotate(0);");
        game.setDiscardPile(new ArrayList<Card>());
    }

    /* *** MOVIMIENTOS DE LOS MAZOS *** */

    /**
     * Suelta una carta concreta en la pila de descartes, dando una rotación para
     * simular desorden del mazo y colocando la carta boca arriba.
     * 
     * @param _card       la carta soltada por el jugador principal
     * @param discardPile la pila de descartes de la partida
     */
    public static void dropOnDiscardPile(Card _card, ArrayList<Card> discardPile) {
        float rotation = -0.004f + (float) (Math.random() * (0.004f + 0.004f));
        _card.setUncovered(true).rotateCard("center", Float.toString(rotation));
        discardPile.add(_card);
    }

    /**
     * Retira varias cartas del mazo de robo.
     * 
     * @param n    el número de cartas robadas.
     * @param game la partida en ejecución
     * @return un ArrayList con las cartas robadas
     */
    public static ArrayList<Card> drawCardsFromDeck(int n, Pumba game) {
        ArrayList<Card> drawnCards = new ArrayList<>();
        CardsDeck drawPile = game.getDrawPile();
        for (int i = 0; i < n; i++) {
            Card c = drawPile.drawLastCard();
            if (c == null) {
                flipDiscardsPile(game);
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
     * 
     * @param game la partida en ejecución
     */
    private static void flipDiscardsPile(Pumba game) {
        ArrayList<Card> discardPile = game.getDiscardPile();
        CardsDeck drawPile = game.getDrawPile();
        System.out.println("VOLTEAR DESCARTES");
        Card lastCard = discardPile.remove(discardPile.size() - 1);
        drawPile.returnCards(discardPile);
        drawPile.shuffle();
        discardPile = new ArrayList<>();
        discardPile.add(lastCard);
        game.setDiscardPile(discardPile);
    }

    /**
     * Indica si el enlace de la pila de descarte va a estar disponible para que el
     * jugador principal (persona) robe carta, o no. La pila se activara si es el
     * turno del jugador, no es ronda de elección de palo (la persona echó una sota)
     * o jugador que robe (la persona echó un as), no es la ronda de puntuación
     * final, el número de cartas de la persona no es 0 y si no tiene cartas válidas
     * en mano para jugar.
     * 
     * @param game         la partida en ejecución
     * @param personPlayer la persona (jugador no máquina)
     * @return true para activar la pila de descartes y false para desactivarla
     */
    public static boolean isDrawPileEnabled(Pumba game, Player personPlayer) {
        Card c = personPlayer.checkCardOnTable();
        String s = personPlayer.getGame().getSuit();
        if (game.getTurn() != 0 || game.isSelectionRound || game.isScoreRound
                || (personPlayer.getNumberOfCardsInHand() == 0)
                || (personPlayer.getCardHand().getValidCards(c, s).size() != 0))
            return false;
        return true;
    }

    /* ** CAMBIOS DURANTE LA PARTIDA ** */

    /**
     * Cambia el sentido de la ronda de juego.
     * 
     * @param game la partida en ejecución
     * @return devuelve un objeto Pumba de la partida en juego.
     */
    public static void reverseDirection(Pumba game) {
        System.out.println("CAMBIO SENTIDO");
        game.setPlayDirection(game.getPlayDirection() * -1);
    }

    /**
     * Elige un palo aleatoriamente, sin que sea el mismo que está en juego y cambia
     * el palo en juego.
     * 
     * @param game la partida en ejecución
     * @return un String con el palo escogido.
     */
    public static String chooseSuit(Pumba game) {
        HashMap<String, Integer> number = new HashMap<>();
        ArrayList<Card> cardHand = game.getPlayerOfTurn().getCardHand().getCards();
        for (Card c : cardHand) {
            String suit = c.getSuitStr();
            number.put(suit, number.containsKey(suit) ? (number.get(suit) + 1) : 1);
        }
        String chosenSuit = null;
        int count = 0;
        for (Map.Entry<String, Integer> set : number.entrySet()) {
            if (chosenSuit == null || count < set.getValue()) {
                count = set.getValue();
                chosenSuit = set.getKey();
            }
        }
        return changeSuit(chosenSuit, game);
    }

    /**
     * Cambia el palo en juego a aquel pasado por parámetros.
     * 
     * @param _suit el palo al que cambia el juego.
     * @param game  la partida en ejecución
     * @return el palo al que cambia el juego.
     */
    public static String changeSuit(String _suit, Pumba game) {
        game.setSuit(_suit);
        System.out.println("---CAMBIO DE PALO A " + _suit.toUpperCase());
        return _suit;
    }

    /**
     * Elige un jugador aleatorio, sin que sea el que está en turno.
     * 
     * @param game la partida en ejecución
     * @return el jugador escogido.
     */
    public static Player choosePlayer(Pumba game) {
        int n;
        do {
            n = (int) (Math.random() * game.getNumberOfPlayers());
        } while (game.getTurn() == n);
        Player chosenPlayer = game.getPlayers().get(n);
        System.out.println("--" + chosenPlayer.getPlayerName(true) + " elegido para robar");
        return chosenPlayer;
    }

    /**
     * Hace que se active el botón de ¡PUMBA! para que la persona lo pulse, además
     * de activar isPumbaTime para checkear si la persona ha pulsado o no el botón
     * posteriormente.
     * 
     * @param player      el jugador a comprobar
     * @param droppedCard la carta jugada en la ronda
     */
    public static void activatePumbaTime(Player player, Card droppedCard) {
        ArrayList<Card> validCards = player.getCardHand().getValidCards(droppedCard, null);
        activatePumbaTime(player, droppedCard, validCards);
    }

    /**
     * Hace que se active el botón de ¡PUMBA! para que la persona lo pulse, además
     * de activar isPumbaTime para checkear si la persona ha pulsado o no el botón
     * posteriormente.
     * 
     * @param player      el jugador a comprobar
     * @param droppedCard la carta jugada en la ronda
     * @param validCards  las cartas válidas en la mano del jugador
     */
    public static void activatePumbaTime(Player player, Card droppedCard, ArrayList<Card> validCards) {
        if (player.getNumberOfCardsInHand() == 2 && validCards.size() >= 1) {
            player.setPumbaTime(true);
            System.out.printf("\033[1;31m-*- PUMBA ACTIVO  %s -*-\033[0m\n", player.getPlayerName().toUpperCase());
        }
    }

    /**
     * Hace que se active el momento de decir ¡PUMBA! para los jugadores máquina.
     * 
     * @param player     el jugador a comprobar
     * @param validCards las cartas válidas en la mano del jugador
     */
    public static void activatePumbaTime(Player player, ArrayList<Card> validCards) {
        if (player.getNumberOfCardsInHand() == 2 && validCards.size() >= 1) {
            if (player.getNumber() != 1) {
                player.setPumbaTime(true);
                System.out.printf("\033[1;34m--- PUMBA ACTIVO %s ---\033[0m\n", player.getPlayerName().toUpperCase());
            }
        }
    }

    /* ** PUNTUACIONES ** */

    /**
     * Asigna la puntuación de la ronda a cada jugador en función de sus cartas en
     * mano.
     * 
     * @param game la partida en ejecución
     */
    public static void setPlayersScore(Pumba game) {
        int playerScore = 101;
        game.isScoreRound = true;
        for (Player p : game.getPlayers()) {
            int score = 0;
            for (Card c : p.getCardHand().getCards()) {
                score += c.setUncovered(true).getScore();
            }
            p.updateScore(score);
            if (p.getScore() < playerScore)
                winner = p.getNumber();
        }
    }

    public static int getWinner() {
        return winner;
    }

}
