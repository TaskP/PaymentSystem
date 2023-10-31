package com.example.payment.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * Collection of static methods for id generation
 *
 */
public class IdUtils {

    // Shift epoch closer to January 1, 1970 UTC in order to have some more space
    // for long time based ids
    private static final long epoch = 1698709109194L;

    private static final AtomicInteger counter = new AtomicInteger((int) (System.currentTimeMillis() / 1000));

    private IdUtils() {
        super();
    }

    /**
     * Generates id based on time stamp plus consecutive counter [0-1000)
     * Uniqueness: up to 1000 ids per millisecond. Total 1M per second.
     *
     * @return
     */
    public static long idLong() {
        return (System.currentTimeMillis() - epoch) * 1000 + (counter.incrementAndGet() / 1000);
    }
}
