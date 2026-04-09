package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Administrative role entity
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "ums_role")
public class UmsRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Integer adminCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer status; // 0 = disabled, 1 = enabled
}
