package com.cosmetic.analysis.service.impl;

import com.cosmetic.analysis.entity.SubstanceDescription;
import com.cosmetic.analysis.repository.SubstanceDescriptionRepository;
import com.cosmetic.analysis.service.SubstanceDescriptionService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SubstanceDescriptionServiceImpl implements SubstanceDescriptionService {

    private final SubstanceDescriptionRepository repository;

    public SubstanceDescriptionServiceImpl(SubstanceDescriptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<SubstanceDescription> getAll() {
        return repository.findAll();
    }

    @Override
    public SubstanceDescription getBySubstanceDetected(String substanceDetected) {
        return repository.findBySubstanceDetected(substanceDetected);
    }
}
