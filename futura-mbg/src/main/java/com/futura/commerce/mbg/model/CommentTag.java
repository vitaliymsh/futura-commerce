package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

/**
 * Product comment tag dictionary entity
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "comment_tag")
public class CommentTag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tagName;

    private Integer sort;
}
