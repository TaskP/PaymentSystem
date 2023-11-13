package com.example.payment.iam.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.payment.iam.TestIamBase;
import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;

/**
 * UserRESTController test cases. Negative.
 *
 * ./gradlew test --tests "TestUserControllerRESTNegative"
 *
 */
@SpringBootTest(classes = com.example.payment.app.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TestUserControllerRESTNegative extends TestIamBase {

    /**
     * MockMvc bean instance to invoke the APIs.
     */
    @Autowired
    private MockMvc mvc;

    /**
     * List Uses.
     *
     * Steps:
     *
     * 1. Create a User with a Merchant Role
     *
     * 2. List Users. Should receive access denied.
     *
     * 3. Delete User
     */
    @Test
    public void testListAll() throws Exception {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on
        final User user = getUserAdministrator(methodName);
        user.setRole(Role.MERCHANT);
        createUser(user);

        //@formatter:off
        mvc.perform(MockMvcRequestBuilders
                      .get("/api/user")
                      .accept(MediaType.APPLICATION_JSON)
                      .with(user(getUserDetails(user)))
                  )
                 .andDo(print())
                 .andExpect(status().is4xxClientError());
        //@formatter:on

        deleteUser(user);
    }
}
