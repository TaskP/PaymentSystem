package com.example.payment.merchant.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * Transaction data model.
 */
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@DiscriminatorColumn(name = "transaction_type", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue(value = "Z")
@Table(name = "transaction")
public class Transaction implements Serializable {

    public TransactionType getType() {
        return TransactionType.UKNOWN;
    }

    private static final long serialVersionUID = 1L;

    /**
     * Transaction uuid.
     */
    @Id
    // @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    /**
     * Merchant ID.
     */
    @Column(name = "merchant_id", nullable = false, insertable = false, updatable = false)
    private long merchantId;

    /**
     * Merchant.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", referencedColumnName = "id")
    private Merchant merchant;

    /**
     * Transaction Amount.
     */
    @Column(name = "amount", nullable = true)
    @TransactionAmountValidation(allowsNull = true)
    private Double amount;

    /**
     * Transaction Status.
     */
    @Column(nullable = false)
    @TransactionStatusValidation
    private byte status;

    /**
     * Customer Email. Not mandatory. Max length is 256.
     *
     * RFC 3696 states {64}@{255} = 320 but RFC 2821 sets limit in rcpt to 256 hence
     * limit is 256
     *
     * https://www.rfc-editor.org/errata_search.php?rfc=3696&eid=1690
     */
    @Column(name = "customer_email", nullable = true, length = 256)
    @Size(max = 256, message = "Invalid Email. Length must not be grater than 256")
    @Email
    private String customerEmail;

    /**
     * Customer Phone. Not mandatory. Max length is 40.
     */
    @Column(name = "customer_phone", nullable = true, length = 40)
    @Size(max = 40, message = "Invalid Phone. Length must not be grater than 40")
    private String customerPhone;

    /**
     * Reference Transaction ID. Not mandatory.
     */
    @Column(name = "reference_id", nullable = true, insertable = false, updatable = false)
    private UUID referenceId;

    /**
     * Reference Transaction.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_id")
    private Transaction referenceTransaction;

    /**
     * Epoch. Date and time in milliseconds.
     */
    @Column(name = "epoch", nullable = false)
    private long epoch = System.currentTimeMillis();

    /**
     *
     * Get current Transaction uuid.
     *
     * @return uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Set Transaction uuid.
     *
     * @param idIn Transaction uuid to set
     * @return
     */
    public Transaction setUuid(final UUID idIn) {
        this.uuid = idIn;
        return this;
    }

    /**
     *
     * Get current Merchant ID.
     *
     * @return uuid
     */
    public long getMerchantId() {
        return merchantId;
    }

    /**
     * Set Merchant ID.
     *
     * @param merchantIdIn
     */
    public void setMerchantId(final long merchantIdIn) {
        this.merchantId = merchantIdIn;
    }

    /**
     *
     * Get current Merchant.
     *
     * @return Merchant
     */
    public Merchant getMerchant() {
        return merchant;
    }

    /**
     * Set Merchant ID.
     *
     * @param merchantIn
     */
    public void setMerchant(final Merchant merchantIn) {
        this.merchant = merchantIn;
        setMerchantId(merchant == null ? 0L : merchant.getId());
    }

    /**
     *
     * @return amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * @param amountIn
     */
    public void setAmount(final Double amountIn) {
        this.amount = amountIn;
    }

    /**
     *
     * @return status
     */
    public byte getStatus() {
        return status;
    }

    public TransactionStatus getStatusType() {
        return TransactionStatus.parse(getStatus());
    }

    /**
     * @param statusIn
     */
    public void setStatus(final byte statusIn) {
        this.status = statusIn;
    }

    public void setStatusType(final TransactionStatus statusType) {
        setStatus(statusType == null ? 0 : statusType.getStatusId());
    }

    public void setStatusType(final String statusIn) {
        setStatusType(TransactionStatus.parse(statusIn));

    }

    /**
     * @param statusIn
     */
    public void setStatus(final TransactionStatus statusIn) {
        setStatus(statusIn == null ? TransactionStatus.UNKNOWN.getStatusId() : statusIn.getStatusId());
    }

    /**
     *
     * @return CustomerEmail
     */
    public String getCustomerEmail() {
        return customerEmail;
    }

    /**
     *
     * @param customerEmailIn
     */
    public void setCustomerEmail(final String customerEmailIn) {
        this.customerEmail = customerEmailIn;
    }

    /**
     *
     * @return customerPhone
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     *
     * @param customerPhoneIn
     */
    public void setCustomerPhone(final String customerPhoneIn) {
        this.customerPhone = customerPhoneIn;
    }

    /**
     *
     * @return referenceId
     */
    public UUID getReferenceId() {
        return referenceId;
    }

    /**
     *
     * @param referenceIdIn
     */
    public void setReferenceId(final UUID referenceIdIn) {
        this.referenceId = referenceIdIn;
    }

    /**
     *
     * @return Transaction
     */
    public Transaction getReferenceTransaction() {
        return referenceTransaction;
    }

    /**
     *
     * @param referenceTransactionIn
     */
    public void setReferenceTransaction(final Transaction referenceTransactionIn) {
        this.referenceTransaction = referenceTransactionIn;
        setReferenceId(referenceTransactionIn == null ? null : referenceTransactionIn.getUuid());
    }

    /**
     *
     * @return epoch
     */
    public long getEpoch() {
        return epoch;
    }

    /**
     *
     * @param epochIn
     */
    public void setEpoch(final long epochIn) {
        this.epoch = epochIn;
    }

    @Override
    public String toString() {
        return "Transaction [Type=" + getType() + "uuid=" + uuid + ", merchantId=" + merchantId + ", merchant=" + merchant + ", amount=" + amount + ", status="
                + status + ", customerEmail=" + customerEmail + ", customerPhone=" + customerPhone + ", referenceId=" + referenceId + ", referenceTransaction="
                + referenceTransaction + ", epoch=" + epoch + "]";
    }

}
