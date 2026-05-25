package com.futura.commerce.security.service.impl;

import com.futura.commerce.mbg.model.UmsUser;
import com.futura.commerce.mbg.repository.UmsUserRepository;
import com.futura.commerce.security.dto.UserLogin;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Customer user details service using Spring Data JPA repository
 *
 * @author Vitalii
 */
@Service
public class UserDetailService implements UserDetailsService {

    @Resource
    private UmsUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        UmsUser umsUser = userRepository.findByPhone(phone)
                .or(() -> userRepository.findByUsername(phone))
                .orElseThrow(() -> new UsernameNotFoundException("Customer user not found: " + phone));

        return new UserLogin(umsUser, List.of());
    }
}
