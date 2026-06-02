package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.CommentTagRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for CommentTagRelation
 *
 * @author Vitalii
 */
@Repository
public interface CommentTagRelationRepository extends JpaRepository<CommentTagRelation, Long> {
    List<CommentTagRelation> findByCommentIdIn(Collection<Long> commentIds);
    List<CommentTagRelation> findByCommentId(Long commentId);
}
