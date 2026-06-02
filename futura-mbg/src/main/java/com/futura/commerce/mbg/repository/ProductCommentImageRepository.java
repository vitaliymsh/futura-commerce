package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.ProductCommentImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for ProductCommentImage
 *
 * @author Vitalii
 */
@Repository
public interface ProductCommentImageRepository extends JpaRepository<ProductCommentImage, Long> {
    List<ProductCommentImage> findByCommentIdIn(Collection<Long> commentIds);
    List<ProductCommentImage> findByCommentId(Long commentId);
}
