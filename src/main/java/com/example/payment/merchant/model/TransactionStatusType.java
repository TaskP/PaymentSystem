package com.example.payment.merchant.model;

/**
 * Transaction Status (approved, reversed, refunded, error).
 */
public enum TransactionStatusType {
    /**
     * Unknown. Fallback status.
     */
    UNKNOWN(0, "Unknown"),

    /**
     * Approved.
     */
    APPROVED(1, "Approved"),
    /**
     * Reversed.
     */
    REVERSED(2, "Reversed"),
    /**
     * Refunded.
     */
    REFUNDED(3, "Refunded"),
    /**
     * Error.
     */
    ERROR(4, "Error");

    /**
     * Java clones the array when we call values(). Caching it increases the speed.
     */
    private static final TransactionStatusType[] VALUES = TransactionStatusType.values();

    /**
     * @return cached array of values
     */
    public static TransactionStatusType[] getValues() {
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

    TransactionStatusType(final int param, final String typeName) {
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
    public static TransactionStatusType parse(final Byte statusId) {
        if (statusId == null) {
            return null;
        }

        for (final TransactionStatusType item : getValues()) {
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
    public static TransactionStatusType parse(final String name) {
        if (name == null) {
            return null;
        }

        for (final TransactionStatusType item : getValues()) {
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
