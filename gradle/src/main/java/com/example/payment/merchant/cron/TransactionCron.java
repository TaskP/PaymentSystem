package com.example.payment.merchant.cron;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.payment.merchant.service.TransactionService;

/**
 * Cleans transactions that are older that one hour.
 */
@Service
public class TransactionCron {

    /**
     * Logger.
     */
    private static final Log LOG = LogFactory.getLog(TransactionCron.class);

    /**
     * TransactionService.
     */
    @Autowired
    private TransactionService transactionService;

    @Scheduled(initialDelay = 30, fixedDelay = 900, timeUnit = TimeUnit.SECONDS)
    public void cleanTransactions() {
        try {
            transactionService.cleanTransactions();
        } catch (final Throwable t) {
            LOG.warn("CleanTransactions Error.", t);
        }
    }
}
