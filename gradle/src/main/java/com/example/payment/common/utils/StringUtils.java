package com.example.payment.common.utils;

/**
 *
 * Collection of static methods for String manipulation.
 *
 */
public final class StringUtils {

    private StringUtils() {
        super();
    }

    public static boolean isEmpty(final CharSequence in) {
        if (in == null) {
            return true;
        }
        return in.isEmpty();
    }

    public static String toLowerCase(final String in) {
        if (in == null || in.isEmpty()) {
            return in;
        }
        return in.toLowerCase();
    }
}
