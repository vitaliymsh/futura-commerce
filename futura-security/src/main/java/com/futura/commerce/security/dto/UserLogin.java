package com.futura.commerce.security.dto;

import com.futura.commerce.mbg.model.UmsUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Customer user principal details wrapper
 *
 * @author Vitalii
 */
public class UserLogin extends User {
    private final UmsUser umsUser;

    public UserLogin(UmsUser umsUser, Collection<? extends GrantedAuthority> authorities) {
        super(umsUser.getPhone(), umsUser.getPassword() != null ? umsUser.getPassword() : "", authorities);
        this.umsUser = umsUser;
    }

    public UmsUser getUmsUser() {
        return umsUser;
    }
}
