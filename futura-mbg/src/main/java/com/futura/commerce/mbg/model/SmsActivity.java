package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Domain entity mapping table sms_activity
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "sms_activity")
public class SmsActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
}
