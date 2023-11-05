package com.example.payment.iam.model;

import java.io.Serializable;
import java.util.Set;

import com.example.payment.common.utils.BitUtils;
import com.example.payment.common.utils.StringUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/*
 * The User class represents a user in the system.
 *
 * <ul>
 *   <li>User ID: A unique identifier for each user in the system.</li>
 *   <li>Username: The user's login name. Mandatory and unique. Length must be between 1 to 255</li>
 *   <li>Full Name: Full name of user. Length must be between 0 to 255. Allows <code>null</code>
 *   <li>Password: The user's password hash.Allows <code>null</code>. If not set user is not allowed.</li>
 *   <li>Roles: Bitmask with user's roles</li>
 *   <li>Status: active/inactive (true/false).</li>
 * </ul>
 *
 * Persistence table is users, in plural since in PostgreSQL word user is reserved.
 */
@Entity
@Table(name = "users")
public final class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * User ID. A unique identifier for each user in the system.
     */
    @Id
    private long id;

    /**
     * Username. The user's login name. Unique. Max length is 255
     */
    @Column(nullable = false, unique = true, length = 255)
    @NotNull
    @Size(min = 1, max = 255, message = "Invalid Username, Length must be between 1 to 255")
    private String username;

    /**
     * Full Name: Full name of user. Max length is 255.
     */
    @Column(nullable = true, length = 255)
    @Size(max = 255, message = "Invalid FullName, Length must not be grater than 255")
    private String fullName;

    /**
     * Password. The user's password hash.Allows <code>null</code>. If not set user
     * is not allowed. BCrypt hash length is max 72. Argon2 can be up to 2^32 but
     * default is 64-bytes (512-bits)
     */
    @Column(nullable = true, length = 128)
    @Size(max = 128, message = "Invalid Password, Length must not be grater than 128")
    private String password;

    /**
     * Roles. Bitmask with user's roles
     */
    @Column(nullable = false)
    @RoleValidation
    private long role;

    /**
     * Status: active/inactive (true/false).
     */
    @Column(nullable = false)
    @NotNull
    private boolean status;

    public long getId() {
        return id;
    }

    public User setId(final long idIn) {
        this.id = idIn;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(final String usernameIn) {
        this.username = StringUtils.toLowerCase(usernameIn);
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public User setFullName(final String fullNameIn) {
        this.fullName = fullNameIn;
        return this;
    }

    /**
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param passwordIn
     */
    public User setPassword(final String passwordIn) {
        this.password = passwordIn;
        return this;
    }

    /**
     *
     * @return role
     */
    public long getRole() {
        return role;
    }

    public Set<Role> getRoles() {
        return Role.toRoles(getRole());
    }

    public User setRole(final long roleIn) {
        this.role = roleIn;
        return this;
    }

    public User setRole(final Role roleIn) {
        setRole(roleIn == null ? 0 : roleIn.getValue());
        return this;
    }

    public void setRoles(final Set<Role> roles) {
        if (roles == null || roles.size() == 0) {
            setRole(0L);
            return;
        }
        for (final Role role : roles) {
            setRole(BitUtils.setBit(role.getBitPosition(), getRole()));
        }
    }

    public void setRoles(final String roles) {
        if (roles == null || roles.length() == 0) {
            setRole(0L);
            return;
        }
        final String[] rolesSplit = roles.split(",");
        for (final String role : rolesSplit) {
            final Role roleParsed = Role.parse(role);
            if (roleParsed == null) {
                continue;
            }
            setRole(BitUtils.setBit(roleParsed.getBitPosition(), getRole()));
        }
    }

    /**
     *
     * @return status
     */
    public boolean getStatus() {
        return status;
    }

    public User setStatus(final boolean statusIn) {
        this.status = statusIn;
        return this;
    }

    public void setStatusActive(final String statusIn) {
        setStatus("active".equalsIgnoreCase(statusIn));
    }

    public String getStatusActive() {
        return getStatus() ? "Active" : "Inactive";
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", fullName=" + fullName + ", password=" + password + ", role=" + role + ", status=" + status
                + "]";
    }

}
