package com.example.payment.merchant.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.payment.merchant.factory.TransactionFactory;
import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.model.Transaction;
import com.example.payment.merchant.service.TransactionService;

import jakarta.validation.Valid;

/**
 * RESTful controller responsible for managing transaction-related operations
 * through HTTP end points.
 */

@RestController
@RequestMapping("/api/merchant/transaction")
public class TransactionControllerREST extends CommonMerchantControllerREST {

    /**
     * Logger.
     */
    private static final Log LOG = LogFactory.getLog(TransactionControllerREST.class);

    /**
     * TransactionFactory.
     */
    @Autowired
    private TransactionFactory transactionFactory;

    /**
     * TransactionService.
     */
    @Autowired
    private TransactionService transactionService;

    @Override
    protected Log getLog() {
        return LOG;
    }

    /**
     * @return collection of paginated entities from TransactionService
     */
    @GetMapping
    public List<Transaction> findAll(@RequestParam(defaultValue = "1") final int page, @AuthenticationPrincipal final UserDetails userDetails) {
        final Merchant merchant = getMerchant("FindAll", userDetails);
        final int pageSize = 5;
        final Pageable pageable = PageRequest.of(page - 1, pageSize);
        try {
            final Page<Transaction> paginated = transactionService.findAll(merchant.getId(), pageable);
            return paginated.getContent();
        } catch (final Exception e) {
            throw error("FindAll failed!", e);
        }
    }

    /**
     * Gets/finds transaction by transaction uuid. Method: HTTP GET.
     *
     * @param id
     * @return Optional<Transaction>
     */
    @GetMapping("/{uuid}")
    public Transaction findById(@PathVariable final UUID uuid, @AuthenticationPrincipal final UserDetails userDetails) {
        final Merchant merchant = getMerchant("FindById", userDetails);
        final Optional<Transaction> ret;
        try {
            ret = transactionService.findById(uuid);
        } catch (final Exception e) {
            throw error("FindById failed!", e);
        }

        if (ret == null || !ret.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        if (ret.get().getMerchantId() != merchant.getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return ret.get();
    }

    /**
     * Creates TransactionAuthorize.
     */
    @ResponseStatus(HttpStatus.CREATED) // 201
    @PostMapping("/authorize")
    public Transaction createTransactionAuthorize(@RequestBody @Valid final Transaction transaction, @AuthenticationPrincipal final UserDetails userDetails) {
        final Merchant merchant = getMerchant("CreateTransactionAuthorize", userDetails);
        transaction.setMerchant(merchant);
        transactionFactory.setTransactionIdIfNeeded(transaction);
        try {
            return transactionService.create(transactionFactory.getTransactionAuthorize(transaction));
        } catch (final Exception e) {
            throw error("Create failed!", e);
        }
    }

    /**
     * Creates TransactionCharge.
     */
    @ResponseStatus(HttpStatus.CREATED) // 201
    @PostMapping("/charge")
    public Transaction createTransactionCharge(@RequestBody @Valid final Transaction transaction, @AuthenticationPrincipal final UserDetails userDetails) {
        final Merchant merchant = getMerchant("CreateTransactionCharge", userDetails);
        transaction.setMerchant(merchant);
        transactionFactory.setTransactionIdIfNeeded(transaction);
        try {
            return transactionService.create(transactionFactory.getTransactionCharge(transaction));
        } catch (final Exception e) {
            throw error("Create failed!", e);
        }
    }

    /**
     * Creates TransactionRefund.
     */
    @ResponseStatus(HttpStatus.CREATED) // 201
    @PostMapping("/refund")
    public Transaction createTransactionRefund(@RequestBody @Valid final Transaction transaction, @AuthenticationPrincipal final UserDetails userDetails) {
        final Merchant merchant = getMerchant("CreateTransactionRefund", userDetails);
        transaction.setMerchant(merchant);
        transactionFactory.setTransactionIdIfNeeded(transaction);
        try {
            return transactionService.create(transactionFactory.getTransactionRefund(transaction));
        } catch (final Exception e) {
            throw error("Create failed!", e);
        }
    }

    /**
     * Creates TransactionReversal.
     */
    @ResponseStatus(HttpStatus.CREATED) // 201
    @PostMapping("/reversal")
    public Transaction createTransactionReversal(@RequestBody @Valid final Transaction transaction, @AuthenticationPrincipal final UserDetails userDetails) {
        final Merchant merchant = getMerchant("CreateTransactionReversal", userDetails);
        transaction.setMerchant(merchant);
        transactionFactory.setTransactionIdIfNeeded(transaction);
        try {
            return transactionService.create(transactionFactory.getTransactionReversal(transaction));
        } catch (final Exception e) {
            throw error("Create failed!", e);
        }
    }

}
