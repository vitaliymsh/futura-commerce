package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Product comment uploaded images entity
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "product_comment_image")
public class ProductCommentImage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long commentId;

    @Column(name = "img_url")
    private String imgUrl;

    private Integer sort;

    private LocalDateTime createTime;
}
