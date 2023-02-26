package modules;

import java.util.Random;

public class Enums {

    public enum Suits {
        OROS, BASTOS, ESPADAS, COPAS;

        public static Suits getRandom() {
            Random random = new Random();
            Suits[] palos = values();
            return palos[random.nextInt(palos.length)];
        }
    }

    public enum Numbers {
        AS, DOS, TRES, CUATRO, CINCO, SEIS, SIETE, SOTA, CABALLO, REY;

        public static Numbers getRandom() {
            Random random = new Random();
            Numbers[] numeros = values();
            return numeros[random.nextInt(numeros.length)];
        }
    }

}
