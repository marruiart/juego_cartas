package modules;

import java.util.ArrayList;

public class Pumba {
    private Baraja mazo;
    private ArrayList<Carta> descartes;
    private ArrayList<Jugador> jugadores;
    private int turno;
    private int numeroJugadores;

    public Pumba(int numeroJugadores) {
        this.mazo = new Baraja();
        this.descartes = new ArrayList<>();
        this.jugadores = new ArrayList<>();
        this.numeroJugadores = numeroJugadores;
        puntuarCartas();
        generarJugadores();
        repartirCartas();
    }

    public ArrayList<Carta> getMazo() {
        return this.mazo.getCartas();
    }

    public ArrayList<Carta> getDescartes() {
        for (Carta c : this.descartes)
            c.setDescubierta(true);
        return this.descartes;
    }

    public ArrayList<Jugador> getJugadores() {
        return this.jugadores;
    }

    public int getTurno() {
        return this.turno;
    }

    public int setTurno() {
        turno = (++this.turno) % this.numeroJugadores;
        return this.turno;
    }

    public Jugador getJugadorTurno() {
        return this.jugadores.get(turno);
    }

    public void soltarEnDescartes(Carta carta) {
        descartes.add(carta);
    }

    public Carta robarCartaMazo() {
        return robarCartasMazo(1)[0];
    }

    public Carta[] robarCartasMazo(int n) {
        Carta[] cartasRobadas = new Carta[n];
        for (int i = 0; i < n; i++) {
            Carta c = mazo.sacarPrimeraCarta();
            if (c == null) {
                this.voltearDescartes();
                c = mazo.sacarPrimeraCarta();
            }
            cartasRobadas[i] = c;
        }
        return cartasRobadas;
    }

    public void voltearDescartes() {
        Carta ultima = this.descartes.remove(this.descartes.size() - 2);
        System.out.println(ultima.getPalo() + ultima.getNumero());
        mazo.devolverCartas(this.descartes);
        mazo.barajar();
        this.descartes = new ArrayList<>();
        this.descartes.add(ultima);
    }

    private void puntuarCartas() {
        for (Carta c : mazo.getCartas()) {
            int p;
            switch (c.getNumero()) {
                case "as":
                    p = 1;
                    break;
                case "tres":
                    p = 3;
                    break;
                case "cuatro":
                    p = 4;
                    break;
                case "cinco":
                    p = 5;
                    break;
                case "seis":
                    p = 6;
                    break;
                case "siete":
                    p = 7;
                case "caballo":
                    p = 9;
                default:
                    p = 10;
            }
            ;
            c.setPuntos(p);
        }
    }

    private void generarJugadores() {
        this.turno = (int) (Math.random() * this.numeroJugadores);
        for (int i = 0; i < this.numeroJugadores; i++) {
            boolean esMano = (this.turno == i) ? true : false;
            jugadores.add(new Jugador(this, i + 1, esMano));
        }
    }

    private void repartirCartas() {
        for (Jugador j : jugadores) {
            int n = j.esMano() ? 5 : 4;
            ArrayList<Carta> cartas = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                Carta c = mazo.sacarCarta();
                if (!j.esMaquina())
                    c.setDescubierta(true);
                cartas.add(c);
            }
            j.recibirMano(j, cartas);
        }
    }

}
