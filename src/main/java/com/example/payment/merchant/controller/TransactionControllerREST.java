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

import com.example.payment.common.controller.CommonControllerRest;
import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;
import com.example.payment.iam.model.UserDetailsImpl;
import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.model.Transaction;
import com.example.payment.merchant.service.MerchantService;
import com.example.payment.merchant.service.TransactionService;

import jakarta.validation.Valid;

/**
 * RESTful controller responsible for managing user-related operations through
 * HTTP end points.
 */

@RestController
@RequestMapping("/api/merchant/transaction")
public class TransactionControllerREST extends CommonControllerRest {

    private static final Log LOG = LogFactory.getLog(TransactionControllerREST.class);

    /**
     * MerchantService.
     */
    @Autowired
    private MerchantService merchantService;

    /**
     * TransactionService.
     */
    @Autowired
    private TransactionService transactionService;

    @Override
    protected Log getLog() {
        return LOG;
    }

    protected Merchant getMerchant(final UserDetails userDetails) throws ResponseStatusException {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (!(userDetails instanceof UserDetailsImpl)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        final User user = ((UserDetailsImpl) userDetails).getUser();
        if (!Role.MERCHANT.is(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        final Optional<Merchant> ret = merchantService.findByUserId(user.getId());
        if (!ret.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (!ret.get().getStatus()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return ret.get();
    }

    /**
     * @return collection of paginated entities from TransactionService
     */
    @GetMapping
    public List<Transaction> findAll(@RequestParam(defaultValue = "1") final int page, @AuthenticationPrincipal final UserDetails userDetails) {
        final Merchant merchant = getMerchant(userDetails);
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
        final Merchant merchant = getMerchant(userDetails);
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
     * Creates a transaction. Method: HTTP POST. On success returns an HTTP 201
     * (Created) status code. On duplicate transaction error returns HTTP 409
     * Conflict
     *
     * @param transaction
     * @return newly created Transaction
     */
    @ResponseStatus(HttpStatus.CREATED) // 201
    @PostMapping
    public Transaction create(@RequestBody @Valid final Transaction transaction, @AuthenticationPrincipal final UserDetails userDetails) {
        final Merchant merchant = getMerchant(userDetails);
        transaction.setMerchant(merchant);
        try {
            return transactionService.create(transaction);
        } catch (final Exception e) {
            throw error("Create failed!", e);
        }
    }

}
