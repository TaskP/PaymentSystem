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
import jakarta.validation.ValidationException;
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

    @Override
    public ValidationException getValidationException(final Transaction transaction) {
        if (transaction == null) {
            return new ValidationException("Transaction is null");
        }
        if (transaction.getUuid() == null) {
            return new ValidationException("Invalid UUID");
        }
        final Set<ConstraintViolation<Transaction>> valResult = validator.validate(transaction);
        if (valResult != null && valResult.size() != 0) {
            return new ValidationException("Validation failed! Error:" + valResult);
        }
        switch (transaction.getType()) {
        case AUTORIZE:
            if (transaction.getAmount() == null) {
                return new ValidationException("Validation failed! Amount can not be null.");
            }
            if (transaction.getReferenceTransaction() != null) {
                return new ValidationException("Validation failed! ReferenceTransaction must be null.");
            }
            if (transaction.getReferenceId() != null) {
                return new ValidationException("Validation failed! ReferenceId must be null.");
            }
            return null;
        case CHARGE:
            if (transaction.getAmount() == null) {
                return new ValidationException("Validation failed! Amount can not be null.");
            }
            return null;
        case REFUND:
            if (transaction.getAmount() == null) {
                return new ValidationException("Validation failed! Amount can not be null.");
            }
            if (transaction.getReferenceId() == null) {
                return new ValidationException("Validation failed! ReferenceId must be set.");
            }
            return null;
        case REVERSAL:
            if (transaction.getAmount() != null) {
                return new ValidationException("Validation failed! Amount must be null.");
            }
            if (transaction.getReferenceId() == null) {
                return new ValidationException("Validation failed! ReferenceId must be set.");
            }
            return null;
        default:
            return new ValidationException("TransactionType not supported");
        }
    }

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
     * @throws ValidationException
     */
    @Override
    public Transaction getTransaction(final TransactionType type, final UUID uuid, final Merchant merchant, final Double amount, final TransactionStatus status,
            final String customerEmail, final String customerPhone, final UUID referenceId, final Transaction referenceTransaction) throws ValidationException {

        if (type == null) {
            throw new ValidationException("Invalid TransactionType");
        }
        if (status == null) {
            throw new ValidationException("Invalid TransactionStatus");
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
            throw new ValidationException("TransactionType not supported");
        }

        ret.setUuid(uuid);
        ret.setMerchant(merchant);
        ret.setAmount(amount);
        ret.setStatus(status);
        ret.setCustomerEmail(customerEmail);
        ret.setCustomerPhone(customerPhone);
        if (referenceTransaction != null) {
            ret.setReferenceTransaction(referenceTransaction);
        } else {
            ret.setReferenceTransaction(null);
            ret.setReferenceId(referenceId);
        }

        final ValidationException ex = getValidationException(ret);
        if (ex != null) {
            throw ex;
        }
        return ret;
    }

}
