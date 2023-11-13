package com.example.payment.merchant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.payment.iam.TestIamBase;
import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;
import com.example.payment.iam.repository.UserRepository;
import com.example.payment.merchant.factory.MerchantFactory;
import com.example.payment.merchant.factory.TransactionFactory;
import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.model.Transaction;
import com.example.payment.merchant.repository.TransactionRepository;
import com.example.payment.merchant.service.MerchantService;

public abstract class TestMerchantBase extends TestIamBase {

    /**
     * MerchantFactory.
     */
    @Autowired
    private MerchantFactory merchantFactory;

    /**
     * MerchantFactory.
     */
    @Autowired
    private MerchantService merchantService;

    /**
     * UserRepository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * TransactionRepository.
     */
    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * TransactionFactory.
     */
    @Autowired
    private TransactionFactory transactionFactory;

    protected User getUserMerchant() {
        return getUserFactory().getUser(getRunId(), getName(), getName(), Role.MERCHANT);
    }

    protected User getUserMerchant(final String suffix) {
        return getUserFactory().getUser(getRunId(), getName() + "-" + suffix, getName() + "-" + suffix, Role.MERCHANT);
    }

    protected UserDetails getUserDetailsMerchant() {
        return getUserDetails(getUserMerchant());
    }

    protected UserDetails getUserDetailsMerchant(final String suffix) {
        return getUserDetails(getUserMerchant(suffix));
    }

    protected Merchant getMerchant() {
        return getMerchantFactory().getMerchant(getRunId(), getName()).addUser(getUserMerchant());
    }

    protected Merchant getMerchant(final String suffix) {
        return getMerchantFactory().getMerchant(getRunId(), getName() + "-" + suffix).addUser(getUserMerchant(suffix));
    }

    protected Merchant getMerchant(final User user) {
        return getMerchantFactory().getMerchant(user.getId(), user.getUsername()).addUser(user);
    }

    protected Merchant createMerchant() {
        return createMerchant(getMerchant());
    }

    protected Merchant createMerchant(final String suffix) {
        return createMerchant(getMerchant(suffix));
    }

    protected Merchant createMerchant(final User user) {
        return createMerchant(getMerchant(user));
    }

    protected Merchant createMerchant(final Merchant merchant) {
        return getMerchantService().create(merchant);
    }

    protected void deleteMerchantCascade() {
        deleteMerchantCascade(getMerchant());
    }

    protected void deleteMerchantCascade(final String suffix) {
        deleteMerchantCascade(getMerchant(suffix));
    }

    protected void deleteMerchantCascade(final Merchant merchant) {
        final Page<Transaction> transactions = transactionRepository.findByMerchantId(merchant.getId(), Pageable.ofSize(10000000));
        if (transactions != null && transactions.hasContent()) {
            transactionRepository.deleteAll(transactions.getContent());
        }
        merchantService.deleteById(merchant.getId());
        if (merchant.getUsers() != null && merchant.getUsers().size() > 0) {
            userRepository.deleteAll(merchant.getUsers());
        }
    }

    protected MerchantFactory getMerchantFactory() {
        return merchantFactory;
    }

    protected MerchantService getMerchantService() {
        return merchantService;
    }

    protected TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }

}
