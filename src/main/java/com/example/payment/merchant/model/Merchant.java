package com.example.payment.merchant.model;

import java.io.Serializable;

import com.example.payment.common.IdUtils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Merchant data model.
 */
@Entity
@Table(name = "merchant")
public final class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Merchant ID. A unique identifier for each merchant in the system.
     */
    @Id
    @Column
    private long id;

    /**
     * Merchant name. Unique. Mandatory. Length must be between 1 to 255
     */
    @Column(nullable = false, unique = true, length = 255)
    @Size(min = 1, max = 255, message = "Invalid Name. Length must be between 1 to 255")
    @NotNull
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
    private boolean status;

    /**
     * Total transaction sum. Modeled in external entity in order to avoid locking
     * on update.
     */
    @OneToOne(mappedBy = "merchant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private MerchantSum merchantSum;

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
    public Merchant(final long idIn) {
        super();
        this.setId(idIn);
        // this.setMerchantSum(0);
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
    public Merchant(final long idIn, final String nameIn, final String descriptionIn, final String emailIn, final boolean statusIn) {
        this(idIn);
        this.setName(nameIn);
        this.setDescription(descriptionIn);
        this.setEmail(emailIn);
        this.setStatus(statusIn);
    }

    /**
     * @return current merchant Id
     */
    public long getId() {
        return id;
    }

    /**
     * @param idIn merchant id to set
     */
    public void setId(final long idIn) {
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

    /**
     *
     * @return MerchantSum
     */
    public MerchantSum getMerchantSum() {
        return merchantSum;
    }

    /**
     *
     * @param merchantSumIn
     */
    public void setMerchantSum(final double merchantSumIn) {
        if (this.merchantSum == null) {
            this.merchantSum = new MerchantSum(getId(), merchantSumIn);
        } else {
            this.merchantSum.setTotalTransactionSum(merchantSumIn);
        }
    }

    @Override
    public String toString() {
        return "Merchant [id=" + id + ", name=" + name + ", description=" + description + ", email=" + email + ", status=" + status + "]";
    }

}
