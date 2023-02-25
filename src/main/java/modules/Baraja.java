package modules;

import java.util.*;
import modules.Enums.*;

public class Baraja {
    private ArrayList<Carta> cartas = new ArrayList<Carta>();

    public Baraja() {
        generarMazo();
        barajar();
    }

    public ArrayList<Carta> getCartas() {
        for (Carta c : this.cartas)
            c.setDescubierta(false).setEnlace(null);
        return this.cartas;
    }

    public Carta sacarCarta() {
        int n = (int) (Math.random() * cartas.size());
        return sacarCarta(n);
    }

    public Carta sacarCarta(int posicion) {
        if (cartas.size() == 0) {
            return null;
        }
        Carta carta = cartas.remove(posicion);
        return carta;
    }

    public Carta sacarPrimeraCarta() {
        if (cartas.size() == 0) {
            return null;
        }
        Carta carta = cartas.remove(0);
        return carta;
    }

    public Carta sacarUltimaCarta() {
        if (this.cartas.size() == 0) {
            return null;
        }
        Carta carta = this.cartas.remove(this.cartas.size() - 1);
        return carta;
    }

    public void devolverCarta(Carta carta) {
        ArrayList<Carta> cartas = new ArrayList<>();
        cartas.add(carta);
        devolverCartas(cartas);
    }

    public void devolverCartas(ArrayList<Carta> cartas) {
        for (Carta c : cartas) {
            c.setDescubierta(false).setEnlace(null);
            this.cartas.add(c);
        }
    }

    public void generarMazo() {
        Palos[] palos = Palos.values();
        Numeros[] numeros = Numeros.values();
        for (Palos p : palos) {
            for (Numeros n : numeros) {
                cartas.add(new Carta(n, p));
            }
        }
    }

    public void barajar() {
        Collections.shuffle(cartas);
    }
}
