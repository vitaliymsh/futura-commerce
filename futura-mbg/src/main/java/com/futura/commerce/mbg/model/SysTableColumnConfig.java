package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User personalized table column display configuration
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "sys_table_column_config")
public class SysTableColumnConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long adminId;
    private String pageCode;
    private String columnCode;
    private String columnName;
    private Integer isShow;
    private Integer sortNum;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
