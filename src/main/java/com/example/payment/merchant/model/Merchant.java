package com.example.payment.merchant.model;

import com.example.payment.utils.IdUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Merchant data model.
 */
@Entity
@Table(name = "merchant")
public class Merchant {
    /**
     * User ID. A unique identifier for each merchant in the system.
     */
    @Id
    @Column(unique = true)
    @NotNull
    private Long id;

    /**
     * Merchant name. Unique. Mandatory. Length must be between 1 to 255
     */
    @Column(nullable = false, unique = true, length = 255)
    @NotNull
    @Size(min = 1, max = 255, message = "Invalid Name. Length must be between 1 to 255")
    private String name;

    /**
     * Merchant description. Not mandatory. Max length is 1024.
     */
    @Column(nullable = true, length = 1024)
    @Size(max = 1024, message = "Invalid Description. Length must not be grater than 1024")
    private String description;

    /**
     * Merchant email. Not mandatory. Max length is 256.
     *
     * RFC 3696 states {64}@{255} = 320 but RFC 2821 sets limit in rcpt to 256 hence
     * limit is 256
     *
     * https://www.rfc-editor.org/errata_search.php?rfc=3696&eid=1690
     */
    @Column(nullable = true, length = 256)
    @Size(max = 256, message = "Invalid Email. Length must not be grater than 256")
    @Email
    private String email;

    /**
     * Status: active/inactive (true/false).
     */
    @Column(nullable = false)
    @NotNull
    private boolean status;

    /**
     * Default no-arg constructor.
     */
    public Merchant() {
        this(IdUtils.idLong());
    }

    /**
     *
     * @param idIn merchant id to set
     */
    public Merchant(final Long idIn) {
        super();
        this.setId(idIn);
    }

    /**
     * All fields constructor.
     *
     * @param idIn
     * @param nameIn
     * @param descriptionIn
     * @param emailIn
     * @param statusIn
     */
    public Merchant(final Long idIn, final String nameIn, final String descriptionIn, final String emailIn, final boolean statusIn) {
        this(idIn);
        this.setName(nameIn);
        this.setDescription(descriptionIn);
        this.setEmail(emailIn);
        this.setStatus(statusIn);
    }

    /**
     * @return current merchant Id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param idIn merchant id to set
     */
    public void setId(final Long idIn) {
        this.id = idIn;
    }

    /**
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * @param nameIn name to set
     */
    public void setName(final String nameIn) {
        this.name = nameIn;
    }

    /**
     *
     * @return Full Name
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param descriptionIn
     */
    public void setDescription(final String descriptionIn) {
        this.description = descriptionIn;
    }

    /**
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param emailIn
     */
    public void setEmail(final String emailIn) {
        this.email = emailIn;
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
}
