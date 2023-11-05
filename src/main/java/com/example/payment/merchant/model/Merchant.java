package com.example.payment.merchant.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.example.payment.common.utils.StringUtils;
import com.example.payment.iam.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Merchant data model.
 */
@Entity
@Table(name = "merchant")
@Transactional
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
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "merchant_id")
    private MerchantSum merchantSum = new MerchantSum(id);

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "merchant_user", joinColumns = { @JoinColumn(name = "merchant_id", referencedColumnName = "id") }, inverseJoinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true) })
    private Set<User> users;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "merchant", cascade = CascadeType.ALL)
    private Set<Transaction> transactions;

    /**
     * @return current merchant Id
     */
    public long getId() {
        return id;
    }

    private void updateId() {
        if (merchantSum == null) {
            merchantSum = new MerchantSum();
        }
        merchantSum.setMerchantId(getId());
    }

    /**
     * @param idIn merchant id to set
     */
    public Merchant setId(final long idIn) {
        this.id = idIn;
        updateId();
        return this;
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

    @Transient
    private transient Set<String> usernamesSet;

    /**
     *
     * @param statusIn
     */
    public void setStatus(final boolean statusIn) {
        this.status = statusIn;
    }

    public void setStatusActive(final String statusIn) {
        setStatus("active".equalsIgnoreCase(statusIn));
    }

    public String getStatusActive() {
        return getStatus() ? "Active" : "Inactive";
    }

    public MerchantSum getMerchantSum() {
        return merchantSum;
    }

    public Merchant addUser(final User user) {
        if (users == null) {
            users = new LinkedHashSet<>();
        }
        users.add(user);
        return this;
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(final Set<User> users) {
        this.users = users;
    }

    public String getUsernames() {
        if (getUsers() == null || getUsers().isEmpty()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (final User user : getUsers()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(user.getUsername());
        }
        return sb.toString();
    }

    public void setUsernames(final String usernames) {
        if (StringUtils.isEmpty(usernames)) {
            this.usernamesSet = new HashSet<>();
            return;
        }
        this.usernamesSet = new HashSet<>();
        Collections.addAll(usernamesSet, StringUtils.toLowerCase(usernames).split(","));

    }

    public Set<String> getUsernamesSet() {
        return usernamesSet;
    }

    public void setUsernamesSet(final Set<String> usernamesSet) {
        this.usernamesSet = usernamesSet;
    }

    @Override
    public String toString() {
        return "Merchant [id=" + id + ", name=" + name + ", description=" + description + ", email=" + email + ", status=" + status + ", " + getMerchantSum()
                + ", users:" + getUsers() + "]";
    }

}
