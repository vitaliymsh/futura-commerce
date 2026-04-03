package com.futura.commerce.security.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security UserDetailsService bridge for admin identities
 *
 * @author Vitalii
 */
@Service
public class AdminUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Will wire to JPA repository once admin entities are introduced
        throw new UsernameNotFoundException("User not found or disabled: " + username);
    }
}
