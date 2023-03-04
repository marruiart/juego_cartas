package modules;

import java.util.ArrayList;

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
    public static void generatePlayers(ArrayList<Player> players, int numberOfPlayers, int turn, Pumba game) {
        turn = (int) (Math.random() * numberOfPlayers);
        // turn = 0; /* CAMBIAR: DESCOMENTAR PARA QUE EL JUGADOR PRINCIPAL SEA MANO
        // */
        System.out.println("MANO --- jugador " + (turn + 1));
        for (int i = 0; i < numberOfPlayers; i++) {
            boolean isMano = (turn == i);
            players.add(new Player(game, i + 1, isMano));
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
    
}
