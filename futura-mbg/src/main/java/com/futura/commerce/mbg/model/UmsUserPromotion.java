package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain entity mapping table ums_user_promotion
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "ums_user_promotion")
public class UmsUserPromotion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long packageId;
    private BigDecimal amount;
    private LocalDateTime createTime;
}
