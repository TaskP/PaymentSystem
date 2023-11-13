package com.example.payment.merchant.model;

/**
 * Transaction Status (pending, approved, reversed, refunded, error).
 */
public enum TransactionStatus {
    /**
     * Unknown. Fallback status.
     */
    UNKNOWN(0, "Unknown"),

    /**
     * Pending. First state.
     */
    PENDING(1, "Pending"),

    /**
     * Approved.
     */
    APPROVED(2, "Approved"),

    /**
     * Reversed.
     */
    REVERSED(3, "Reversed"),

    /**
     * Refunded.
     */
    REFUNDED(4, "Refunded"),

    /**
     * Error.
     */
    ERROR(5, "Error");

    /**
     * Java clones the array when we call values(). Caching it increases the speed.
     */
    private static final TransactionStatus[] VALUES = TransactionStatus.values();

    /**
     * @return cached array of values
     */
    public static TransactionStatus[] getValues() {
        return VALUES;
    }

    /**
     * Status id.
     */
    private final byte statusId;

    /**
     * Status name.
     */
    private final String statusName;

    TransactionStatus(final int param, final String typeName) {
        this.statusId = (byte) param;
        this.statusName = typeName;
    }

    /**
     *
     * @return Status id
     */
    public byte getStatusId() {
        return this.statusId;
    }

    /**
     *
     * @return Status name
     */
    public String getStatusName() {
        return this.statusName;
    }

    /**
     * Parses Status based on id.
     *
     * @param statusId
     * @return TransactionStatusType
     */
    public static TransactionStatus parse(final Byte statusId) {
        if (statusId == null) {
            return null;
        }

        for (final TransactionStatus item : getValues()) {
            if (item.getStatusId() == statusId) {
                return item;
            }
        }

        return null;
    }

    /**
     * Parses Status based on statusName or enum name.
     *
     * @param name
     * @return TransactionStatusType
     */
    public static TransactionStatus parse(final String name) {
        if (name == null) {
            return null;
        }

        for (final TransactionStatus item : getValues()) {
            if (name.equalsIgnoreCase(item.getStatusName())) {
                return item;
            }
            if (name.equalsIgnoreCase(item.name())) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.getStatusName();
    }
}
