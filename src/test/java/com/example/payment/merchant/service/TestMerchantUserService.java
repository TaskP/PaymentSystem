package com.example.payment.merchant.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;
import com.example.payment.iam.service.UserService;
import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.model.MerchantUser;
import com.example.payment.utils.IdUtils;

/**
 * MerchantService test cases.
 */
@SpringBootTest(classes = com.example.payment.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestMerchantUserService {

    /**
     * MerchantService under test.
     */
    @Autowired
    private MerchantUserService merchantUserService;

    /**
     * MerchantService under test.
     */
    @Autowired
    private MerchantService merchantService;

    /**
     * MerchantService under test.
     */
    @Autowired
    private UserService userService;

    /**
     * Test create. Happy path.
     */
    @Test
    void testCreateHappy() {
        final long merchantId = IdUtils.idLong();
        final String name = "testCreateHappy-" + merchantId;

        // Create Merchant
        Merchant merchant = new Merchant(merchantId, name, name + " Description", "example@example.com", true);
        merchant = this.merchantService.save(merchant);
        Optional<Merchant> optMerchant = this.merchantService.findById(merchantId);
        assertTrue(optMerchant.isPresent(), "Merchant findById failed");
        assertThat(optMerchant.get().getName()).isEqualTo(name);

        optMerchant = this.merchantService.findByName(name);
        assertTrue(optMerchant.isPresent(), "Merchant findByName failed");
        assertThat(optMerchant.get().getName()).isEqualTo(name);

        // Create User
        final String username = name;
        final long userId = merchantId + 1;
        User user = new User(userId, username, username + " Lastname", "pass-" + userId, Role.ADMINISTRATOR.getValue(), true);
        user = this.userService.create(user);
        Optional<User> optUser = this.userService.findById(userId);
        assertTrue(optUser.isPresent(), "User findById failed");
        assertThat(optUser.get().getUsername()).isEqualTo(username);

        optUser = this.userService.findByUsername(username);
        assertTrue(optUser.isPresent(), "User findByUsername failed");
        assertThat(optUser.get().getUsername()).isEqualTo(username);

        // Create MerchantUser
        final MerchantUser merchantUser = new MerchantUser(merchant, user);
        this.merchantUserService.save(merchantUser);
        Optional<MerchantUser> optMerchantUser = this.merchantUserService.findById(merchantId, userId);
        assertTrue(optMerchantUser.isPresent(), "MerchantUser findById failed");

        List<MerchantUser> listMerchantUser = this.merchantUserService.findByMerchantId(merchantId);
        assertNotNull(listMerchantUser);
        assertTrue(listMerchantUser.size() == 1);

        listMerchantUser = this.merchantUserService.findByUserId(userId);
        assertNotNull(listMerchantUser);
        assertTrue(listMerchantUser.size() == 1);

        // Delete MerchantUser
        this.merchantUserService.deleteById(merchantId, userId);
        optMerchantUser = this.merchantUserService.findById(merchantId, userId);
        assertFalse(optMerchantUser.isPresent(), "MerchantUser delete failed");

        // Delete Merchant
        this.merchantService.deleteById(merchantId);
        optMerchant = this.merchantService.findById(merchantId);
        assertFalse(optMerchant.isPresent(), "Merchant delete failed");

        // Delete User
        this.userService.deleteById(userId);
        optUser = this.userService.findById(userId);
        assertFalse(optUser.isPresent(), "User delete failed");
    }

}
