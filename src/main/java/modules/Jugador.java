package modules;

import java.util.ArrayList;

public class Jugador {
    private Pumba partida;
    private int numero;
    private Mano cartas;
    private boolean esMano;
    private boolean esMaquina;

    public Jugador(Pumba partida, int numero, boolean esMano) {
        this.partida = partida;
        this.numero = numero;
        this.esMano = esMano;
        this.esMaquina = numero == 1 ? false : true;
    }

    public Pumba getPartida() {
        return this.partida;
    }

    public int getNumero() {
        return this.numero;
    }

    public Mano getMano() {
        return this.cartas;
    }

    public boolean esMano() {
        return this.esMano;
    }

    public boolean esMaquina() {
        return this.esMaquina;
    }

    public void soltarCarta() {
        this.soltarCartas(1);
    }

    public void soltarCartas(int n) {
        for (int i = 0; i < n; i++) {
            Carta c = cartas.removeCarta();
            c.setDescubierta(true);
            partida.soltarEnDescartes(c);
        }
    }

    public void robarCarta() {
        this.robarCartas(1);
    }

    public void robarCartas(int n) {
        Carta[] cartasRobadas = partida.robarCartasMazo(n);
        for (Carta c : cartasRobadas) {
            if (!this.esMaquina)
                c.setDescubierta(true);
            cartas.addCarta(c);
        }
    }

    public Mano recibirMano(Jugador jugador, ArrayList<Carta> cartas) {
        this.cartas = new Mano(this, cartas);
        return this.cartas;
    }

    @Override
    public String toString() {
        String mano = "<h3>Cartas en la mano:</h3>";
        for (Carta c : cartas.getCartas()) {
            mano += c;
        }
        return String.format("<h2>Jugador %d%s%s%s</h2>", numero, esMano ? " (Mano)" : "", esMaquina ? "" : " (Tú)",
                mano);
    }
}
