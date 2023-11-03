package com.example.payment.merchant.factory;

import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.model.Transaction;
import com.example.payment.merchant.model.TransactionAuthorize;
import com.example.payment.merchant.model.TransactionCharge;
import com.example.payment.merchant.model.TransactionRefund;
import com.example.payment.merchant.model.TransactionReversal;
import com.example.payment.merchant.model.TransactionStatus;
import com.example.payment.merchant.model.TransactionType;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

/**
 * The TransactionFactoryImpl interface implementation.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class TransactionFactoryImpl implements TransactionFactory {

    /**
     * Validator.
     */
    @Autowired
    private Validator validator;

    /**
     * Build new Transaction. Validate and throw exception if not valid.
     *
     * @param type
     * @param uuid
     * @param merchant
     * @param amount
     * @param status
     * @param customerEmail
     * @param customerPhone
     * @param referenceTransaction
     * @return Transaction
     * @throws IllegalArgumentException
     */
    @Override
    public Transaction getTransaction(final TransactionType type, final UUID uuid, final Merchant merchant, final Double amount, final TransactionStatus status,
            final String customerEmail, final String customerPhone, final Transaction referenceTransaction) throws IllegalArgumentException {

        if (type == null) {
            throw new IllegalArgumentException("Invalid TransactionType");
        }
        if (status == null) {
            throw new IllegalArgumentException("Invalid TransactionStatus");
        }
        final Transaction ret;
        switch (type) {
        case AUTORIZE:
            ret = new TransactionAuthorize();
            break;
        case CHARGE:
            ret = new TransactionCharge();
            break;
        case REFUND:
            ret = new TransactionRefund();
            break;
        case REVERSAL:
            ret = new TransactionReversal();
            break;
        default:
            throw new IllegalArgumentException("TransactionType not supported");
        }

        if (uuid != null) {
            ret.setUuid(uuid);
        }
        ret.setMerchant(merchant);
        ret.setAmount(amount);
        ret.setStatus(status);
        ret.setCustomerEmail(customerEmail);
        ret.setCustomerPhone(customerPhone);
        ret.setReferenceTransaction(referenceTransaction);

        final Set<ConstraintViolation<Transaction>> valResult = validator.validate(ret);
        if (valResult != null && valResult.size() != 0) {
            throw new IllegalArgumentException("Validation failed! Error:" + valResult);
        }
        return ret;
    }

}
