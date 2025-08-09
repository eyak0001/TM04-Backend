// 创建 RiskInfoId.java
package com.cosmetic.analysis.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@Data
@EqualsAndHashCode
public class RiskInfoId implements Serializable {
    private String notifNo;
    private String substanceDetected;
    
    public RiskInfoId() {}
    
    public RiskInfoId(String notifNo, String substanceDetected) {
        this.notifNo = notifNo;
        this.substanceDetected = substanceDetected;
    }
}