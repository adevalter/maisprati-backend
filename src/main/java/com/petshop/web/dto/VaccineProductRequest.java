package com.petshop.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VaccineProductRequest {

    @NotBlank
    @Size(max = 120)
    private String name;

    @Size(max = 20)
    private String species;

    @Min(1)
    private Integer defaultIntervalDays;

    @Min(1)
    private Integer recommendedDoses;

    private Boolean active;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    public Integer getDefaultIntervalDays() { return defaultIntervalDays; }
    public void setDefaultIntervalDays(Integer defaultIntervalDays) {
        this.defaultIntervalDays = defaultIntervalDays;
    }
    public Integer getRecommendedDoses() { return recommendedDoses; }
    public void setRecommendedDoses(Integer recommendedDoses) {
        this.recommendedDoses = recommendedDoses;
    }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
