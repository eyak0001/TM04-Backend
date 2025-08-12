package com.cosmetic.analysis.service;

import com.cosmetic.analysis.entity.RiskInfo;
import java.util.List;

public interface RiskInfoService {
    List<RiskInfo> getAll();
    List<RiskInfo> findByNotifNo(String notifNo);
    List<String> findSubstanceDetectedByNotifNo(String notifNo);
}
