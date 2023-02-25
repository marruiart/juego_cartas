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
        System.out.println("Cartas en mazo: " + this.mazo.getCartas().size());
        return this.mazo.getCartas();
    }

    public ArrayList<Carta> getDescartes() {
        for (Carta c : this.descartes)
            c.setDescubierta(true);
        System.out.println("Cartas en descartes: " + this.descartes.size());
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

    public Pumba setTurno() {
        this.turno += sentidoTurno;
        if ((this.turno) % this.numeroJugadores == -1)
            this.turno = this.numeroJugadores - 1;
        else
            this.turno = (this.turno) % this.numeroJugadores;
        System.out.println("-------SET: " + this.getJugadorTurno().getNombreJugador().toUpperCase());
        return this;
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
        carta.rotarCarta("center", "0");
        descartes.add(carta);
    }

    public Carta robarCartaMazo() {
        return robarCartasMazo(1).get(0);
    }

    public ArrayList<Carta> robarCartasMazo(int n) {
        System.out.println(getJugadorTurno().getNombreJugador() + " robando " + n + " cartas...");
        ArrayList<Carta> cartasRobadas = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Carta c = mazo.sacarPrimeraCarta();
            if (c == null) {
                this.voltearDescartes();
                c = mazo.sacarPrimeraCarta();
            }
            if (c != null) {
                cartasRobadas.add(c);
            }
        }
        return cartasRobadas;
    }

    public void voltearDescartes() {
        System.out.println("VOLTEAR DESCARTES");
        Carta ultima = this.descartes.remove(this.descartes.size() - 1);
        mazo.devolverCartas(this.descartes);
        mazo.barajar();
        this.descartes = new ArrayList<>();
        this.descartes.add(ultima);
    }

    public Pumba cambioSentido() {
        System.out.println("CAMBIO SENTIDO");
        this.sentidoTurno *= -1;
        return this;
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
            System.out.println("---" + jugador.getNombreJugador() + " chupa: " + this.chupateDos + " cartas---");
            jugador.robarCartas(chupateDos);
            this.chupateDos = -1;
        } else {
            this.chupateDos += 2;
        }
        return cartaSoltada;
    }

    public String ejecutarJugada() {
        Jugador jugador = this.getJugadorTurno();
        System.out.println("\nTURNO: " + jugador.getNombreJugador().toUpperCase());
        String partida = "";
        Carta cartaSoltada;
        String jugada = "";
        String turno = "";
        boolean repiteTurno = true;
        if (!this.estaIniciada()) {
            this.setPartidaIniciada(true);
            cartaSoltada = jugador.soltarCarta();
            jugada = " echa " + cartaSoltada.getStringCarta() + ". ";
        } else {
            Carta centroMesa = this.getCentroMesa();
            if (centroMesa.getNumero().equals("dos")) {
                int numeroCartas = jugador.getCartasEnMano();
                cartaSoltada = jugadaEspecialDos(jugador, centroMesa);
                int cartasChupadas = jugador.getCartasEnMano() - numeroCartas;
                if ((this.chupateDos == -1 && cartasChupadas > 1) || cartasChupadas > 1) {
                    jugada = String.format(" chupa %d cartas. ", cartasChupadas);
                } else {
                    if (cartaSoltada == null) {
                        jugador.robarCarta();
                        jugada = " roba carta. ";
                    } else {
                        jugada = String.format(" echa %s. ", cartaSoltada.getStringCarta());
                    }
                }
            } else {
                cartaSoltada = jugador.soltarCartaValida(centroMesa);
                if (cartaSoltada == null) {
                    if (centroMesa.getNumero().equals("rey")) {
                        System.out.println("--YA NO REPITE TURNO");
                        repiteTurno = false;
                    }
                    jugador.robarCarta();
                    jugada = " roba carta. ";
                } else {
                    jugada = String.format(" echa %s. ", cartaSoltada.getStringCarta());
                }
            }
        }
        partida = jugador.getStringJugador(true) + jugada;
        if (jugador.getCartasEnMano() == 0) {
            return "FIN DE LA PARTIDA, ¡GANA EL JUGADOR " + jugador.getNombreJugador().toUpperCase() + "!";
        } else if (cartaSoltada != null && cartaSoltada.getNumero().equals("siete")) {
            this.cambioSentido();
            partida += "Cambio de sentido. ";
        } else if (cartaSoltada != null && cartaSoltada.getNumero().equals("caballo")) {
            System.out.println("SALTA TURNO: no juega " + this.getJugadorTurno().getNombreJugador());
            this.setTurno();
            partida += String.format("Salta el turno del %s. ", this.getJugadorTurno().getNombreJugador());
        }
        jugador = this.setTurno().getJugadorTurno();
        turno = this.getJugadorTurno().esMaquina() ? String.format("Turno del %s.", jugador.getNombreJugador())
                : "Es tu turno, elige carta.";
        if (cartaSoltada != null && repiteTurno && cartaSoltada.getNumero().equals("rey")) {
            cambioSentido().setTurno();
            System.out.println("REY - VUELVE A JUGAR: " + this.getJugadorTurno().getNombreJugador());
            turno = String.format("Vuelve a jugar el %s. ", this.getJugadorTurno().getNombreJugador());
        }
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
