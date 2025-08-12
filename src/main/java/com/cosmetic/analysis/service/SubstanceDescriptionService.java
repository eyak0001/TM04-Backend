package com.cosmetic.analysis.service;

import com.cosmetic.analysis.entity.SubstanceDescription;
import java.util.List;

public interface SubstanceDescriptionService {
    List<SubstanceDescription> getAll();
    SubstanceDescription getBySubstanceDetected(String substanceDetected);
}
