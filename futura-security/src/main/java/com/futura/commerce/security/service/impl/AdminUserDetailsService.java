package com.futura.commerce.security.service.impl;

import com.futura.commerce.mbg.model.UmsAdmin;
import com.futura.commerce.mbg.repository.UmsAdminRepository;
import jakarta.annotation.Resource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security UserDetailsService bridge for admin identities
 *
 * @author Vitalii
 */
@Service
public class AdminUserDetailsService implements UserDetailsService {

    @Resource
    private UmsAdminRepository umsAdminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UmsAdmin admin = umsAdminRepository.findByUsernameAndStatus(username, 1)
                .orElseThrow(() -> new UsernameNotFoundException("User not found or disabled: " + username));

        List<String> perms = umsAdminRepository.selectPermsByUserId(admin.getId());
        List<SimpleGrantedAuthority> authorities = perms != null
                ? perms.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                : Collections.emptyList();

        return new User(admin.getUsername(), admin.getPassword(), authorities);
    }
}
