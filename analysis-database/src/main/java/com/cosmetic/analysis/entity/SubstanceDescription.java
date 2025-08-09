package com.cosmetic.analysis.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "substance_description")
@Data
public class SubstanceDescription {

    @Id
    @Column(name = "substance_detected", length = 50)
    private String substanceDetected;

    @Column(columnDefinition = "TEXT")
    private String description;
}
