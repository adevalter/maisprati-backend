package com.petshop.web.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class PetHistoryResponse {

    private Long id;
    private String type;
    private String title;
    private String body;
    private Instant occurredAt;
    private LocalDate nextDueAt;
    private BigDecimal weightKg;
    private String professionalName;
    private Long vaccineProductId;
    private String vaccineProductName;
    private Integer doseNumber;

    public PetHistoryResponse(
            Long id,
            String type,
            String title,
            String body,
            Instant occurredAt,
            LocalDate nextDueAt,
            BigDecimal weightKg,
            String professionalName,
            Long vaccineProductId,
            String vaccineProductName,
            Integer doseNumber) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.body = body;
        this.occurredAt = occurredAt;
        this.nextDueAt = nextDueAt;
        this.weightKg = weightKg;
        this.professionalName = professionalName;
        this.vaccineProductId = vaccineProductId;
        this.vaccineProductName = vaccineProductName;
        this.doseNumber = doseNumber;
    }

    public Long getId() { return id; }
    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public Instant getOccurredAt() { return occurredAt; }
    public LocalDate getNextDueAt() { return nextDueAt; }
    public BigDecimal getWeightKg() { return weightKg; }
    public String getProfessionalName() { return professionalName; }
    public Long getVaccineProductId() { return vaccineProductId; }
    public String getVaccineProductName() { return vaccineProductName; }
    public Integer getDoseNumber() { return doseNumber; }
}
