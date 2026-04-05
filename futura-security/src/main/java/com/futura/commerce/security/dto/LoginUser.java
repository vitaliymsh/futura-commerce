package com.futura.commerce.security.dto;

import com.futura.commerce.mbg.model.UmsAdmin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Custom UserDetails implementation holding authenticated UmsAdmin entity
 *
 * @author Vitalii
 */
public class LoginUser extends User {

    private final UmsAdmin admin;

    public LoginUser(UmsAdmin admin, Collection<? extends GrantedAuthority> authorities) {
        super(admin.getUsername(), admin.getPassword(), authorities);
        this.admin = admin;
    }

    public UmsAdmin getAdmin() {
        return admin;
    }
}
