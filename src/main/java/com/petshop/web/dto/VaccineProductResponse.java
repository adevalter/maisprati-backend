package com.petshop.web.dto;

public class VaccineProductResponse {

    private Long id;
    private String name;
    private String species;
    private int defaultIntervalDays;
    private int recommendedDoses;
    private boolean active;

    public VaccineProductResponse(
            Long id,
            String name,
            String species,
            int defaultIntervalDays,
            int recommendedDoses,
            boolean active) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.defaultIntervalDays = defaultIntervalDays;
        this.recommendedDoses = recommendedDoses;
        this.active = active;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public int getDefaultIntervalDays() { return defaultIntervalDays; }
    public int getRecommendedDoses() { return recommendedDoses; }
    public boolean isActive() { return active; }
}
