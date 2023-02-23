package modules;

import java.util.ArrayList;

public class Pumba {
    private Baraja mazo;
    private ArrayList<Carta> descartes;
    private ArrayList<Jugador> jugadores;
    private int turno;
    private int numeroJugadores;
    private boolean partidaIniciada;
    private int chupateDos;
    private int sentidoTurno;

    public Pumba(int numeroJugadores) {
        this.mazo = new Baraja();
        this.descartes = new ArrayList<>();
        this.jugadores = new ArrayList<>();
        this.numeroJugadores = numeroJugadores;
        this.partidaIniciada = false;
        this.chupateDos = 2;
        this.sentidoTurno = 1;
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

    public Carta getCentroMesa() {
        return this.descartes.get(this.descartes.size() - 1);
    }

    public ArrayList<Jugador> getJugadores() {
        return this.jugadores;
    }

    public int getTurno() {
        return this.turno;
    }

    public int setTurno() {
        this.turno += sentidoTurno;
        if ((this.turno) % this.numeroJugadores == -1)
            this.turno = this.numeroJugadores - 1;
        else
            this.turno = (this.turno) % this.numeroJugadores;
        System.out.println(turno + 1);
        return this.turno;
    }

    public boolean estaIniciada() {
        return this.partidaIniciada;
    }

    public void setPartidaIniciada(boolean partidaIniciada) {
        this.partidaIniciada = partidaIniciada;
    }

    public Jugador getJugadorTurno() {
        return this.jugadores.get(turno);
    }

    public void soltarEnDescartes(Carta carta) {
        descartes.add(carta);
    }

    public Carta robarCartaMazo() {
        return robarCartasMazo(1).get(0);
    }

    public ArrayList<Carta> robarCartasMazo(int n) {
        ArrayList<Carta> cartasRobadas = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Carta c = mazo.sacarPrimeraCarta();
            if (c == null) {
                this.voltearDescartes();
                c = mazo.sacarPrimeraCarta();
            }
            cartasRobadas.add(c);
        }
        return cartasRobadas;
    }

    public void voltearDescartes() {
        Carta ultima = this.descartes.remove(this.descartes.size() - 1);
        /* System.out.println(ultima.getPalo() + ultima.getNumero()); */
        mazo.devolverCartas(this.descartes);
        mazo.barajar();
        this.descartes = new ArrayList<>();
        this.descartes.add(ultima);
    }

    public void cambioSentido() {
        System.out.println("CAMBIO SENTIDO");
        this.sentidoTurno *= -1;
    }

    public Carta jugadaEspecialDos(Jugador jugador, Carta centroMesa) {
        Carta cartaSoltada = null;
        if (this.chupateDos == -1) {
            cartaSoltada = jugador.soltarCartaValida(centroMesa, centroMesa.getPalo());
            if (cartaSoltada != null) {
                this.chupateDos = 2;
            }
            return cartaSoltada;
        }
        if (this.chupateDos != 8)
            cartaSoltada = jugador.soltarCartaValida(centroMesa);
        if (cartaSoltada == null) {
            /* System.out.println("Chupa: " + this.chupateDos + " cartas"); */
            jugador.robarCartas(chupateDos);
            this.chupateDos = -1;
        } else {
            this.chupateDos += 2;
        }
        return cartaSoltada;
    }

    public String ejecutarJugada() {
        Jugador jugador = this.getJugadorTurno();
        String partida = "";
        Carta cartaSoltada;
        String jugada = "";
        if (!this.estaIniciada()) {
            System.out.println("INICIADA");
            this.setPartidaIniciada(true);
            cartaSoltada = jugador.soltarCarta();
            jugador.robarCarta();
            jugada = " echa " + cartaSoltada.getStringCarta();
        } else {
            Carta centroMesa = this.getCentroMesa();
            if (centroMesa.getNumero().equals("dos")) {
                int numeroCartas = jugador.getMano().getCartas().size();
                cartaSoltada = jugadaEspecialDos(jugador, centroMesa);
                int cartasChupadas = jugador.getMano().getCartas().size() - numeroCartas;
                if ((this.chupateDos == -1 && cartasChupadas > 1) || cartasChupadas > 1) {
                    jugada = " chupa " + cartasChupadas + " cartas";
                } else {
                    jugada = (cartaSoltada == null) ? " roba carta"
                            : (" echa " + cartaSoltada.getStringCarta());
                }
            } else {
                cartaSoltada = jugador.soltarCartaValida(centroMesa);
                jugada = (cartaSoltada == null) ? " roba carta"
                        : (" echa " + cartaSoltada.getStringCarta());
            }
            jugador.robarCarta();
        }
        partida = jugador.getStringJugador() + jugada + (jugada.contains("echa") ? " y roba carta. " : ". ");
        if (cartaSoltada != null && cartaSoltada.getNumero().equals("siete"))
            this.cambioSentido();
        this.setTurno();
        jugador = this.getJugadorTurno();
        String turno = this.getJugadorTurno().esMaquina() ? "Turno del " + jugador.getStringJugador() + "."
                : "Es tu turno, elige carta.";
        return partida + turno;
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
