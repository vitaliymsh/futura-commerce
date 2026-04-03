package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.UmsAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for UmsAdmin
 *
 * @author Vitalii
 */
public interface UmsAdminRepository extends JpaRepository<UmsAdmin, Long>, JpaSpecificationExecutor<UmsAdmin> {

    Optional<UmsAdmin> findByUsernameAndStatus(String username, Integer status);

    Optional<UmsAdmin> findByUsername(String username);

    @Query(value = "SELECT u.permission FROM ums_menu u " +
           "INNER JOIN ums_role_menu um ON u.id = um.menu_id " +
           "INNER JOIN ums_admin ua ON ua.role_id = um.role_id " +
           "WHERE ua.id = :id AND ua.status = 1 AND u.status = 1", nativeQuery = true)
    List<String> selectPermsByUserId(@Param("id") Long id);
}
