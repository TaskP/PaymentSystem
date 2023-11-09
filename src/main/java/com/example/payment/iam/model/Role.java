package com.example.payment.iam.model;

/**
 * Roles.
 */
public enum Role {
    /**
     * Administrator Role.
     */
    ADMINISTRATOR(1, "Administrator"),
    /**
     * Merchant Role.
     */
    MERCHANT(2, "Merchant");

    /**
     * Java clones the array when we call values(). Caching it increases the speed.
     */
    private static final Role[] VALUES = Role.values();

    /**
     * @return cached array of values
     */
    public static Role[] getValues() {
        return VALUES;
    }

    /**
     * Role Id.
     */
    private final byte value;

    /**
     * Role name.
     */
    private final String roleName;

    Role(final int param, final String typeName) {
        this.value = (byte) param;
        this.roleName = typeName;
    }

    /**
     *
     * @return Role value
     */
    public byte getValue() {
        return this.value;
    }

    /**
     *
     * @return Role name
     */
    public String getRoleName() {
        return this.roleName;
    }

    /**
     * Parses Role based on value.
     *
     * @param bitPos
     * @return Role if found
     */
    public static Role parse(final Byte value) {
        if (value == null) {
            return null;
        }

        for (final Role item : getValues()) {
            if (item.getValue() == value) {
                return item;
            }
        }

        return null;
    }

    /**
     * Parses Role based on roleName or enum name.
     *
     * @param name Role name to parse
     * @return Role if found
     */
    public static Role parse(final String name) {
        if (name == null) {
            return null;
        }

        for (final Role item : getValues()) {
            if (name.equalsIgnoreCase(item.getRoleName())) {
                return item;
            }
            if (name.equalsIgnoreCase(item.name())) {
                return item;
            }
        }
        return null;
    }

    /**
     * Validates Role by value. Checks if there is a role with that value.
     *
     * @param value
     * @return true if a Role is found
     */
    public static boolean isValid(final Byte value) {
        return parse(value) != null;
    }

    @Override
    public String toString() {
        return this.getRoleName();
    }
}
