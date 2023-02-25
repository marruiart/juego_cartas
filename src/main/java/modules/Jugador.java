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

    public int getCartasEnMano() {
        return this.cartas.getNumeroCartas();
    }

    public String getNombreJugador() {
        return getNombreJugador(false);
    }

    public String getNombreJugador(boolean primeraMayus) {
        if (primeraMayus)
            return String.format("Jugador %d", numero);
        else
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
        return this.soltarCarta(null);
    }

    public Carta soltarCarta(Carta c) {
        Carta carta = (c == null) ? this.cartas.removeCarta() : this.cartas.removeCarta(c);
        System.out.println("Suelta " + carta.getStringCarta());
        carta.setDescubierta(true);
        partida.soltarEnDescartes(carta);
        return carta;
    }

    public void robarCarta() {
        this.robarCartas(1);
    }

    public void robarCartas(int n) {
        System.out.printf("%s roba %d carta%s\n", this.getNombreJugador(), n, n == 1 ? "" : "s");
        ArrayList<Carta> cartasRobadas = partida.robarCartasMazo(n);
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
        int[][] posiciones = { { 2 }, { 1, 3 }, { 0, 2, 4 }, { 0, 1, 3, 4 }, { 0, 1, 2, 3, 4 } };
        String posicion = "";
        if (this.esMaquina()) {
            posicion = "posicion" + posiciones[partida.getJugadores().size() - 2][this.getNumero() - 2];
        }
        String textoMano = esMano ? " (Mano)" : "";
        String textoMaquina = esMaquina ? "" : " (Tú)";
        String jugador = this.getNombreJugador().replace(" ", "");
        return String.format(
                "<div class='" + jugador + " " + posicion + "'><h2 class='nombre'>%s %s%s</h2>%s</div>",
                this.getNombreJugador(), textoMano, textoMaquina, this.cartas.toString());
    }
}
