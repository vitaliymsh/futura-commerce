package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

/**
 * Product comment tag association entity
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "comment_tag_relation")
public class CommentTagRelation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long commentId;

    private Long tagId;
}
