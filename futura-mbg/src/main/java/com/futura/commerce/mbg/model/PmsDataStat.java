package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Domain entity mapping table pms_data_stat
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "pms_data_stat")
public class PmsDataStat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate statDate;
    private BigDecimal salesAmount;
    private Integer orderCount;
    private Integer userCount;
}
