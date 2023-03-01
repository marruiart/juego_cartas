package modules;

import modules.Enums.*;

public class Card {
    private Numbers number;
    private Suits suit;
    private Integer score;
    private boolean uncovered;
    private boolean isDrawCard;
    private String img;
    private String rotation;

    public Card() {
        // this.img = "assets/img/transparent.png";
        // this.rotation = "transform-origin: bottom center; transform: scale(1)
        // rotate(0);";
        this.isDrawCard = true;
    }

    public Card(Numbers _number, Suits _suit) {
        this(_number, _suit, false);
    }

    public Card(String _number, String _suit) {
        this(Numbers.getNumber(_number), Suits.getSuit(_suit), false);
    }

    public Card(Numbers _number, Suits _suit, boolean _uncovered) {
        this.number = _number;
        this.suit = _suit;
        this.uncovered = _uncovered;
        this.isDrawCard = false;
        this.img = "assets/img/" + (this.uncovered ? (getNumber() + "_" + getSuitStr()) : "reverso") + ".png";
        this.rotation = "transform-origin: bottom center; transform: scale(1) rotate(0);";
    }

    /* GETTERS Y SETTERS */

    public String getNumber() {
        return number.toString().toLowerCase();
    }

    public Suits getSuit() {
        return this.suit;
    }

    public String getSuitStr() {
        return this.suit.toString().toLowerCase();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(int p) {
        this.score = p;
    }

    public boolean getUncovered() {
        return this.uncovered;
    }

    /**
     * Cambia la imagen de la carta a la del reverso de la carta, en caso de que
     * _uncovered sea false, o al anverso si es true.
     * 
     * @param _uncovered boolean que indica si la carta se tapa o se destapa.
     * @return la carta que se ha tapado o destapado.
     */
    public Card setUncovered(boolean _uncovered) {
        this.uncovered = _uncovered;
        this.img = String.format("assets/img/%s.png",
                (this.uncovered ? (getNumber() + "_" + getSuitStr()) : "reverso"));
        return this;
    }

    public boolean isDrawCard() {
        return this.isDrawCard;
    }

    public String getCardNameLink() {
        return this.getNumber() + "_" + this.getSuitStr();
    }

    public String getCardName() {
        return this.getNumber() + " de " + this.getSuitStr();
    }

    public void flipCard() {
        setUncovered(this.uncovered ? false : true);
    }

    /**
     * Asigna una rotación de la carta que se añadirá al estilo CSS en línea.
     * 
     * @param _position indica el eje de rotación de la carta
     * @param _rotation indica los grados de giro de la carta
     */
    public void rotateCard(String _position, String _rotation) {
        this.rotation = String.format("transform-origin: bottom %s; transform: scale(1) rotate(%sturn);",
                _position, _rotation);
    }

    /**
     * Devuelve si la carta es válida o no con respecto a la carta que hay en el
     * centro de la mesa.
     * 
     * @param _cardOnTable carta en el centro de la mesa
     * @return true si la carta es válida
     * @return false si la carta no es válida (no se puede soltar)
     */
    public boolean isValid(Card _cardOnTable) {
        return isValid(_cardOnTable, null);
    }

    /**
     * Devuelve si la carta es válida o no con respecto a la carta que hay en el
     * centro de la mesa, teniendo en cuenta el palo que hay en juego.
     * 
     * @param _cardOnTable carta en el centro de la mesa
     * @param _suit        palo en juego
     * @return true si la carta es válida
     * @return false si la carta no es válida (no se puede soltar)
     */
    public boolean isValid(Card _cardOnTable, String _suit) {
        if (_cardOnTable == null)
            return true;
        String suitOnTable = _cardOnTable.getSuitStr();
        String numberOnTable = _cardOnTable.getNumber();
        String thisSuit = this.getSuitStr();
        String thisNumber = this.getNumber();
        if (_suit != null) {
            if (thisSuit.equals(_suit)
                    || (numberOnTable.equals("sota") && thisNumber.equals("sota"))
                    || (numberOnTable.equals("dos") && thisNumber.equals("dos")))
                return true;
            else
                return false;
        }
        if (numberOnTable.equals("dos"))
            return (thisNumber.equals("dos") ? true : false);
        if (thisNumber.equals("sota"))
            return true;
        if (numberOnTable.equals(thisNumber) || suitOnTable.equals(thisSuit))
            return true;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        return this.getClass() == o.getClass() && (this.number == ((Card) o).number && this.suit == ((Card) o).suit);
    }

    /**
     * Coloca la carta del jugador 1 entre etiquetas de enlace (<a> carta </a>),
     * añadiendo la ruta del enlace. Recibe un booleano que determina si la carta va
     * a ser clickeable (false) o no (true).
     * 
     * @param disable si es false, el enlace será clickeable, si es true, se
     *                desactiva el enlace.
     * @return String con la carta entre etiquetas de enlace
     */
    public String toStringAnchorTag(boolean disable) {
        String href = "http://localhost:8080/juegos_cartas/pumba.jsp?start=0&card=" + this.getCardNameLink();
        String[] classContent = new String[1];
        if (disable)
            classContent[0] = "disabled";
        else
            classContent[0] = "";
        return Utilities.printAnchor(href, this.toString(), classContent);
    }

    @Override
    public String toString() {
        return Utilities.printImg(this.img, this.img, this.rotation, true, "card");
    }
}
