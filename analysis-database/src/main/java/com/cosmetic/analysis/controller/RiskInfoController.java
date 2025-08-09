package com.cosmetic.analysis.controller;

import com.cosmetic.analysis.dto.ApiResponse;
import com.cosmetic.analysis.entity.RiskInfo;
import com.cosmetic.analysis.service.RiskInfoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk")
public class RiskInfoController {

    private final RiskInfoService service;

    public RiskInfoController(RiskInfoService service) {
        this.service = service;
    }

    @PostMapping("/all")
    public List<RiskInfo> getAll() {
        return service.getAll();
    }

    // 根据 notifNo 获取风险信息
    @PostMapping("/byNotifNo")
    public ApiResponse<RiskInfo> getByNotifNo(@RequestParam String notifNo) {
        List<RiskInfo> list = service.findByNotifNo(notifNo);
        RiskInfo first = list.isEmpty() ? null : list.get(0);
        return new ApiResponse<>(200, first, "success", first == null ? 0 : 1);
    }
}
