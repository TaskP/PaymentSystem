package com.example.payment.merchant.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.payment.common.utils.JsonUtils;
import com.example.payment.merchant.TestMerchantBase;
import com.example.payment.merchant.factory.TransactionFactory;
import com.example.payment.merchant.model.Transaction;

abstract class TestTransactionControllerRESTBase extends TestMerchantBase {

    /**
     * TransactionFactory.
     */
    @Autowired
    private TransactionFactory transactionFactory;

    /**
     * MockMvc bean instance to invoke the APIs.
     */
    @Autowired
    private MockMvc mvc;

    protected MockMvc getMvc() {
        return mvc;
    }

    protected Transaction createAndCheck(final UserDetails userDetails, final Transaction transaction) throws Exception {
        final String transactionChargeJson = JsonUtils.asJsonString(getTransactionFactory().formatOut(transaction));
        //@formatter:off
        getMvc().perform(MockMvcRequestBuilders
                      .post("/api/merchant/transaction")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(transactionChargeJson)
                      .accept(MediaType.APPLICATION_JSON)
                      .with(user(userDetails))
                  )
                 .andDo(print())
                 .andExpect(status().is2xxSuccessful());

        getMvc().perform(MockMvcRequestBuilders
                      .get("/api/merchant/transaction")
                      .accept(MediaType.APPLICATION_JSON)
                      .with(user(userDetails))
                  )
                 .andDo(print())
                 .andExpect(status().is2xxSuccessful())
                 .andExpect(content().string(containsString(transaction.getUuid().toString())));
        //@formatter:on
        if (transaction.getReferenceId() != null) {
            getMvc().perform(MockMvcRequestBuilders.get("/api/merchant/transaction/" + transaction.getUuid().toString()).accept(MediaType.APPLICATION_JSON)
                    .with(user(userDetails))).andDo(print()).andExpect(status().is2xxSuccessful())
                    .andExpect(content().string(containsString(transaction.getReferenceId().toString())))
                    .andExpect(content().string(containsString(transaction.getUuid().toString())));
        } else {
            getMvc().perform(MockMvcRequestBuilders.get("/api/merchant/transaction/" + transaction.getUuid().toString()).accept(MediaType.APPLICATION_JSON)
                    .with(user(userDetails))).andDo(print()).andExpect(status().is2xxSuccessful())
                    .andExpect(content().string(containsString(transaction.getUuid().toString())));
        }

        return transaction;
    }

    protected TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }

    protected void setTransactionFactory(final TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    protected void setMvc(final MockMvc mvc) {
        this.mvc = mvc;
    }

}
