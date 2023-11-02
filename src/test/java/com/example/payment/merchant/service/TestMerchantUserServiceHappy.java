package com.example.payment.merchant.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.payment.common.IdUtils;
import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;
import com.example.payment.iam.service.UserService;
import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.model.MerchantUser;

/**
 * MerchantUserService test cases. Happy path.
 */
@SpringBootTest(classes = com.example.payment.app.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestMerchantUserServiceHappy {

    /**
     * MerchantUserService under test.
     */
    @Autowired
    private MerchantUserService merchantUserService;

    /**
     * MerchantService.
     */
    @Autowired
    private MerchantService merchantService;

    /**
     * UserService.
     */
    @Autowired
    private UserService userService;

    /**
     * Test create.
     *
     * Steps:
     *
     * 1. Creates a Merchant
     *
     * 2. Creates a User
     *
     * 3. Creates MerchantUser
     *
     * 4. Fetches MerchantUser by Merchant Id and User Id
     *
     * 5. Lists MerchantUser by Merchant Id
     *
     * 6. Lists MerchantUser by User Id
     *
     * 7. Deletes MerchantUser
     *
     * 8. Deletes Merchant
     *
     * 9. Deletes User
     */
    @Test
    void testCreate() {
        final long runId = IdUtils.idLong();
        final String clazzName = getClass().getSimpleName();
        final String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final String email = methodName + "-" + runId + "@" + clazzName + ".test";

        final long merchantId = runId;
        final String testName = clazzName + "-" + runId;

        // Create Merchant
        Merchant merchant = new Merchant(merchantId, testName, testName + " Description", email, true);
        merchant = this.merchantService.save(merchant);

        // Create User
        final String username = testName;
        final long userId = merchantId + 1;
        User user = new User(userId, username, username + " Lastname", "pass-" + userId, Role.ADMINISTRATOR.getValue(), true);
        user = this.userService.create(user);

        // Create MerchantUser
        final MerchantUser merchantUser = new MerchantUser(merchant, user);
        this.merchantUserService.save(merchantUser);
        Optional<MerchantUser> optMerchantUser = this.merchantUserService.findById(merchantId, userId);
        assertTrue(optMerchantUser.isPresent(), "MerchantUser findById failed");

        List<MerchantUser> listMerchantUser = this.merchantUserService.findByMerchantId(merchantId);
        assertNotNull(listMerchantUser, "List failed #1");
        assertTrue(listMerchantUser.size() == 1, "List failed #2");

        listMerchantUser = this.merchantUserService.findByUserId(userId);
        assertNotNull(listMerchantUser, "List failed #6");
        assertTrue(listMerchantUser.size() == 1, "List failed #7");

        // Delete MerchantUser
        this.merchantUserService.deleteById(merchantId, userId);
        optMerchantUser = this.merchantUserService.findById(merchantId, userId);
        assertFalse(optMerchantUser.isPresent(), "MerchantUser delete failed");

        // Delete Merchant
        this.merchantService.deleteById(merchantId);

        // Delete User
        this.userService.deleteById(userId);
    }

}
