package com.example.payment.iam.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;
import com.example.payment.iam.model.UserDetailsImpl;
import com.example.payment.iam.repository.UserRepository;

/**
 * Service implementation responsible for loading user details during
 * authentication in Spring Security.
 *
 */
@Service
public final class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * User repository.
     */
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Optional<User> user = userRepo.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User (" + username + ") does not exist");
        }
        if (!user.get().getStatus()) {
            throw new UsernameNotFoundException("User (" + username + ") is not active");
        }
        if (user.get().getPassword() == null || user.get().getPassword().isEmpty()) {
            throw new UsernameNotFoundException("User (" + username + ") does not have password set");
        }

        final Set<Role> roles = Role.toRoles(user.get().getRole());
        if (roles.isEmpty()) {
            throw new UsernameNotFoundException("User (" + username + ") does not have any role assigned");
        }
        return new UserDetailsImpl(user.get());
    }

}
