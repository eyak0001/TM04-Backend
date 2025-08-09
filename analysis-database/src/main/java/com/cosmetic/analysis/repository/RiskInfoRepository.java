package com.cosmetic.analysis.repository;

import com.cosmetic.analysis.entity.RiskInfo;
import com.cosmetic.analysis.entity.RiskInfoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskInfoRepository extends JpaRepository<RiskInfo, RiskInfoId> {
    
    @Query("SELECT r.substanceDetected FROM RiskInfo r WHERE r.notifNo = :notifNo")
    List<String> findSubstanceDetectedByNotifNo(@Param("notifNo") String notifNo);
    List<RiskInfo> findByNotifNo(String notifNo);
}