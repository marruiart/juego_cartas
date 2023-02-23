package modules;

import java.util.ArrayList;

import modules.Enums.Palos;

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

    public String getStringJugador() {
        return String.format("jugador %d", numero);
    }

    public boolean esMano() {
        return this.esMano;
    }

    public boolean esMaquina() {
        return this.esMaquina;
    }

    public Carta verCentroMesa() {
        return partida.getCentroMesa();
    }

    public Carta soltarCartaValida(Carta centroMesa) {
        return soltarCartaValida(centroMesa, null);
    }

    public Carta soltarCartaValida(Carta centroMesa, String cambioPalo) {
        ArrayList<Carta> cartasValidas = this.cartas.getCartasValidas(centroMesa, cambioPalo);
        if (cartasValidas.size() == 0)
            return null;
        int n = (int) (Math.random() * cartasValidas.size());
        return this.soltarCarta(cartasValidas.get(n));
    }

    public Carta soltarCarta() {
        return this.soltarCartas(1).get(0);
    }

    public Carta soltarCarta(Carta c) {
        ArrayList<Carta> carta = new ArrayList<>();
        carta.add(c);
        return this.soltarCartas(carta).get(0);
    }

    public ArrayList<Carta> soltarCartas(int n) {
        ArrayList<Carta> cartasSoltadas = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Carta carta = cartas.removeCarta();
            cartasSoltadas.add(carta);
            carta.setDescubierta(true);
            /* System.out.println(c); */
            partida.soltarEnDescartes(carta);
        }
        return cartasSoltadas;
    }

    public ArrayList<Carta> soltarCartas(ArrayList<Carta> cartasSoltadas) {
        for (Carta c : cartasSoltadas) {
            Carta carta = this.cartas.removeCarta(c);
            carta.setDescubierta(true);
            /* System.out.println(c); */
            partida.soltarEnDescartes(carta);
        }
        return cartasSoltadas;
    }

    public void robarCarta() {
        this.robarCartas(1);
    }

    public void robarCartas(int n) {
        Carta[] cartasRobadas = partida.robarCartasMazo(n);
        for (Carta c : cartasRobadas) {
            if (!this.esMaquina)
                c.setDescubierta(true);
            else
                c.setDescubierta(false);
            cartas.addCarta(c);
        }
    }

    public Mano recibirMano(Jugador jugador, ArrayList<Carta> cartas) {
        this.cartas = new Mano(this, cartas);
        return this.cartas;
    }

    @Override
    public String toString() {
        String textoMano = esMano ? " (Mano)" : "";
        String textoMaquina = esMaquina ? "" : " (Tú)";
        return String.format("<div class=\"jugador\"><h2>%s %s%s</h2>%s</div>",
                this.getStringJugador(), textoMano, textoMaquina, this.cartas.toString());
    }
}
