package com.example.payment.merchant.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.payment.merchant.model.MerchantUser;
import com.example.payment.merchant.model.MerchantUserKey;

/**
 * The MerchantUserRepository interface extends the Spring Data JPA
 * JpaRepository, providing related data access methods. Inherits CRUD (Create,
 * Read, Update, Delete) operations for Merchant entity
 */
@Repository
public interface MerchantUserRepository extends JpaRepository<MerchantUser, MerchantUserKey>, JpaSpecificationExecutor<MerchantUser> {

    /**
     * Lists MerchantUser for Merchant ID.
     *
     * @param merchantId
     * @return List<MerchantUser>
     */
    List<MerchantUser> findByIdMerchantId(Long merchantId);

    /**
     * Lists MerchantUser for User ID.
     *
     * @param userId
     * @return List<MerchantUser>
     */
    List<MerchantUser> findByIdUserId(Long userId);

    /**
     * Lists MerchantUser for User ID Using specification interface.
     *
     * @param userId
     * @return Specification<MerchantUser>
     */
    static Specification<MerchantUser> findByUserId(final Long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id").get("userId"), userId);
    }
}
