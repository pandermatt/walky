package main.java.pipeep.geometry;

/*
 * This class is part of the pipeep.geometry library and was not written by us
 */

/**
 * Provides static utility functions to help work around floating point rounding
 * errors
 */
class Rounding {

    /**
     * A value considered "close enough" to zero, the accepted rounding error.
     */
    private static final double EQUALITY_DIFFERENCE = 1e-7;

    public static boolean isEqual(double a, double b) {
        return Math.abs(a - b) < EQUALITY_DIFFERENCE;
    }

    public static boolean isZero(double a) {
        return isEqual(a, 0.);
    }

    private static boolean isGreaterThan(double a, double b) {
        return a > b && !isEqual(a, b);
    }

    private static boolean isLessThan(double a, double b) {
        return isGreaterThan(b, a);
    }

    public static boolean isLessOrEqual(double a, double b) {
        return !isGreaterThan(a, b);
    }

    public static boolean isGreaterOrEqual(double a, double b) {
        return !isLessThan(a, b);
    }
}
