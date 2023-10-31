package com.example.payment.iam.model;

import java.io.Serializable;

import com.example.payment.utils.IdUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/*
 * The User class represents a user in the system.
 *
 * <p>Key attributes:
 * <ul>
 *   <li>User ID: A unique identifier for each user in the system.</li>
 *   <li>Username: The user's login name. Mandatory and unique. Length must be between 1 to 255</li>
 *   <li>Full Name: Full name of user. Length must be between 0 to 255. Allows <code>null</code>
 *   <li>Password: The user's password hash.Allows <code>null</code>. If not set user is not allowed.</li>
 *   <li>Roles: Bitmask with user's roles</li>
 *   <li>Status: active/inactive (true/false).</li>
 * </ul>
 */
@Entity
@Table(name = "user")
public final class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * User ID. A unique identifier for each user in the system.
     */
    @Id
    @Column(unique = true)
    @NotNull
    private Long id;

    /**
     * Username. The user's login name. Unique. Length must be between 1 to 255
     */
    @Column(nullable = false, unique = true, length = 255)
    @NotNull
    @Size(min = 1, max = 255, message = "Invalid Username, Length must be between 1 to 255")
    private String username;

    /**
     * Full Name: Full name of user. Length must be between 0 to 255.
     */
    @Column(nullable = true, length = 255)
    @Size(max = 255, message = "Invalid FullName, Length must be between 0 to 255")
    private String fullName;

    /**
     * Password. The user's password hash.Allows <code>null</code>. If not set user
     * is not allowed. BCrypt hash length is max 72. Argon2 can be up to 2^32 but
     * default is 64-bytes (512-bits)
     */
    @Column(nullable = true, length = 128)
    @Size(max = 128, message = "Invalid Password, Length should not be grater than 128")
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

    /**
     * Default no-arg constructor.
     */
    public User() {
        this(IdUtils.idLong());
    }

    /**
     *
     * @param idIn user id to set
     */
    public User(final Long idIn) {
        super();
        this.setId(idIn);
    }

    /**
     * All fields constructor.
     *
     * @param idIn
     * @param usernameIn
     * @param fullNameIn
     * @param passwordIn
     * @param roleIn
     * @param statusIn
     */
    public User(final Long idIn, final String usernameIn, final String fullNameIn, final String passwordIn, final long roleIn, final boolean statusIn) {
        this(idIn);
        this.setUsername(usernameIn);
        this.setFullName(fullNameIn);
        this.setPassword(passwordIn);
        this.setRole(roleIn);
        this.setStatus(statusIn);
    }

    /**
     * @return current user Id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param idIn user id to set
     */
    public void setId(final Long idIn) {
        this.id = idIn;
    }

    /**
     * @return Username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param usernameIn username to set
     */
    public void setUsername(final String usernameIn) {
        this.username = usernameIn;
    }

    /**
     *
     * @return Full Name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     *
     * @param fullNameIn
     */
    public void setFullName(final String fullNameIn) {
        this.fullName = fullNameIn;
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
    public void setPassword(final String passwordIn) {
        this.password = passwordIn;
    }

    /**
     *
     * @return role
     */
    public long getRole() {
        return role;
    }

    /**
     *
     * @param roleIn
     */
    public void setRole(final long roleIn) {
        this.role = roleIn;
    }

    /**
     *
     * @return status
     */
    public boolean getStatus() {
        return status;
    }

    /**
     *
     * @param statusIn
     */
    public void setStatus(final boolean statusIn) {
        this.status = statusIn;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", fullName=" + fullName + ", password=" + password + ", role=" + role + ", status=" + status
                + "]";
    }

}
