package com.iba.iot.datasimulator.common.util;

import java.util.Random;

/**
 *  Apache RandomUtils doesn't work with negative ranges.
 */
public class MathUtil {

    /** **/
    private static Random random = new Random(System.currentTimeMillis());

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public static int randomInt(int min, int max) {
        return random.nextInt(max + 1 - min) + min;
    }

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public static long randomLong(long min, long max) {
        return (long)(random.nextDouble()*(max + 1 - min)) + min;
    }

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public static double randomDouble(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }

    /**
     *
     * @param number
     * @return
     */
    public static int getNumberLength(long number) {
        return (int) Math.log10(number) + 1;
    }
}
