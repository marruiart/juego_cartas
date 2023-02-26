package modules;

import modules.Enums.*;

public class Card {
    private Numbers number;
    private Suits suit;
    private String img;
    private String rotation;
    private Integer score;
    private boolean uncovered;

    public Card(Numbers _number, Suits _palo) {
        this(_number, _palo, false);
    }

    public Card(Numbers _number, Suits _suit, boolean _uncovered) {
        this.number = _number;
        this.suit = _suit;
        this.uncovered = _uncovered;
        this.img = "assets/img/" + (this.uncovered ? (getNumber() + "_" + getSuit()) : "reverso") + ".png";
        this.rotation = "style='transform-origin: bottom center; transform: scale(1) rotate(0);'";
    }

    public String getNumber() {
        return number.toString().toLowerCase();
    }

    public String getSuit() {
        return suit.toString().toLowerCase();
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

    public Card setUncovered(boolean _uncovered) {
        this.uncovered = _uncovered;
        this.img = "assets/img/" + (this.uncovered ? (getNumber() + "_" + getSuit()) : "reverso") + ".png";
        return this;
    }

    public String getCardNameLink() {
        return this.getNumber() + "_" + this.getSuit();
    }

    public String getStringCard() {
        return this.getNumber() + " de " + this.getSuit();
    }

    public void flipCard() {
        this.uncovered = this.uncovered ? false : true;
        this.img = "assets/img/" + (this.uncovered ? (getNumber() + "_" + getSuit()) : "reverso") + ".png";
    }

    public void rotateCard(String _position, String _rotation) {
        this.rotation = String.format("style='transform-origin: bottom %s; transform: scale(1) rotate(%sturn);'",
                _position,
                _rotation);
    }

    public boolean isValid(Card c, String _suit) {
        if (_suit != null) {
            if (this.getSuit().equals(_suit))
                return true;
            else
                return false;
        }
        if (c.getNumber().equals("dos")) {
            if (this.getNumber().equals("dos"))
                return true;
            else
                return false;
        }
        if (this.getNumber().equals("sota")) {
            return true;
        }
        if (c.getNumber().equals(this.getNumber()) || c.getSuit().equals(this.getSuit())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        return this.getClass() == o.getClass() && (this.number == ((Card) o).number && this.suit == ((Card) o).suit);
    }

    public String toStringAnchorTag(boolean disable) {
        return String.format(
                "<a href='http://localhost:8080/juegos_cartas/pumba.jsp?start=0&card=%s' %s>%s</a>",
                this.getCardNameLink(), disable ? "class='disabled'" : "", this.toString());
    }

    @Override
    public String toString() {
        return String.format("<img class='card' src=%s %s>", this.img, this.rotation);
    }
}
