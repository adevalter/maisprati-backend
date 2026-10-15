package com.petshop.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PetRequest {

    @NotNull
    private Long clientId;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 40)
    private String species;

    @Size(max = 80)
    private String breed;

    @Size(max = 20)
    private String sex;

    private LocalDate birthDate;

    @Positive
    private BigDecimal weightKg;

    private String allergies;
    private String notes;

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public BigDecimal getWeightKg() { return weightKg; }
    public void setWeightKg(BigDecimal weightKg) { this.weightKg = weightKg; }
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
