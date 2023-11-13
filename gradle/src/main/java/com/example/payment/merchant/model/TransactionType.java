package com.example.payment.merchant.model;

/**
 * Transaction Types.
 */
public enum TransactionType {

    /**
     * Uknown. Case when TransactionType can not be determined.
     */
    UKNOWN(0, "Uknown"),
    /**
     * Authorize.
     */
    AUTORIZE(1, "Authorize"),
    /**
     * Charge.
     */
    CHARGE(2, "Charge"),
    /**
     * Refund.
     */
    REFUND(3, "Refund"),
    /**
     * Reversal.
     */
    REVERSAL(4, "Reversal");

    /**
     * Java clones the array when we call values(). Caching it increases the speed.
     */
    private static final TransactionType[] VALUES = TransactionType.values();

    /**
     * @return cached array of values
     */
    public static TransactionType[] getValues() {
        return VALUES;
    }

    /**
     * Type id.
     */
    private final byte typeId;

    /**
     * Type name.
     */
    private final String typeName;

    TransactionType(final int param, final String name) {
        this.typeId = (byte) param;
        this.typeName = name;
    }

    /**
     *
     * @return Type id
     */
    public byte getTypeId() {
        return this.typeId;
    }

    /**
     *
     * @return Type name
     */
    public String getTypeName() {
        return this.typeName;
    }

    /**
     * Parses Type based on id.
     *
     * @param typeId
     * @return TransactionTypeType
     */
    public static TransactionType parse(final Byte typeId) {
        if (typeId == null) {
            return null;
        }

        for (final TransactionType item : getValues()) {
            if (item.getTypeId() == typeId) {
                return item;
            }
        }

        return null;
    }

    /**
     * Parses Type based on typeName or enum name.
     *
     * @param name
     * @return TransactionTypeType
     */
    public static TransactionType parse(final String name) {
        if (name == null) {
            return null;
        }

        for (final TransactionType item : getValues()) {
            if (name.equalsIgnoreCase(item.getTypeName())) {
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
        return this.getTypeName();
    }
}
