package com.example.payment.iam.model;

import java.util.HashSet;
import java.util.Set;

import com.example.payment.utils.BitUtils;

/**
 * Predefined User Roles Using a Bitmask. Up to 64 Roles in long
 */
public enum Role {
    USER(0, "User"), // 1
    ADMINISTRATOR(1, "Administrator"), // 2
    MERCHANT(2, "Merchant") // 4
    ;

    // Java clones the array when we call values(). Caching it increases the speed.
    private final static Role[] VALUES = Role.values();

    public static Role[] getValues() {
        return VALUES;
    }

    private final byte bitPosition;

    private final String typeName;

    Role(final int param, final String typeName) {
        this.bitPosition = (byte) param;
        this.typeName = typeName;
    }

    public byte getBitPosition() {
        return this.bitPosition;
    }

    public String getTypeName() {
        return this.typeName;
    }

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

    public static Role parse(final String name) {
        if (name == null) {
            return null;
        }

        for (final Role item : getValues()) {
            if (name.equalsIgnoreCase(item.typeName)) {
                return item;
            }
        }
        return null;
    }

    public static boolean isValid(final Byte param) {
        if (parse(param) != null) {
            return true;
        }
        return false;
    }

    public long getValue() {
        if (this.getBitPosition() <= 0) {
            return 0L;
        }

        return BitUtils.setBit(this.getBitPosition(), 0L);
    }

    public static Set<Role> toRoles(final long roles) {
        final HashSet<Role> ret = new HashSet<>();
        if (roles == 0) {
            return ret;
        }
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
        return this.getTypeName();
    }
}
