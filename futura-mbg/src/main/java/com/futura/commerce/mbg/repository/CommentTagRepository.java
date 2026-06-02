package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.CommentTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for CommentTag
 *
 * @author Vitalii
 */
@Repository
public interface CommentTagRepository extends JpaRepository<CommentTag, Long> {
}
