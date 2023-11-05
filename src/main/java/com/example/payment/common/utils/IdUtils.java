package com.example.payment.common.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Collection of static methods for id generation.
 *
 */
public final class IdUtils {

    private IdUtils() {
        super();
    }

    /**
     * Epoch Shift in order to have some more space for long time based ids.
     *
     */
    private static final long EPOCH = 1698709109194L;

    /**
     * Level of uniqueness. How many consecutive requests per millisecond will
     * be unique.
     */
    private static final int GRANULARITY = 1000;

    /**
     * Sequential thread-safe counter of generated id.
     */
    private static final AtomicInteger COUNTER = new AtomicInteger(
            (int) (((System.currentTimeMillis() - EPOCH) / GRANULARITY)
                    % GRANULARITY));

    /**
     * Generates id based on time stamp plus consecutive counter. Uniqueness: up
     * to value set in {@link IdUtils#GRANULARITY} per millisecond.
     *
     * @return generated long id
     */
    public static long idLong() {
        return (System.currentTimeMillis() - EPOCH) * GRANULARITY
                + (COUNTER.incrementAndGet() % GRANULARITY);
    }
}
