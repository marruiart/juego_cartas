package modules;

import java.util.ArrayList;

public class Mano {
    private Jugador jugador;
    private ArrayList<Carta> cartas = new ArrayList<Carta>();
    private ArrayList<Carta> cartasValidas;

    public Mano(Jugador jugador, ArrayList<Carta> cartas) {
        this.jugador = jugador;
        this.cartas = cartas;
    }

    public Jugador getJugador() {
        return this.jugador;
    }

    public ArrayList<Carta> getCartas() {
        return this.cartas;
    }

    public ArrayList<Carta> getCartasValidas(Carta centroMesa, String palo) {
        cartasValidas = new ArrayList<Carta>();
        for (Carta c : cartas) {
            if (c.esValida(centroMesa, palo))
                cartasValidas.add(c);
        }
        return this.cartasValidas;
    }

    public Carta removeCarta() {
        int n = (int) (Math.random() * cartas.size());
        Carta carta = cartas.remove(n);
        return carta;
    }

    public Carta removeCarta(Carta c) {
        cartas.remove(c);
        return c;
    }

    public void addCarta(Carta carta) {
        if (carta != null) {
            cartas.add(carta);
        }
    }

    @Override
    public String toString() {
        String jugador = getJugador().getStringJugador().replace(" ", "");
        String mano = "<div class='mano " + jugador + "'>";
        for (Carta c : this.cartas) {
            mano += c;
        }
        return mano + "</div>";
    }
}
