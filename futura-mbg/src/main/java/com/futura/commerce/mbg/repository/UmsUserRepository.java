package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.UmsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for UmsUser
 *
 * @author Vitalii
 */
@Repository
public interface UmsUserRepository extends JpaRepository<UmsUser, Long> {
    Optional<UmsUser> findByPhone(String phone);
    Optional<UmsUser> findByUsername(String username);
}
