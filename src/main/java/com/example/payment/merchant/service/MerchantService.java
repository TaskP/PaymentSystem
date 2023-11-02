package com.example.payment.merchant.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.repository.MerchantRepository;

import jakarta.validation.Valid;

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
     * List all Merchants.
     *
     * @return List<Merchant> with all Merchants
     */
    public List<Merchant> findAll() {
        return merchantRepository.findAll();
    }

    /**
     * Get Merchant by Merchant ID.
     *
     * @param id
     * @return Optional<Merchant>
     */
    public Optional<Merchant> findById(final long id) {
        return merchantRepository.findById(id);
    }

    /**
     * Persists a Merchant.
     *
     * @param merchant
     * @return persisted Merchant
     */
    public Merchant save(@Valid final Merchant merchant) {
        return merchantRepository.save(merchant);
    }

    /**
     * Deletes Merchant by Merchant ID.
     *
     * @param id
     */
    public void deleteById(final long id) {
        merchantRepository.deleteById(id);
    }

    /**
     * Get Merchant by name.
     *
     * @param name
     * @return Optional<Merchant>
     */
    public Optional<Merchant> findByName(final String name) {
        return merchantRepository.findByName(name);
    }
}
