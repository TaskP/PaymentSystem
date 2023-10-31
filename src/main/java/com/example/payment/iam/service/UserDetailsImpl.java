package com.example.payment.iam.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;

/**
 * Implementation of Spring Security's UserDetails interface, representing user
 * details and authentication information.
 *
 */
@Service
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * Current user.
     */
    private User user;

    /**
     * Default no-argument constructor.
     */
    public UserDetailsImpl() {
        super();
    }

    /**
     *
     * @param userIn to set as current user
     */
    public UserDetailsImpl(final User userIn) {
        this();
        this.user = userIn;
    }

    /**
     * @return Collection of GrantedAuthority based on User role.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user == null) {
            return Collections.emptySet();
        }
        final Set<Role> roles = Role.toRoles(user.getRole());
        final Set<GrantedAuthority> authorities = new HashSet<>();
        for (final Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return authorities;
    }

    /**
     * @return user password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * @return user username
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * @return user status
     */
    @Override
    public boolean isAccountNonExpired() {
        return user.getStatus();
    }

    /**
     * @return user status
     */
    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus();
    }

    /**
     * @return user status
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return user.getStatus();
    }

    /**
     * @return user status
     */
    @Override
    public boolean isEnabled() {
        return user.getStatus();
    }

}
