package modules;

import java.util.*;
import modules.Enums.*;

public class CardsDeck {
    private ArrayList<Card> cards = new ArrayList<Card>();

    public CardsDeck() {
        generateDeck();
        shuffle();
    }

    /**
     * Devuelve las cartas que hay en el mazo. Si el boolean que recibe es true,
     * todas las cartas se muestran, si es false, quedan boca abajo.
     * 
     * @param uncover booleano que establece si se muestran o no las cartas.
     * @return ArrayList con las cartas del mazo.
     */
    public ArrayList<Card> getCards(boolean uncover) {
        for (Card c : this.cards)
            c.setUncovered(uncover);
        return this.cards;
    }

    /**
     * Devuelve las cartas que hay en el mazo boca abajo.
     * 
     * @return ArrayList con las cartas del mazo.
     */
    public ArrayList<Card> getCards() {
        return getCards(true); /* CAMBIAR A TRUE PARA DESTAPAR LAS CARTAS DE LOS JUGADORES */
    }

    /**
     * Saca una carta del mazo de forma aleatoria.
     * 
     * @return la carta extraída
     */
    public Card drawCard() {
        int n = (int) (Math.random() * cards.size());
        return drawCard(n);
    }

    /**
     * Saca una carta del mazo en una posición concreta.
     * 
     * @param _position posición de la carta a extraer
     * @return la carta extraída
     */
    public Card drawCard(int _position) {
        if (cards.size() == 0) {
            return null;
        }
        Card card = cards.remove(_position);
        return card;
    }

    /**
     * Saca la primera carta del mazo.
     * 
     * @return la carta extraída
     */
    public Card drawFirstCard() {
        return drawCard(0);
    }

    /**
     * Saca la última carta del mazo.
     * 
     * @return la carta extraída
     */
    public Card drawLastCard() {
        return drawCard(this.cards.size() - 1);
    }

    /**
     * Devuelve una carta concreta al mazo.
     * 
     * @param _card la carta devuelta
     */
    public void returnCard(Card _card) {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(_card);
        returnCards(cards);
    }

    /**
     * Devuelve un grupo de cartas al mazo mazo. Si el boolean que recibe es true,
     * todas las cartas se muestran, si es false, quedan boca abajo.
     * 
     * @param uncover booleano que establece si se muestran o no las cartas.
     * @param _cards  ArrayList de las cartas devueltas
     * @return ArrayList con las cartas del mazo.
     */
    public void returnCards(ArrayList<Card> _cards, boolean uncover) {
        for (Card c : _cards) {
            c.setUncovered(uncover);
            this.cards.add(c);
        }
    }

    /**
     * Devuelve un grupo de cartas al mazo boca abajo.
     * 
     * @param _cards ArrayList de las cartas devueltas
     */
    public void returnCards(ArrayList<Card> _cards) {
        returnCards(_cards, true); /* CAMBIAR A TRUE PARA DESTAPAR LAS CARTAS DEL MAZO DE ROBO */
    }

    /**
     * Genera una baraja de cartas española con 40 cartas (as, dos, tres, cuatro,
     * cinco, seis, siete, sota, caballo y rey de oros, bastos, copas y espadas,
     * respectivamente)
     */
    public void generateDeck() {
        Suits[] suits = Suits.values();
        Numbers[] numbers = Numbers.values();
        for (Suits p : suits) {
            for (Numbers n : numbers) {
                cards.add(new Card(n, p));
            }
        }
    }

    /**
     * Baraja el mazo de cartas.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }
}
