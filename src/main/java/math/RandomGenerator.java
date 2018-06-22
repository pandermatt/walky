package main.java.math;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class can generate random numbers, booleans and colors
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class RandomGenerator {

    /**
     * Create a random number inside a range
     *
     * @param from the minimalValue
     * @param to the maximalValue
     * @return generated random number
     */
    public static int randomNumber(int from, int to) {
        return ThreadLocalRandom.current().nextInt(from, to + 1);
    }

    /**
     * Returns a random boolean-value
     */
    public static boolean randomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    /**
     * Returns a random color
     */
    public static Color randomColor() {
        return new Color(randomRGBValue(), randomRGBValue(), randomRGBValue());
    }

    /**
     * Returns a random color that is never dark. At least 1 RGB-Value is > 150
     */
    public static Color randomBrightColor() {

        //which rgb value should be bright
        int bright = randomNumber(1, 3);
        return new Color(
                bright == 1 ? randomBrightValue() : randomRGBValue(),
                bright == 2 ? randomBrightValue() : randomRGBValue(),
                bright == 3 ? randomBrightValue() : randomRGBValue());
    }

    /**
     * Returns a random number between 0 and 255
     */
    private static int randomRGBValue() {
        return randomNumber(0, 255);
    }

    /**
     * Returns a random number between 155 and 255
     */
    private static int randomBrightValue() {
        return randomNumber(150, 255);
    }
}
