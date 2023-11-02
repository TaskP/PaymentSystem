package com.example.payment.common;

/**
 *
 * Collection of static methods for bit manipulation.
 *
 */
public final class BitUtils {

    private BitUtils() {
        super();
    }

    /**
     * Checks if bit on position is set.
     *
     * @param position
     * @param value
     * @return true if bit at position is set
     */
    public static boolean isSet(final int position, final long value) {
        return (value & (1L << position)) == (1L << position);
    }

    /**
     * Sets bit on position.
     *
     * @param position
     * @param value
     * @return value with set bit at position
     */
    public static long setBit(final int position, final long value) {
        return (value | (1L << position));
    }

    /**
     * Clears bit on position.
     *
     * @param position
     * @param value
     * @return value with cleared bit at position
     */
    public static long clearBit(final int position, final long value) {
        return (value & ~(1L << position));
    }
}
