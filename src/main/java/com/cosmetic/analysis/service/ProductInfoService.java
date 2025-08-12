package com.cosmetic.analysis.service;

import com.cosmetic.analysis.entity.ProductInfo;
import java.util.List;

public interface ProductInfoService {
    List<ProductInfo> getAll();
    ProductInfo getByNotifNo(String notifNo);
}
