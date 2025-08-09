package com.cosmetic.analysis.service.impl;

import com.cosmetic.analysis.entity.ProductInfo;
import com.cosmetic.analysis.repository.ProductInfoRepository;
import com.cosmetic.analysis.service.ProductInfoService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    private final ProductInfoRepository repository;

    public ProductInfoServiceImpl(ProductInfoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProductInfo> getAll() {
        return repository.findAll();
    }

    @Override
    public ProductInfo getByNotifNo(String notifNo) {
        return repository.findByNotifNo(notifNo); 
    }
}
