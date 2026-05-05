package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Domain entity mapping table ums_user
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "ums_user")
public class UmsUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String nickname;

    private String phone;

    private Integer age;

    /**
     * Gender: 1-male, 0-female
     */
    private Integer gender;

    /**
     * Login password
     */
    private String password;

    private LocalDateTime createTime;
}
