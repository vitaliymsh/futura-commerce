package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.UmsCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for customer cart
 *
 * @author Vitalii
 */
@Repository
public interface UmsCartRepository extends JpaRepository<UmsCart, Long> {

    List<UmsCart> findByUserId(Long userId);

    List<UmsCart> findByUserIdAndSelected(Long userId, Integer selected);
}
