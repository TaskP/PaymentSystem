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
     * List all merchants.
     *
     * @return List<Merchant> with all merchants
     */
    public List<Merchant> findAll() {
        return merchantRepository.findAll();
    }

    /**
     * Get merchant by merchant id.
     *
     * @param id
     * @return Optional<Merchant>
     */
    public Optional<Merchant> findById(final Long id) {
        return merchantRepository.findById(id);
    }

    /**
     * Persists a merchant.
     *
     * @param merchant
     * @return persisted merchant
     */
    public Merchant save(@Valid final Merchant merchant) {
        return merchantRepository.save(merchant);
    }

    /**
     * Deletes merchant by merchant id.
     *
     * @param id
     */
    public void deleteById(final Long id) {
        merchantRepository.deleteById(id);
    }

    /**
     * Get merchant by name.
     *
     * @param name
     * @return Optional<Merchant>
     */
    public Optional<Merchant> findByMerchantname(final String name) {
        return merchantRepository.findByName(name);
    }
}
