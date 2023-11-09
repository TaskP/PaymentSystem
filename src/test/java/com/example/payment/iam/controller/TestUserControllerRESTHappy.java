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

import com.example.payment.common.utils.IdUtils;
import com.example.payment.iam.factory.UserFactory;
import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;
import com.example.payment.iam.model.UserDetailsImpl;

/**
 * UserRESTController test cases. Happy path.
 */
@SpringBootTest(classes = com.example.payment.app.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TestUserControllerRESTHappy {

    /**
     * MockMvc bean instance to invoke the APIs.
     */
    @Autowired
    private MockMvc mvc;

    /**
     * UserFactory.
     */
    @Autowired
    private UserFactory userFactory;

    /**
     * Test list users.
     *
     * @throws Exception
     */
    @Test
    public void testListAll() throws Exception {
        final long runId = IdUtils.idLong();
        final String clazzName = getClass().getSimpleName();
        final String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final String fullName = methodName + "-" + runId + "@" + clazzName + ".test";

        final long id = runId;
        final String username = (clazzName + "-" + runId).toLowerCase();
        final User user = userFactory.getUser(id, username, fullName, "pass-" + id, Role.ADMINISTRATOR, true);
        final UserDetailsImpl userDetails = new UserDetailsImpl(user);

        //@formatter:off
        mvc.perform(MockMvcRequestBuilders.get("/api/user").accept(MediaType.APPLICATION_JSON)
                .with(user(userDetails)))
                .andDo(print())
                .andExpect(status().isOk());
        //@formatter:on
    }
}
