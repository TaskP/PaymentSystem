package com.example.payment.common.utils;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

public class TaskExecutor extends ScheduledThreadPoolExecutor {

    private static int getExecutorThreads() {
        int cpuCount = 0;
        try {
            cpuCount = Runtime.getRuntime().availableProcessors();
        } catch (final Throwable e) {
        }
        if ((cpuCount / 2) < 1) {
            return 1;
        }
        return (cpuCount / 2);
    }

    public TaskExecutor() {
        this(getExecutorThreads());
    }

    public TaskExecutor(final int corePoolSize) {
        super(corePoolSize);
    }

    public TaskExecutor(final int corePoolSize, final RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    public TaskExecutor(final int corePoolSize, final ThreadFactory threadFactory, final RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    public TaskExecutor(final int corePoolSize, final ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }
}
