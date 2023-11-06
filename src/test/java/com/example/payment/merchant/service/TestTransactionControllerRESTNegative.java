package com.example.payment.merchant.service;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.payment.iam.model.Role;

/**
 * TransactionControllerREST test cases. Negative scenario.
 */
@SpringBootTest(classes = com.example.payment.app.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TestTransactionControllerRESTNegative {

    /**
     * MockMvc bean instance to invoke the APIs.
     */
    @Autowired
    private MockMvc mvc;

    /**
     * Test list users.
     *
     * @throws Exception
     */
    @Test
    public void testListAll() throws Exception {
     //@formatter:off
        mvc.perform(MockMvcRequestBuilders
                .get("/api/merchant/transaction")
                .accept(MediaType.APPLICATION_JSON)
                .with(user(getClass().getSimpleName()).password(getClass().getSimpleName())
                .authorities(new SimpleGrantedAuthority(Role.ADMINISTRATOR.getRoleName()))))
                .andDo(print())
                .andExpect(status().is4xxClientError());
      //@formatter:on
    }
}