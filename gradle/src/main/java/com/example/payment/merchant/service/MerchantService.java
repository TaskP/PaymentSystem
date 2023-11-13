package com.example.payment.merchant.service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.payment.iam.factory.UserFactory;
import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;
import com.example.payment.iam.service.UserService;
import com.example.payment.merchant.factory.MerchantFactory;
import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.repository.MerchantRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;

/**
 * Provides a CRUD services and operations related to Merchant.
 */
@Service
public class MerchantService {

    /**
     * Autowired MerchantRepository.
     */
    @Autowired
    private MerchantRepository merchantRepository;

    /**
     * MerchantFactory.
     */
    @Autowired
    private MerchantFactory merchantFactory;

    /**
     * UserFactory.
     */
    @Autowired
    private UserFactory userFactory;

    /**
     * UserService.
     */
    @Autowired
    private UserService userService;

    /**
     * TransactionService.
     */
    @Autowired
    private TransactionService transactionService;

    /**
     * Strip user password.
     *
     * @param merchant
     * @return merchant
     */
    protected Merchant formatOut(final Merchant merchant) {
        if (merchant != null && merchant.getUsers() != null) {
            for (final User user : merchant.getUsers()) {
                user.setPassword(null);
            }
        }
        return merchant;
    }

    /**
     * Strip user password.
     *
     * @param user optional
     * @return Optional<User>
     */
    protected Optional<Merchant> formatOut(final Optional<Merchant> merchant) {
        if (merchant != null && merchant.isPresent()) {
            formatOut(merchant.get());
        }
        return merchant;
    }

    /**
     * Strip user password.
     *
     * @param merchants list
     * @return List<Merchant>
     */
    protected List<Merchant> formatOut(final List<Merchant> merchants) {
        if (merchants != null && !merchants.isEmpty()) {
            for (final Merchant item : merchants) {
                formatOut(item);
            }
        }
        return merchants;
    }

    /**
     * Strip user password.
     *
     * @param merchants
     * @return Page<Merchant>
     */
    protected Page<Merchant> formatOut(final Page<Merchant> merchants) {
        if (merchants != null && !merchants.isEmpty()) {
            for (final Merchant item : merchants) {
                formatOut(item);
            }
        }
        return merchants;
    }

    /**
     * List all Merchants.
     *
     * @return List<Merchant> with all Merchants
     */
    public List<Merchant> findAll() {
        return formatOut(merchantRepository.findAll());
    }

    /**
     * Get Merchant by Merchant ID.
     *
     * @param id
     * @return Optional<Merchant>
     */
    public Optional<Merchant> findById(final long id) {
        return formatOut(merchantRepository.findById(id));
    }

    public Optional<Merchant> findByUserId(final long id) {
        return formatOut(merchantRepository.findByUsersId(id));
    }

    public Optional<Merchant> findByUserIdUnformatted(final long id) {
        return merchantRepository.findByUsersId(id);
    }

    private Merchant processUsernameSet(final Merchant merchant) {
        if (merchant == null || merchant.getUsernamesSet() == null) {
            return merchant;
        }

        if (merchant.getUsernamesSet().isEmpty()) {
            if (merchant.getUsers() != null) {
                merchant.getUsernamesSet().clear();
            }
            return merchant;
        }
        if (merchant.getUsers() == null) {
            merchant.setUsers(new LinkedHashSet<>());
        }

        final Set<User> toProcess = new HashSet<>();
        for (final User user : merchant.getUsers()) {
            if (!merchant.getUsernamesSet().contains(user.getUsername())) {
                toProcess.add(user);
            }
        }
        if (!toProcess.isEmpty()) {
            merchant.getUsers().removeAll(toProcess);
            toProcess.clear();
        }

        for (final String username : merchant.getUsernamesSet()) {
            boolean match = false;
            for (final User user : merchant.getUsers()) {
                if (username.equalsIgnoreCase(user.getUsername())) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                final Optional<User> user = userService.findByUsername(username);
                if (user.isPresent()) {
                    toProcess.add(user.get());
                } else {
                    toProcess.add(userFactory.getUser(username, merchant.getName(), username, Role.MERCHANT));
                }
            }
        }
        if (!toProcess.isEmpty()) {
            merchant.getUsers().addAll(toProcess);
            toProcess.clear();
        }
        merchant.setUsernamesSet(null);
        return merchant;
    }

    /**
     * Create a Merchant.
     *
     * @param merchant
     * @return persisted Merchant
     */
    public Merchant create(final Merchant merchant) {
        final ValidationException vex = merchantFactory.getValidationException(merchantFactory.setMerchantIdIfNeeded(merchant));
        if (vex != null) {
            throw vex;
        }
        Optional<Merchant> ex = findById(merchant.getId());
        if (ex.isPresent()) {
            throw new EntityExistsException("Merchant exists! Id:" + merchant.getId());
        }
        ex = findByName(merchant.getName());
        if (ex.isPresent()) {
            throw new EntityExistsException("Merchant exists! Name:" + merchant.getName());
        }
        processUsernameSet(merchant);
        if (merchant.getMerchantSum() == null) {
            merchant.setMerchantSum(0);
        }
        return formatOut(merchantRepository.save(merchant));
    }

    public Merchant update(final Merchant merchant) {
        Optional<Merchant> ex = findById(merchant.getId());
        if (!ex.isPresent()) {
            throw new EntityNotFoundException("Merchant not found! Id:" + merchant.getId());
        }
        merchant.setUsers(ex.get().getUsers());
        ex = findByName(merchant.getName());
        if (ex.isPresent() && ex.get().getId() != merchant.getId()) {
            throw new EntityExistsException("Merchant exists! Name:" + merchant.getName());
        }
        processUsernameSet(merchant);
        return formatOut(merchantRepository.save(merchant));
    }

    /**
     * Deletes Merchant by Merchant ID.
     *
     * @param id
     */
    public void deleteById(final long id) {
        final Optional<Merchant> ex = findById(id);
        if (!ex.isPresent()) {
            throw new EntityNotFoundException("Merchant not found! Id:" + id);
        }
        final long transactionsCount = transactionService.count(id);
        if (transactionsCount > 0) {
            throw new DataIntegrityViolationException("Merchant has transactions! Id:" + id + " Count:" + transactionsCount);
        }
        merchantRepository.deleteById(id);
    }

    /**
     * Get Merchant by name.
     *
     * @param name
     * @return Optional<Merchant>
     */
    public Optional<Merchant> findByName(final String name) {
        return formatOut(merchantRepository.findByName(name));
    }

    public Page<Merchant> findByName(final String name, final Pageable pageable) {
        return formatOut(merchantRepository.findByName(name, pageable));
    }

}
