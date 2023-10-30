package com.example.payment.utils;

/**
 *
 * Collection of static methods for bit manipulation
 *
 */
public class BitUtils {

    private BitUtils() {
        super();
    }

    /**
     * Checks if bit on position is set
     *
     * @param position
     * @param l
     * @return
     */
    public static boolean isSet(final int position, final long l) {
        return (l & (1L << position)) == (1L << position);
    }

    /**
     * Sets bit on position
     *
     * @param position
     * @param l
     * @return
     */
    public static long setBit(final int position, final long l) {
        return (l | (1L << position));
    }

    /**
     * Clears bit on position
     *
     * @param position
     * @param l
     * @return
     */
    public static long clearBit(final int position, final long l) {
        return (l & ~(1L << position));
    }
}
