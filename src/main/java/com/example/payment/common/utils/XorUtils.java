package com.example.payment.common.utils;

/**
 *
 * Collection of static methods for XOR manipulation.
 *
 */
public final class XorUtils {

    private XorUtils() {
        super();
    }

    /**
     * XOR two Long. If value1 is null value2 is returned and vice versa.
     *
     * @param value1
     * @param value2
     * @return Long result of value1^value2
     */
    public static Long xor(final Long value1, final Long value2) {
        if (value1 == null) {
            return value2;
        }
        if (value2 == null) {
            return value1;
        }
        return (value1 ^ value2);
    }
}
