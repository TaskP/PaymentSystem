package com.example.payment.iam.model;

import java.io.Serializable;

import com.example.payment.utils.IdUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(unique = true)
    @NotNull
    private Long id = IdUtils.idLong();

    @Column(nullable = false, unique = true, length = 255)
    @NotNull
    @Size(min = 1, max = 255, message = "Invalid Username, Length should be between 1 to 255")
    private String username;

    @Column(nullable = true, length = 255)
    @Size(max = 255, message = "Invalid FullName, Length should be between 0 to 255")
    private String fullName;

    // BCrypt hash length is max 72.
    // Argon2 can be up to 2^32 but default is 64-bytes (512-bits)
    @Column(nullable = true, length = 128)
    @Size(max = 128, message = "Invalid Password, Length should not be grater than 128")
    private String password;

    @Column(nullable = false)
    @RoleValidation
    private long role;

    @Column(nullable = false)
    @NotNull
    private boolean status;

    public User() {
        super();
    }

    public User(final Long id, final String username, final String fullName, final String password, final long role, final boolean status) {
        this();
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public long getRole() {
        return role;
    }

    public void setRole(final long role) {
        this.role = role;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(final boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", fullName=" + fullName + ", password=" + password + ", role=" + role + ", status=" + status + "]";
    }

}
