package openroguelike;

import java.util.Random;

public class RandomNumberGenerator {
    public static RandomNumberGenerator RANDOM = new RandomNumberGenerator();

    private final Random random = new Random();

    public int getRandom(int bound) {
        return Math.abs(nextInt()) % bound;
    }

    private int nextInt() {
        return random.nextInt();
    }

}
