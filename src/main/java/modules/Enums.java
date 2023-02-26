package modules;

import java.util.Random;

public class Enums {

    public enum Suits {
        OROS, BASTOS, ESPADAS, COPAS;

        public static Suits getRandom() {
            Random random = new Random();
            Suits[] suits = values();
            return suits[random.nextInt(suits.length)];
        }

        public static Suits getSuit(String _suit) {
            Suits[] suits = values();
            for (Suits s : suits) {
                if (s.toString().toLowerCase().equals(_suit))
                    return s;
            }
            return null;
        }
    }

    public enum Numbers {
        AS, DOS, TRES, CUATRO, CINCO, SEIS, SIETE, SOTA, CABALLO, REY;

        public static Numbers getRandom() {
            Random random = new Random();
            Numbers[] numbers = values();
            return numbers[random.nextInt(numbers.length)];
        }

        public static Numbers getNumber(String _number) {
            Numbers[] numbers = values();
            for (Numbers n : numbers) {
                if (n.toString().toLowerCase().equals(_number))
                    return n;
            }
            return null;
        }
    }

}
