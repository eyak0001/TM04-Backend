package com.cosmetic.analysis.service.impl;

import com.cosmetic.analysis.entity.RiskInfo;
import com.cosmetic.analysis.repository.RiskInfoRepository;
import com.cosmetic.analysis.service.RiskInfoService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RiskInfoServiceImpl implements RiskInfoService {

    private final RiskInfoRepository repository;

    public RiskInfoServiceImpl(RiskInfoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<RiskInfo> getAll() {
        return repository.findAll();
    }

    @Override
    public List<RiskInfo> findByNotifNo(String notifNo) {
        return repository.findAll()
                .stream()
                .filter(r -> r.getNotifNo().equalsIgnoreCase(notifNo))
                .toList();
    }
    
    @Override
    public List<String> findSubstanceDetectedByNotifNo(String notifNo) {
        return repository.findSubstanceDetectedByNotifNo(notifNo);
    }
}
