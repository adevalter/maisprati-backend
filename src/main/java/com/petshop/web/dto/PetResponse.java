package com.petshop.web.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class PetResponse {

    private Long id;
    private Long clientId;
    private String clientName;
    private String name;
    private String species;
    private String breed;
    private String sex;
    private LocalDate birthDate;
    private BigDecimal weightKg;
    private String allergies;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;

    public PetResponse(
            Long id,
            Long clientId,
            String clientName,
            String name,
            String species,
            String breed,
            String sex,
            LocalDate birthDate,
            BigDecimal weightKg,
            String allergies,
            String notes,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.clientId = clientId;
        this.clientName = clientName;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.sex = sex;
        this.birthDate = birthDate;
        this.weightKg = weightKg;
        this.allergies = allergies;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public Long getClientId() { return clientId; }
    public String getClientName() { return clientName; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public String getBreed() { return breed; }
    public String getSex() { return sex; }
    public LocalDate getBirthDate() { return birthDate; }
    public BigDecimal getWeightKg() { return weightKg; }
    public String getAllergies() { return allergies; }
    public String getNotes() { return notes; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
