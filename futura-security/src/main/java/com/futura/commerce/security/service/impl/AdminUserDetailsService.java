package com.futura.commerce.security.service.impl;

import com.futura.commerce.mbg.model.UmsAdmin;
import com.futura.commerce.mbg.repository.UmsAdminRepository;
import com.futura.commerce.security.dto.LoginUser;
import jakarta.annotation.Resource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (perms != null) {
            for (String p : perms) {
                if (p != null && !p.trim().isEmpty()) {
                    authorities.add(new SimpleGrantedAuthority(p.trim()));
                    if (p.startsWith("admin:")) {
                        authorities.add(new SimpleGrantedAuthority(p.substring(6).trim()));
                    }
                }
            }
        }

        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("user:manage"));
        authorities.add(new SimpleGrantedAuthority("delivery:view"));
        authorities.add(new SimpleGrantedAuthority("delivery:edit"));
        authorities.add(new SimpleGrantedAuthority("afterSales:view"));
        authorities.add(new SimpleGrantedAuthority("order:manage"));
        authorities.add(new SimpleGrantedAuthority("activity:view"));
        authorities.add(new SimpleGrantedAuthority("promotion:view"));
        authorities.add(new SimpleGrantedAuthority("dashboard:view"));

        return new LoginUser(admin, authorities);
    }
}
