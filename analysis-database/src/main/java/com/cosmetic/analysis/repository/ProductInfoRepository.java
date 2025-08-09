package com.cosmetic.analysis.repository;

import com.cosmetic.analysis.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {
    ProductInfo findByNotifNo(String notifNo);
}
