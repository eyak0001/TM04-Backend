package com.cosmetic.analysis.controller;

import com.cosmetic.analysis.dto.ApiResponse;
import com.cosmetic.analysis.dto.ProductInfoResponse;
import com.cosmetic.analysis.entity.ProductInfo;
import com.cosmetic.analysis.service.ProductInfoService;
import com.cosmetic.analysis.service.RiskInfoService;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductInfoController {

    private final ProductInfoService productInfoService;
    private final RiskInfoService riskInfoService;

    public ProductInfoController(ProductInfoService productInfoService, RiskInfoService riskInfoService) {
        this.productInfoService = productInfoService;
        this.riskInfoService = riskInfoService;
    }

    // 获取全部数据
    @PostMapping("/all")
    public ApiResponse<List<ProductInfo>> getAll() {
        List<ProductInfo> list = productInfoService.getAll();
        return new ApiResponse<>(200, list, "success", list.size());
    }

    // 根据 notifNo 查询（接收 JSON Body）
    @PostMapping("/search-by-notifNo")
    public ApiResponse<ProductInfoResponse> getProductByNotifNo(@RequestBody NotifNoRequest request) {
        try {
            String notifNo = request.getNotifNo();
            ProductInfo product = productInfoService.getByNotifNo(notifNo);

            if (product == null) {
                return new ApiResponse<>(404, null, "Product not found", 0);
            }

            ProductInfoResponse response = new ProductInfoResponse();
            response.setNotifNo(product.getNotifNo());
            response.setProduct(product.getProduct());
            response.setCompany(product.getCompany());
            response.setDateNotif(product.getDateNotif());
            response.setStatus(product.isStatus());

            if (product.isStatus()) {
                response.setSubstanceDetected(new ArrayList<>());
            } else {
                List<String> substances = riskInfoService.findSubstanceDetectedByNotifNo(product.getNotifNo());
                response.setSubstanceDetected(substances != null ? substances : new ArrayList<>());
            }

            return new ApiResponse<>(200, response, "success", 1);
            
        } catch (Exception e) {
            return new ApiResponse<>(500, null, "Internal server error: " + e.getMessage(), 0);
        }
    }

    // 随机获取4条产品数据
    @PostMapping("/recommend")
    public ApiResponse<List<ProductBasicInfo>> getRandomProducts(){   
    try {
            List<ProductInfo> allProducts = productInfoService.getAll();
            
            if (allProducts.isEmpty()) {
                return new ApiResponse<>(200, new ArrayList<>(), "No products found", 0);
            }
            
            // 获取请求的数量，默认为4
            int count = 4;
            
            // 随机获取指定数量的数据
            List<ProductInfo> randomProducts = getRandomItems(allProducts, count);
            
            // 转换为只包含基本信息的DTO
            List<ProductBasicInfo> basicInfoList = randomProducts.stream()
                    .map(product -> new ProductBasicInfo(
                            product.getNotifNo(),
                            product.getProduct(),
                            product.getCompany()
                    ))
                    .collect(Collectors.toList());
            
            return new ApiResponse<>(200, basicInfoList, "success", basicInfoList.size());
            
        } catch (Exception e) {
            return new ApiResponse<>(500, null, "Internal server error: " + e.getMessage(), 0);
        }
    }
    
    // 分页搜索产品
    @PostMapping("/search")
    public ApiResponse<List<ProductInfo>> searchProducts(@RequestBody SearchRequest request) {
        try {
            // 获取所有产品
            List<ProductInfo> allProducts = productInfoService.getAll();
            
            // 应用搜索和过滤条件
            List<ProductInfo> filteredProducts = allProducts.stream()
                    .filter(product -> matchesSearchTerm(product, request.getSearchTerm()))
                    .filter(product -> matchesStatusFilter(product, request.getFilter()))
                    .collect(Collectors.toList());
            
            // 分页处理
            int pageNum = request.getPageNum() != null ? request.getPageNum() : 1;
            int pageSize = request.getPageSize() != null ? request.getPageSize() : 10;
            
            // 计算分页
            int totalItems = filteredProducts.size();
            int startIndex = (pageNum - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalItems);
            
            List<ProductInfo> pagedProducts;
            if (startIndex < totalItems) {
                pagedProducts = filteredProducts.subList(startIndex, endIndex);
            } else {
                pagedProducts = new ArrayList<>();
            }
            
            return new ApiResponse<>(200, pagedProducts, "success", totalItems);
            
        } catch (Exception e) {
            return new ApiResponse<>(500, null, "Internal server error: " + e.getMessage(), 0);
        }
    }
    
    // 检查产品是否匹配搜索词
    private boolean matchesSearchTerm(ProductInfo product, String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return true; // 空搜索词匹配所有
        }
        
        String term = searchTerm.toLowerCase().trim();
        
        // 检查 notifNo, product, company 字段
        return (product.getNotifNo() != null && product.getNotifNo().toLowerCase().contains(term)) ||
               (product.getProduct() != null && product.getProduct().toLowerCase().contains(term)) ||
               (product.getCompany() != null && product.getCompany().toLowerCase().contains(term));
    }
    
    // 检查产品是否匹配状态过滤器
    private boolean matchesStatusFilter(ProductInfo product, FilterRequest filter) {
        if (filter == null) {
            return true; // 无过滤条件，显示所有
        }
        
        boolean showSafe = filter.getShowSafe() != null ? filter.getShowSafe() : false;
        boolean showUnsafe = filter.getShowUnsafe() != null ? filter.getShowUnsafe() : false;
        
        // showSafe=false, showUnsafe=false: 不显示产品
        if (!showSafe && !showUnsafe) {
            return false;
        }
        
        // showSafe=true, showUnsafe=true: 显示所有产品
        if (showSafe && showUnsafe) {
            return true;
        }
        
        // showSafe=true, showUnsafe=false: 只显示安全产品 (status = true)
        if (showSafe && !showUnsafe) {
            return product.isStatus() == true;
        }
        
        // showSafe=false, showUnsafe=true: 只显示不安全产品 (status = false)
        if (!showSafe && showUnsafe) {
            return product.isStatus() == false;
        }
        
        return true;
    }

    // 随机选择指定数量的元素
    private <T> List<T> getRandomItems(List<T> list, int count) {
        if (list.size() <= count) {
            return new ArrayList<>(list);
        }
        
        List<T> shuffled = new ArrayList<>(list);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, count);
    }

    // DTO 用来接收查询 notifNo 的 JSON 请求体
    public static class NotifNoRequest {
        private String notifNo;

        public String getNotifNo() {
            return notifNo;
        }

        public void setNotifNo(String notifNo) {
            this.notifNo = notifNo;
        }
    }
    
    // 随机产品请求DTO
    public static class RandomProductRequest {
        private Integer count;
        
        public RandomProductRequest() {}
        
        public Integer getCount() {
            return count;
        }
        
        public void setCount(Integer count) {
            this.count = count;
        }
    }

    // 搜索请求DTO
    public static class SearchRequest {
        private String searchTerm;
        private Integer pageNum;
        private Integer pageSize;
        private FilterRequest filter;
        
        public SearchRequest() {}
        
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
        
        public FilterRequest getFilter() {
            return filter;
        }
        
        public void setFilter(FilterRequest filter) {
            this.filter = filter;
        }
    }
    
    // 过滤器请求DTO
    public static class FilterRequest {
        private Boolean showSafe;
        private Boolean showUnsafe;
        
        public FilterRequest() {}
        
        public Boolean getShowSafe() {
            return showSafe;
        }
        
        public void setShowSafe(Boolean showSafe) {
            this.showSafe = showSafe;
        }
        
        public Boolean getShowUnsafe() {
            return showUnsafe;
        }
        
        public void setShowUnsafe(Boolean showUnsafe) {
            this.showUnsafe = showUnsafe;
        }
    }

    // 产品基本信息DTO
    public static class ProductBasicInfo {
        private String notifNo;
        private String product;
        private String company;
        
        public ProductBasicInfo() {}
        
        public ProductBasicInfo(String notifNo, String product, String company) {
            this.notifNo = notifNo;
            this.product = product;
            this.company = company;
        }
        
        public String getNotifNo() {
            return notifNo;
        }
        
        public void setNotifNo(String notifNo) {
            this.notifNo = notifNo;
        }
        
        public String getProduct() {
            return product;
        }
        
        public void setProduct(String product) {
            this.product = product;
        }
        
        public String getCompany() {
            return company;
        }
        
        public void setCompany(String company) {
            this.company = company;
        }
    }
}