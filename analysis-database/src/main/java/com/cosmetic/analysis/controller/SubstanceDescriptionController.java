package com.cosmetic.analysis.controller;

import com.cosmetic.analysis.dto.ApiResponse;
import com.cosmetic.analysis.entity.SubstanceDescription;
import com.cosmetic.analysis.service.SubstanceDescriptionService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ingredients")
public class SubstanceDescriptionController {

    private final SubstanceDescriptionService service;

    public SubstanceDescriptionController(SubstanceDescriptionService service) {
        this.service = service;
    }

    // 获取全部
    @PostMapping("/all")
    public ApiResponse<List<SubstanceDescription>> getAll() {
        List<SubstanceDescription> list = service.getAll();
        return new ApiResponse<>(200, list, "success", list.size());
    }

    @PostMapping("/search-by-name")
    public ApiResponse<String> getByDetected(@RequestBody Map<String, String> body) {
        String substanceDetected = body.get("substanceDetected");

        if (substanceDetected == null || substanceDetected.trim().isEmpty()) {
            return new ApiResponse<>(400, null, "substanceDetected is required", 0);
        }

        SubstanceDescription description = service.getBySubstanceDetected(substanceDetected);

        if (description == null) {
            return new ApiResponse<>(404, null, "No description found", 0);
        }

        // 返回 description 字段的字符串
        return new ApiResponse<>(200, description.getDescription(), "", 1);
    }

    // 分页搜索物质名称 - 返回包含 substance_detected 字段的对象
    @PostMapping("/search")
    public ApiResponse<List<SubstanceResult>> searchSubstances(@RequestBody SubstanceSearchRequest request) {
        try {
            // 获取所有物质描述
            List<SubstanceDescription> allSubstances = service.getAll();
            
            // 应用搜索条件并转换为结果对象
            List<SubstanceResult> filteredSubstances = allSubstances.stream()
                    .filter(substance -> matchesSearchTerm(substance, request.getSearchTerm()))
                    .map(substance -> new SubstanceResult(substance.getSubstanceDetected()))  // 转换为对象
                    .collect(Collectors.toList());
            
            // 分页处理
            int pageNum = request.getPageNum() != null ? request.getPageNum() : 1;
            int pageSize = request.getPageSize() != null ? request.getPageSize() : 10;
            
            // 计算分页
            int totalItems = filteredSubstances.size();
            int startIndex = (pageNum - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalItems);
            
            List<SubstanceResult> pagedSubstances;
            if (startIndex < totalItems) {
                pagedSubstances = filteredSubstances.subList(startIndex, endIndex);
            } else {
                pagedSubstances = new ArrayList<>();
            }
            
            return new ApiResponse<>(200, pagedSubstances, "success", totalItems);
            
        } catch (Exception e) {
            return new ApiResponse<>(500, null, "Internal server error: " + e.getMessage(), 0);
        }
    }
    
    // 检查物质是否匹配搜索词
    private boolean matchesSearchTerm(SubstanceDescription substance, String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return true; // 空搜索词匹配所有
        }
        
        String term = searchTerm.toLowerCase().trim();
        
        // 检查 substance_detected 字段
        return substance.getSubstanceDetected() != null && 
               substance.getSubstanceDetected().toLowerCase().contains(term);
    }

    // 物质搜索请求DTO
    public static class SubstanceSearchRequest {
        private String searchTerm;
        private Integer pageNum;
        private Integer pageSize;
        
        public SubstanceSearchRequest() {}
        
        public String getSearchTerm() {
            return searchTerm;
        }
        
        public void setSearchTerm(String searchTerm) {
            this.searchTerm = searchTerm;
        }
        
        public Integer getPageNum() {
            return pageNum;
        }
        
        public void setPageNum(Integer pageNum) {
            this.pageNum = pageNum;
        }
        
        public Integer getPageSize() {
            return pageSize;
        }
        
        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }
    }
    
    // 物质搜索结果DTO
    public static class SubstanceResult {
        private String substanceDetected;
        
        public SubstanceResult() {}
        
        public SubstanceResult(String substanceDetected) {
            this.substanceDetected = substanceDetected;
        }
        
        public String getSubstanceDetected() {
            return substanceDetected;
        }
        
        public void setSubstanceDetected(String substanceDetected) {
            this.substanceDetected = substanceDetected;
        }
    }
}