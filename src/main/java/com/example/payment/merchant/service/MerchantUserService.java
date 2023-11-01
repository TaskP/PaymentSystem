package com.example.payment.merchant.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.payment.merchant.model.MerchantUser;
import com.example.payment.merchant.model.MerchantUserKey;
import com.example.payment.merchant.repository.MerchantUserRepository;

import jakarta.validation.Valid;

/**
 * Provides a CRUD services and operations related to MerchantUser.
 */
@Service
public class MerchantUserService {

    /**
     * Autowired MerchantUserRepository.
     */
    @Autowired
    private MerchantUserRepository merchantUserRepository;

    /**
     * List all merchantUsers.
     *
     * @return List<MerchantUser> with all MerchantUsers
     */
    public List<MerchantUser> findAll() {
        return merchantUserRepository.findAll();
    }

    /**
     * Get Merchant by Merchant ID and User ID.
     *
     * @param merchantId
     * @param userId
     * @return Optional<MerchantUser>
     */
    public Optional<MerchantUser> findById(final long merchantId, final long userId) {
        return findById(new MerchantUserKey(merchantId, userId));
    }

    /**
     * Get Merchant by MerchantUserKey.
     *
     * @param id
     * @return Optional<MerchantUser>
     */
    public Optional<MerchantUser> findById(final MerchantUserKey id) {
        return merchantUserRepository.findById(id);
    }

    /**
     * Lists MerchantUser for Merchant ID.
     *
     * @param merchantId
     * @return List<MerchantUser>
     */
    public List<MerchantUser> findByMerchantId(final long merchantId) {
        return merchantUserRepository.findByIdMerchantId(merchantId);
    }

    /**
     * Lists MerchantUser for User ID.
     *
     * @param userId
     * @return Specification<MerchantUser>
     */
    public List<MerchantUser> findByUserId(final long userId) {
        // return
        // merchantUserRepository.findAll(MerchantUserRepository.findByUserId(userId));
        return merchantUserRepository.findByIdUserId(userId);
    }

    /**
     * Persists a MerchantUser.
     *
     * @param merchantUser
     * @return persisted merchantUser
     */
    public MerchantUser save(@Valid final MerchantUser merchantUser) {
        return merchantUserRepository.save(merchantUser);
    }

    /**
     * Deletes Merchant by MerchantUserKey.
     *
     * @param id
     */
    public void deleteById(final MerchantUserKey id) {
        merchantUserRepository.deleteById(id);
    }

    /**
     * Deletes Merchant by Merchant ID and User ID.
     *
     * @param merchantId
     * @param userId
     */
    public void deleteById(final long merchantId, final long userId) {
        deleteById(new MerchantUserKey(merchantId, userId));
    }
}
