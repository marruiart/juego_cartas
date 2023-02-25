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

    public int getNumeroCartas() {
        return this.cartas.size();
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
        String jugador = "mano" + getJugador().getNumero();
        String mano = String.format("<div class='%s'>", jugador);
        float n = getNumeroCartas() / 2;
        float rotacion = (float)Math.floor(n) * -0.02f;
        System.out.println((float)Math.floor(n));
        System.out.println(rotacion);
        for (int i = 0; i < getNumeroCartas(); i++) {
            System.out.println("carta " + (i + 1) + " " + rotacion);
            rotacion = (getNumeroCartas() % 2 == 0 && rotacion == 0) ? 0.02f : rotacion;
            if ((i + 0.5) < n) {
                this.cartas.get(i).rotarCarta("right", Float.toString(rotacion));
            } else if ((i + 0.5) > n) {
                this.cartas.get(i).rotarCarta("left", Float.toString(rotacion));
            } else {
                this.cartas.get(i).rotarCarta("center", "0");
                System.out.println("rotación" + rotacion);
            }
            mano += this.cartas.get(i);
            rotacion += 0.02f; 
        }
        return mano + "</div>";
    }
}
