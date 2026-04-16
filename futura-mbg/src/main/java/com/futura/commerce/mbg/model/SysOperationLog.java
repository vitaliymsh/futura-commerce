package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * System operation audit log entity
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "sys_operation_log")
public class SysOperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String module;
    private String operationType;
    private String content;
    private String businessId;
    private String operator;
    private LocalDateTime createTime;
}
