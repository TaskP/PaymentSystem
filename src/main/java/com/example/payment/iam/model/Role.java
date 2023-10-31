package com.example.payment.iam.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.example.payment.utils.BitUtils;

/**
 * Roles as a bitmask. Supports to 64 Roles in long.
 */
public enum Role {
    /**
     * Administrator Role. Bit position 1. Value 2.
     */
    ADMINISTRATOR(1, "Administrator"),
    /**
     * Merchant Role. Bit position 2. Value 4.
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
     * Bit position of Role.
     */
    private final byte bitPosition;

    /**
     * Role name.
     */
    private final String roleName;

    Role(final int param, final String typeName) {
        this.bitPosition = (byte) param;
        this.roleName = typeName;
    }

    /**
     *
     * @return bit Role position
     */
    public byte getBitPosition() {
        return this.bitPosition;
    }

    /**
     *
     * @return Role name
     */
    public String getRoleName() {
        return this.roleName;
    }

    /**
     * Parses Role based on bit position.
     *
     * @param bitPos
     * @return Role if found
     */
    public static Role parse(final Byte bitPos) {
        if (bitPos == null) {
            return null;
        }

        for (final Role item : getValues()) {
            if (item.getBitPosition() == bitPos) {
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
     * Validates Role by bit position. Checks if there is a role with that
     * bitPosition.
     *
     * @param bitPosition
     * @return true if a Role is found
     */
    public static boolean isValid(final Byte bitPosition) {
        if (parse(bitPosition) != null) {
            return true;
        }
        return false;
    }

    /**
     * @return long value of Role
     */
    public long getValue() {
        if (this.getBitPosition() <= 0) {
            return 0L;
        }

        return BitUtils.setBit(this.getBitPosition(), 0L);
    }

    /**
     * Builds a Set<Roles> based on roles.
     *
     * @param roles as long
     * @return Set<Roles> Will not return <code>null</code>.
     */
    public static Set<Role> toRoles(final Long roles) {
        if (roles == null || roles == 0) {
            return Collections.emptySet();
        }
        final HashSet<Role> ret = new HashSet<>();
        for (final Role item : getValues()) {
            if (BitUtils.isSet(item.getBitPosition(), roles)) {
                ret.add(item);
                continue;
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        return this.getRoleName();
    }
}
