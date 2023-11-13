package com.example.payment.iam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.example.payment.common.utils.IdUtils;
import com.example.payment.iam.factory.UserFactory;
import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;
import com.example.payment.iam.model.UserDetailsImpl;
import com.example.payment.iam.service.UserService;

import jakarta.validation.Validator;

public abstract class TestIamBase {

    private final Log log = LogFactory.getLog(this.getClass().getSimpleName());

    /**
     * UserService.
     */
    @Autowired
    private UserService userService;

    /**
     * UserFactory.
     */
    @Autowired
    private UserFactory userFactory;

    private final long runId = IdUtils.idLong();

    private final String name = getClass().getSimpleName() + "-" + runId;

    protected Log getLog() {
        return log;
    }

    protected long getRunId() {
        return runId;
    }

    protected String getName() {
        return name;
    }

    protected String getEmail() {
        return getClass().getSimpleName() + "@example.com";
    }

    protected String getEmail(final String prefix) {
        return prefix + "@example.com";
    }

    protected User getUserAdministrator() {
        return getUserFactory().getUser(getRunId(), getName(), getName(), Role.ADMINISTRATOR);
    }

    protected User getUserAdministrator(final String suffix) {
        return getUserFactory().getUser(getRunId(), getName() + "-" + suffix, getName() + "-" + suffix, Role.ADMINISTRATOR);
    }

    protected UserDetails getUserDetailsAdministrator() {
        return getUserDetails(getUserAdministrator());
    }

    protected UserDetails getUserDetailsAdministrator(final String suffix) {
        return getUserDetails(getUserAdministrator(suffix));
    }

    protected UserDetails getUserDetails(final User user) {
        return new UserDetailsImpl(user);
    }

    protected UserFactory getUserFactory() {
        return userFactory;
    }

    protected UserService getUserService() {
        return userService;
    }

    protected User createUser(final String suffix) {
        return createUser(getUserAdministrator(suffix));
    }

    protected User createUser(final User user) {
        return userService.create(user);
    }

    protected void deleteUser() {
        deleteUser(getUserAdministrator());
    }

    protected void deleteUser(final String suffix) {
        deleteUser(getUserAdministrator(suffix));
    }

    protected void deleteUser(final User user) {
        userService.deleteById(user.getId());
    }

    protected Validator getValidator() {
        final LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }
}
