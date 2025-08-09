package com.cosmetic.analysis.repository;

import com.cosmetic.analysis.entity.SubstanceDescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubstanceDescriptionRepository extends JpaRepository<SubstanceDescription, String> {
    SubstanceDescription findBySubstanceDetected(String substanceDetected);
}