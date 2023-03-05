package modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PumbaUtilities {

    /* ** FUNCIONES PARA INCIAR LA PARTIDA ** */

    /**
     * Establece la puntuación que tiene cada carta en función de su número y la
     * asigna al atributo score de la carta.
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
     */
    public static void generatePlayers(Pumba game) {
        game.setTurn((int) (Math.random() * game.getNumberOfPlayers()));
        // turn = 0; /* CAMBIAR: DESCOMENTAR PARA QUE EL JUGADOR PRINCIPAL SEA MANO
        // */
        System.out.println("MANO --- jugador " + (game.getTurn() + 1));
        for (int i = 0; i < game.getNumberOfPlayers(); i++) {
            boolean isMano = (game.getTurn() == i);
            game.getPlayers().add(new Player(game, i + 1, isMano));
        }
    }

    /**
     * Se reparte una mano de cartas a cada jugador. Las cartas repartidas se
     * retiran del mazo. Si el jugador es la persona, las cartas se colocan boca
     * arriba.
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

    /* *** MOVIMIENTOS DE LOS MAZOS *** */

    /**
     * Suelta una carta concreta en la pila de descartes, dando una rotación para
     * simular desorden del mazo y colocando la carta boca arriba.
     * 
     * @param _card la carta soltada.
     */
    public static void dropOnDiscardPile(Card _card, ArrayList<Card> discardPile) {
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

    public static boolean isDrawPileEnabled(Pumba game, Player personPlayer) {
        Card c = personPlayer.checkCardOnTable();
        String s = personPlayer.getGame().getSuit();
        if (game.getTurn() != 0 || game.isSelectionRound() || game.isScoreRound()
                || (personPlayer.getNumberOfCardsInHand() == 0)
                || (personPlayer.getCardHand().getValidCards(c, s).size() != 0))
            return false;
        return true;
    }

    /* ** CAMBIOS DURANTE LA PARTIDA ** */

    /**
     * Cambia el sentido de la ronda de juego.
     * 
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

    /* ** PUNTUACIONES ** */

    /**
     * Asigna la puntuación de la ronda a cada jugador en función de sus cartas en
     * mano.
     */
    public static void setPlayersScore(Pumba game) {
        game.setIsScoreRound(true);
        for (Player p : game.getPlayers()) {
            int score = 0;
            for (Card c : p.getCardHand().getCards()) {
                score += c.setUncovered(true).getScore();
            }
            p.updateScore(score);
        }
    }

}
