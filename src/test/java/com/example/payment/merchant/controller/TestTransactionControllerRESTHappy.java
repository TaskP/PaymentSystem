package com.example.payment.merchant.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.payment.iam.model.User;
import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.model.TransactionAuthorize;
import com.example.payment.merchant.model.TransactionCharge;
import com.example.payment.merchant.model.TransactionRefund;
import com.example.payment.merchant.model.TransactionReversal;

/**
 * TransactionControllerREST test cases. Happy path.
 *
 * ./gradlew test --tests "TestTransactionControllerRESTHappy"
 */
@SpringBootTest(classes = com.example.payment.app.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TestTransactionControllerRESTHappy extends TestTransactionControllerRESTBase {

    /**
     * List Merchant transactions.
     *
     * Steps:
     *
     * 1. Create a Merchant
     *
     * 2. List Merchant transactions. List must be empty.
     *
     * 3. Delete Merchant cascade
     */
    @Test
    public void testListAll() throws Exception {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final User user = getUserMerchant(methodName);
        final Merchant merchant = createMerchant(user);
        final UserDetails userDetails = getUserDetails(user);

        //@formatter:off
        getMvc().perform(MockMvcRequestBuilders
                      .get("/api/merchant/transaction")
                      .accept(MediaType.APPLICATION_JSON)
                      .with(user(userDetails))
                  )
                 .andDo(print())
                 .andExpect(status().is2xxSuccessful())
                 .andExpect(content().string("[]"));
        //@formatter:on

        deleteMerchantCascade(merchant);
    }

    /**
     * TrasnactionAuthorize.
     *
     * Steps:
     *
     * 1. Create a Merchant
     *
     * 2. Create TrasnactionAuthorize
     *
     * 3. List Merchant Transactions and check for transaction uuid
     *
     * 4. Get Transaction by UUID and check for transaction uuid
     *
     * 5. Check MerchantSum is 0
     *
     * 6. Delete Merchant cascade
     */
    @Test
    public void testTrasnactionAuthorize() throws Exception {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final User user = getUserMerchant(methodName);
        final Merchant merchant = createMerchant(user);
        final UserDetails userDetails = getUserDetails(user);

        final double sum = (System.currentTimeMillis() / 3461);
        final UUID uuid = getTransactionFactory().getId();

        //Create TransactionAuthorize
        final TransactionAuthorize transactionAuthorize = getTransactionFactory().getTransactionAuthorize(uuid, merchant, sum, getEmail(methodName));

        createAndCheck(userDetails, transactionAuthorize);

        final Optional<Merchant> optMerchant = getMerchantService().findById(merchant.getId());
        assertTrue(optMerchant.isPresent(), "findById failed #1");
        assertTrue(merchant.getName().equals(optMerchant.get().getName()), "findById failed #2");
        assertTrue(optMerchant.get().getMerchantSum() != null, "MerchantSum is null");
        assertTrue(optMerchant.get().getMerchantSum().getTotalTransactionSum() == 0D, "MerchantSum TotalTransactionSum is not zero");

        deleteMerchantCascade(merchant);
    }

    /**
     * Authorize->Charge.
     *
     * Steps:
     *
     * 1. Create a Merchant
     *
     * 2. Create TransactionAuthorize
     *
     * 3. List Merchant Transactions and check for TransactionAuthorize UUID
     *
     * 4. Get Transaction by UUID and check for transaction TransactionAuthorize
     * UUID
     *
     * 5. Create TrasnactionCharge with amount less than amount in
     * TransactionAuthorize
     *
     * 6. List Merchant Transactions and check for TrasnactionCharge UUID
     *
     * 7. Get Transaction by TrasnactionCharge UUID and check for transaction
     * TransactionAuthorize UUID and TrasnactionCharge UUID
     *
     * 8. Check MerchantSum is equal to amount in TrasnactionCharge transaction
     *
     * 9. Delete Merchant cascade
     */
    @Test
    public void testTrasnactionAuthorizeCharge() throws Exception {
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

        //Create TransactionCharge
        final double sumCharge = sumAuthorize / 2;
        final UUID uuidCharge = getTransactionFactory().getId();
        final TransactionCharge transactionCharge = getTransactionFactory().getTransactionCharge(uuidCharge, merchant, sumCharge, transactionAuthorize);

        createAndCheck(userDetails, transactionCharge);
        final Optional<Merchant> optMerchant = getMerchantService().findById(merchant.getId());
        assertTrue(optMerchant.isPresent(), "findById failed #1");
        assertTrue(merchant.getName().equals(optMerchant.get().getName()), "findById failed #2");
        assertTrue(optMerchant.get().getMerchantSum() != null, "MerchantSum is null");
        assertTrue(optMerchant.get().getMerchantSum().getTotalTransactionSum() == sumCharge, "MerchantSum TotalTransactionSum differ");

        deleteMerchantCascade(merchant);
    }

    /**
     * Authorize->Charge->Refund.
     *
     * Steps:
     *
     * 1. Create a Merchant
     *
     * 2. Create TransactionAuthorize
     *
     * 3. List Merchant Transactions and check for TransactionAuthorize UUID
     *
     * 4. Get Transaction by UUID and check for transaction TransactionAuthorize
     * UUID
     *
     * 5. Create TrasnactionCharge with amount less than amount in
     * TransactionAuthorize
     *
     * 6. List Merchant Transactions and check for TrasnactionCharge UUID
     *
     * 7. Get Transaction by TrasnactionCharge UUID and check for transaction
     * TransactionAuthorize UUID and TrasnactionCharge UUID
     *
     * 8. Check MerchantSum is equal to amount in TrasnactionCharge transaction
     *
     * 9. Create TrasnactionRefund with amount less than amount in TrasnactionCharge
     *
     * 10. List Merchant Transactions and check for TrasnactionRefund UUID
     *
     * 11. Get Transaction by TrasnactionCharge UUID and check for transaction
     * TrasnactionCharge UUID and TrasnactionRefund UUID
     *
     * 12. Check MerchantSum is equal to amount in TrasnactionCharge minus amount in
     * TransactionRefund transaction
     *
     * 13. Delete Merchant cascade
     */
    @Test
    public void testTrasnactionAuthorizeChargeRefund() throws Exception {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final User user = getUserMerchant(methodName);
        final Merchant merchant = createMerchant(user);
        final UserDetails userDetails = getUserDetails(user);

        //TransactionAuthorize
        final UUID uuidAuthorize = getTransactionFactory().getId();
        final double sumAuthorize = (System.currentTimeMillis() / 2927);

        final TransactionAuthorize transactionAuthorize = getTransactionFactory().getTransactionAuthorize(uuidAuthorize, merchant, sumAuthorize,
                getEmail(methodName));
        createAndCheck(userDetails, transactionAuthorize);

        //TransactionCharge
        final UUID uuidCharge = getTransactionFactory().getId();
        final double sumCharge = sumAuthorize / 2;

        final TransactionCharge transactionCharge = getTransactionFactory().getTransactionCharge(uuidCharge, merchant, sumCharge, transactionAuthorize);
        createAndCheck(userDetails, transactionCharge);
        Optional<Merchant> optMerchant = getMerchantService().findById(merchant.getId());
        assertTrue(optMerchant.isPresent(), "findById failed #1");
        assertTrue(merchant.getName().equals(optMerchant.get().getName()), "findById failed #2");
        assertTrue(optMerchant.get().getMerchantSum() != null, "MerchantSum is null");
        assertTrue(optMerchant.get().getMerchantSum().getTotalTransactionSum() == sumCharge, "MerchantSum TotalTransactionSum differ");

        //TransactionRefund
        final UUID uuidRefund = getTransactionFactory().getId();
        final double sumRefund = sumCharge / 2;

        final TransactionRefund transactionRefund = getTransactionFactory().getTransactionRefund(uuidRefund, merchant, sumRefund, transactionCharge);
        createAndCheck(userDetails, transactionRefund);
        optMerchant = getMerchantService().findById(merchant.getId());
        assertTrue(optMerchant.isPresent(), "findById failed #1");
        assertTrue(merchant.getName().equals(optMerchant.get().getName()), "findById failed #2");
        assertTrue(optMerchant.get().getMerchantSum() != null, "MerchantSum is null");
        assertTrue(optMerchant.get().getMerchantSum().getTotalTransactionSum() == (sumCharge - sumRefund), "MerchantSum TotalTransactionSum differ");

        deleteMerchantCascade(merchant);
    }

    /**
     * Authorize->Reversal.
     *
     * Steps:
     *
     * 1. Create a Merchant
     *
     * 2. Create TransactionAuthorize
     *
     * 3. List Merchant Transactions and check for TransactionAuthorize UUID
     *
     * 4. Get Transaction by UUID and check for transaction TransactionAuthorize
     * UUID
     *
     * 5. Create TrasnactionReversal
     *
     * 6. List Merchant Transactions and check for TrasnactionReversal UUID
     *
     * 7. Get Transaction by TrasnactionReversal UUID and check for transaction
     * TransactionAuthorize UUID and TrasnactionReversal UUID
     *
     * 8. Check MerchantSum has not changed
     *
     * 9. Delete Merchant cascade
     */
    @Test
    public void testTrasnactionAuthorizeReversal() throws Exception {
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

        //Create TransactionReversal
        final UUID uuidReversal = getTransactionFactory().getId();
        final TransactionReversal transactionReversal = getTransactionFactory().getTransactionReversal(uuidReversal, merchant, transactionAuthorize);

        createAndCheck(userDetails, transactionReversal);
        final Optional<Merchant> optMerchant = getMerchantService().findById(merchant.getId());
        assertTrue(optMerchant.isPresent(), "findById failed #1");
        assertTrue(merchant.getName().equals(optMerchant.get().getName()), "findById failed #2");
        assertTrue(optMerchant.get().getMerchantSum() != null, "MerchantSum is null");
        assertTrue(optMerchant.get().getMerchantSum().getTotalTransactionSum() == 0, "MerchantSum TotalTransactionSum differ");

        deleteMerchantCascade(merchant);
    }

    /**
     * TrasnactionCharge.
     *
     * Steps:
     *
     * 1. Create a Merchant
     *
     * 2. Create TrasnactionCharge
     *
     * 3. List Merchant Transactions and check for TrasnactionCharge UUID
     *
     * 4. Get Transaction by UUID and check for transaction TrasnactionCharge UUID
     *
     * 5. Check MerchantSum is equal to amount in transaction
     *
     * 6. Delete Merchant cascade
     */
    @Test
    public void testTrasnactionCharge() throws Exception {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final User user = getUserMerchant(methodName);
        final Merchant merchant = createMerchant(user);
        final UserDetails userDetails = getUserDetails(user);

        final UUID uuid = getTransactionFactory().getId();
        final double sumCharge = (System.currentTimeMillis() / 2);
        final TransactionCharge transactionCharge = getTransactionFactory().getTransactionCharge(uuid, merchant, sumCharge, getEmail(methodName));
        createAndCheck(userDetails, transactionCharge);

        final Optional<Merchant> optMerchant = getMerchantService().findById(merchant.getId());
        assertTrue(optMerchant.isPresent(), "findById failed #1");
        assertTrue(merchant.getName().equals(optMerchant.get().getName()), "findById failed #2");
        assertTrue(optMerchant.get().getMerchantSum() != null, "MerchantSum is null");
        assertTrue(optMerchant.get().getMerchantSum().getTotalTransactionSum() == sumCharge, "MerchantSum TotalTransactionSum differ");

        deleteMerchantCascade(merchant);
    }

    /**
     * TrasnactionCharge.
     *
     * Steps:
     *
     * 1. Create a Merchant
     *
     * 2. Create TrasnactionCharge
     *
     * 3. List Merchant Transactions and check for TrasnactionCharge UUID
     *
     * 4. Get Transaction by UUID and check for transaction TrasnactionCharge UUID
     *
     * 5. Check MerchantSum is equal to amount in transaction
     *
     * 6. Create TrasnactionRefund with amount less than amount in TrasnactionCharge
     *
     * 7. List Merchant Transactions and check for TrasnactionRefund UUID
     *
     * 8. Get Transaction by TrasnactionCharge UUID and check for transaction
     * TrasnactionCharge UUID and TrasnactionRefund UUID
     *
     * 9. Check MerchantSum is equal to amount in TrasnactionCharge minus amount in
     * TransactionRefund transaction
     *
     * 10. Delete Merchant cascade
     */
    @Test
    public void testTrasnactionChargeRefund() throws Exception {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final User user = getUserMerchant(methodName);
        final Merchant merchant = createMerchant(user);
        final UserDetails userDetails = getUserDetails(user);

        final UUID uuid = getTransactionFactory().getId();
        final double sumCharge = (System.currentTimeMillis() / 2);
        final TransactionCharge transactionCharge = getTransactionFactory().getTransactionCharge(uuid, merchant, sumCharge, getEmail(methodName));
        createAndCheck(userDetails, transactionCharge);

        Optional<Merchant> optMerchant = getMerchantService().findById(merchant.getId());
        assertTrue(optMerchant.isPresent(), "findById failed #1");
        assertTrue(merchant.getName().equals(optMerchant.get().getName()), "findById failed #2");
        assertTrue(optMerchant.get().getMerchantSum() != null, "MerchantSum is null");
        assertTrue(optMerchant.get().getMerchantSum().getTotalTransactionSum() == sumCharge, "MerchantSum TotalTransactionSum differ");

        //TransactionRefund
        final UUID uuidRefund = getTransactionFactory().getId();
        final double sumRefund = sumCharge / 2;

        final TransactionRefund transactionRefund = getTransactionFactory().getTransactionRefund(uuidRefund, merchant, sumRefund, transactionCharge);
        createAndCheck(userDetails, transactionRefund);
        optMerchant = getMerchantService().findById(merchant.getId());
        assertTrue(optMerchant.isPresent(), "findById failed #1");
        assertTrue(merchant.getName().equals(optMerchant.get().getName()), "findById failed #2");
        assertTrue(optMerchant.get().getMerchantSum() != null, "MerchantSum is null");
        assertTrue(optMerchant.get().getMerchantSum().getTotalTransactionSum() == (sumCharge - sumRefund), "MerchantSum TotalTransactionSum differ");

        deleteMerchantCascade(merchant);
    }
}
