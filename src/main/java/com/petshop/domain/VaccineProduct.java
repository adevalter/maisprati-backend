package com.petshop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "vaccine_products")
public class VaccineProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 20)
    private String species = "TODOS";

    @Column(name = "default_interval_days", nullable = false)
    private int defaultIntervalDays = 365;

    @Column(name = "recommended_doses", nullable = false)
    private int recommendedDoses = 1;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    public int getDefaultIntervalDays() { return defaultIntervalDays; }
    public void setDefaultIntervalDays(int defaultIntervalDays) { this.defaultIntervalDays = defaultIntervalDays; }
    public int getRecommendedDoses() { return recommendedDoses; }
    public void setRecommendedDoses(int recommendedDoses) { this.recommendedDoses = recommendedDoses; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
