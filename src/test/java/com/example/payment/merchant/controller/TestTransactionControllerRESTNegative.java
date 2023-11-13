package com.example.payment.merchant.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.payment.common.utils.IdUtils;
import com.example.payment.iam.model.User;
import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.model.TransactionAuthorize;
import com.example.payment.merchant.model.TransactionCharge;
import com.example.payment.merchant.model.TransactionRefund;
import com.example.payment.merchant.model.TransactionReversal;

/**
 * TransactionControllerREST test cases. Negative.
 *
 * ./gradlew test --tests "TestTransactionControllerRESTNegative"
 *
 */
@SpringBootTest(classes = com.example.payment.app.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TestTransactionControllerRESTNegative extends TestTransactionControllerRESTBase {

    /**
     * List Merchant transactions without valid credentials.
     *
     */
    @Test
    public void testListAll() throws Exception {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        //@formatter:off
        getMvc().perform(MockMvcRequestBuilders
                      .get("/api/merchant/transaction")
                      .accept(MediaType.APPLICATION_JSON)
                      .with(user(getUserDetailsAdministrator(methodName)))
                  )
                 .andDo(print())
                 .andExpect(status().is4xxClientError());
        //@formatter:on
    }

    /**
     * Checks Reversal for a different merchant Authorize.
     *
     * @throws Exception
     */

    @Test
    public void testTrasnactionAuthorizeReversalDifferentMerchant() throws Exception {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final User user = getUserMerchant(methodName);
        final Merchant merchant = createMerchant(user);
        final UserDetails userDetails = getUserDetails(user);

        //Create TransactionAuthorize
        final UUID uuidAuthorize = getTransactionFactory().getId();
        final double sumAuthorize = (System.currentTimeMillis() / 2953);
        final TransactionAuthorize transactionAuthorize = getTransactionFactory().getTransactionAuthorize(uuidAuthorize, merchant, sumAuthorize,
                getEmail(methodName));
        createAndCheck(userDetails, transactionAuthorize);

        final User user2 = getUserMerchant(methodName + "-2").setId(IdUtils.idLong());
        final Merchant merchant2 = createMerchant(getMerchant(user2).setId(user2.getId()));
        final UserDetails userDetails2 = getUserDetails(user2);
        //Create TransactionReversal
        final UUID uuidReversal = getTransactionFactory().getId();
        final TransactionReversal transactionReversal = getTransactionFactory().getTransactionReversal(uuidReversal, merchant2, transactionAuthorize);

        final AssertionError exception = assertThrows(AssertionError.class, () -> createAndCheck(userDetails2, transactionReversal));
        assertNotNull(exception);

        deleteMerchantCascade(merchant);
        deleteMerchantCascade(merchant2);
    }

    /**
     * Checks Reversal for already reversed Authorize.
     *
     * @throws Exception
     */

    @Test
    public void testTrasnactionAuthorizeDoubleReversal() throws Exception {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final User user = getUserMerchant(methodName);
        final Merchant merchant = createMerchant(user);
        final UserDetails userDetails = getUserDetails(user);

        //Create TransactionAuthorize
        final UUID uuidAuthorize = getTransactionFactory().getId();
        final double sumAuthorize = (System.currentTimeMillis() / 2953);
        final TransactionAuthorize transactionAuthorize = getTransactionFactory().getTransactionAuthorize(uuidAuthorize, merchant, sumAuthorize,
                getEmail(methodName));
        createAndCheck(userDetails, transactionAuthorize);

        final UUID uuidReversal = getTransactionFactory().getId();
        final TransactionReversal transactionReversal = getTransactionFactory().getTransactionReversal(uuidReversal, merchant, transactionAuthorize);
        createAndCheck(userDetails, transactionReversal);

        final UUID uuidReversal2 = getTransactionFactory().getId();
        final TransactionReversal transactionReversal2 = getTransactionFactory().getTransactionReversal(uuidReversal2, merchant, transactionAuthorize);

        final AssertionError exception = assertThrows(AssertionError.class, () -> createAndCheck(userDetails, transactionReversal2));
        assertNotNull(exception);

        deleteMerchantCascade(merchant);
    }

    /**
     * Checks Charge for a different merchant Authorize.
     *
     * @throws Exception
     */

    @Test
    public void testTrasnactionAuthorizeChargeDifferentMerchant() throws Exception {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final User user = getUserMerchant(methodName);
        final Merchant merchant = createMerchant(user);
        final UserDetails userDetails = getUserDetails(user);

        //Create TransactionAuthorize
        final UUID uuidAuthorize = getTransactionFactory().getId();
        final double sumAuthorize = (System.currentTimeMillis() / 2953);
        final TransactionAuthorize transactionAuthorize = getTransactionFactory().getTransactionAuthorize(uuidAuthorize, merchant, sumAuthorize,
                getEmail(methodName));
        createAndCheck(userDetails, transactionAuthorize);

        final User user2 = getUserMerchant(methodName + "-2").setId(IdUtils.idLong());
        final Merchant merchant2 = createMerchant(getMerchant(user2).setId(user2.getId()));
        final UserDetails userDetails2 = getUserDetails(user2);
        //Create TransactionCharge
        final UUID uuidCharge = getTransactionFactory().getId();
        final TransactionCharge transactionCharge = getTransactionFactory().getTransactionCharge(uuidCharge, merchant2, sumAuthorize, transactionAuthorize);

        final AssertionError exception = assertThrows(AssertionError.class, () -> createAndCheck(userDetails2, transactionCharge));
        assertNotNull(exception);

        deleteMerchantCascade(merchant);
        deleteMerchantCascade(merchant2);
    }

    /**
     * Checks Charge for already charged Authorize.
     *
     * @throws Exception
     */

    @Test
    public void testTrasnactionAuthorizeDoubleCharge() throws Exception {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final User user = getUserMerchant(methodName);
        final Merchant merchant = createMerchant(user);
        final UserDetails userDetails = getUserDetails(user);

        //Create TransactionAuthorize
        final UUID uuidAuthorize = getTransactionFactory().getId();
        final double sumAuthorize = (System.currentTimeMillis() / 2953);
        final TransactionAuthorize transactionAuthorize = getTransactionFactory().getTransactionAuthorize(uuidAuthorize, merchant, sumAuthorize,
                getEmail(methodName));
        createAndCheck(userDetails, transactionAuthorize);

        final UUID uuidCharge = getTransactionFactory().getId();
        final TransactionCharge transactionCharge = getTransactionFactory().getTransactionCharge(uuidCharge, merchant, sumAuthorize, transactionAuthorize);
        createAndCheck(userDetails, transactionCharge);

        final UUID uuidCharge2 = getTransactionFactory().getId();
        final TransactionCharge transactionCharge2 = getTransactionFactory().getTransactionCharge(uuidCharge2, merchant, sumAuthorize, transactionAuthorize);

        final AssertionError exception = assertThrows(AssertionError.class, () -> createAndCheck(userDetails, transactionCharge2));
        assertNotNull(exception);

        deleteMerchantCascade(merchant);
    }

    /**
     * Checks Refund for already refunded Charge.
     *
     * @throws Exception
     */

    @Test
    public void testTrasnactionChargeDoubleRefund() throws Exception {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final User user = getUserMerchant(methodName);
        final Merchant merchant = createMerchant(user);
        final UserDetails userDetails = getUserDetails(user);

        //Create TransactionCharge
        final UUID uuidCharge = getTransactionFactory().getId();
        final double sumCharge = (System.currentTimeMillis() / 2953);
        final TransactionCharge transactionCharge = getTransactionFactory().getTransactionCharge(uuidCharge, merchant, sumCharge, getEmail(methodName));
        createAndCheck(userDetails, transactionCharge);

        final UUID uuidRefund = getTransactionFactory().getId();
        final TransactionRefund transactionRefund = getTransactionFactory().getTransactionRefund(uuidRefund, merchant, transactionCharge.getAmount(),
                transactionCharge);
        createAndCheck(userDetails, transactionRefund);

        final UUID uuidRefund2 = getTransactionFactory().getId();
        final TransactionRefund transactionRefund2 = getTransactionFactory().getTransactionRefund(uuidRefund2, merchant, transactionCharge.getAmount(),
                transactionCharge);

        final AssertionError exception = assertThrows(AssertionError.class, () -> createAndCheck(userDetails, transactionRefund2));
        assertNotNull(exception);

        deleteMerchantCascade(merchant);
    }

}
