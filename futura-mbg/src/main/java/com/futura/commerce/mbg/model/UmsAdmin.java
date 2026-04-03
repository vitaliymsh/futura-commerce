package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Domain entity mapping table ums_admin
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "ums_admin")
public class UmsAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String nickName;
    private String avatar;
    private String phone;
    private Integer status;
    private Long roleId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
