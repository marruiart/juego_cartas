package modules;

import modules.Enums.*;

public class Carta {
    private Numeros numero;
    private Palos palo;
    private String imagen;
    private Integer puntos;
    private boolean descubierta;

    public Carta(Numeros numero, Palos palo) {
        this(numero, palo, false);
    }

    public Carta(Numeros numero, Palos palo, boolean descubierta) {
        this.numero = numero;
        this.palo = palo;
        this.descubierta = descubierta;
        this.imagen = "assets/img/" + (this.descubierta ? (getNumero() + "_" + getPalo()) : "reverso") + ".png";
    }

    public String getNumero() {
        return numero.toString().toLowerCase();
    }

    public String getPalo() {
        return palo.toString().toLowerCase();
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(int p) {
        this.puntos = p;
    }

    public boolean getDescubierta() {
        return this.descubierta;
    }

    public void setDescubierta(boolean d) {
        this.descubierta = d;
        this.imagen = "assets/img/" + (this.descubierta ? (getNumero() + "_" + getPalo()) : "reverso") + ".png";
    }

    public String getStringCarta() {
        return this.getNumero() + " de " + this.getPalo();
    }

    public void girarCarta() {
        this.descubierta = this.descubierta ? false : true;
        this.imagen = "assets/img/" + (this.descubierta ? (getNumero() + "_" + getPalo()) : "reverso") + ".png";
    }

    public boolean esValida(Carta c, String palo) {
        if (palo != null && this.getPalo().equals(palo))
            return true;
        if (c.getNumero().equals("dos")) {
            if (this.getNumero().equals("dos"))
                return true;
            else
                return false;
        }
        if (this.getNumero().equals("sota")) {
            return true;
        }
        if (c.getNumero().equals(this.getNumero()) || c.getPalo().equals(this.getPalo())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        return this.getClass() == o.getClass() && (this.numero == ((Carta) o).numero && this.palo == ((Carta) o).palo);
    }

    @Override
    public String toString() {
        return "<img class=\"carta\" src=" + imagen + " width='100'>";
    }
}
