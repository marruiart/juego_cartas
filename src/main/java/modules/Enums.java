package modules;

import java.util.Random;

public class Enums {

    public enum Palos {
        OROS, BASTOS, ESPADAS, COPAS;

        public static Palos getRandom() {
            Random random = new Random();
            Palos[] palos = values();
            return palos[random.nextInt(palos.length)];
        }
    }

    public enum Numeros {
        AS, DOS, TRES, CUATRO, CINCO, SEIS, SIETE, SOTA, CABALLO, REY;

        public static Numeros getRandom() {
            Random random = new Random();
            Numeros[] numeros = values();
            return numeros[random.nextInt(numeros.length)];
        }
    }
}
