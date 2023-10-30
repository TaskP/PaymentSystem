package com.example.payment.iam.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;

@Service
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private User user;

    public UserDetailsImpl() {
        super();
    }

    public UserDetailsImpl(final User user) {
        this();
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final Set<Role> roles = Role.toRoles(user.getRole());
        final Set<GrantedAuthority> authorities = new HashSet<>();
        for (final Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getTypeName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isStatus();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isStatus();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isStatus();
    }

    @Override
    public boolean isEnabled() {
        return user.isStatus();
    }

}
