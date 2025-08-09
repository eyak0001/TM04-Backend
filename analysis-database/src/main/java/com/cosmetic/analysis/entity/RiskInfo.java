package com.cosmetic.analysis.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "risk_info")
@Data
@IdClass(RiskInfoId.class) // 复合主键
public class RiskInfo {

    @Id
    @Column(name = "notif_no", length = 50)
    private String notifNo;

    @Id
    @Column(name = "substance_detected", length = 50)
    private String substanceDetected;
}
