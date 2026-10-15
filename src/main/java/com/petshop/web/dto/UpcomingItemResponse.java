package com.petshop.web.dto;

import java.time.LocalDate;

public class UpcomingItemResponse {

    private Long historyId;
    private Long petId;
    private String petName;
    private Long clientId;
    private String clientName;
    private String type;
    private String title;
    private LocalDate nextDueAt;
    private Integer doseNumber;

    public UpcomingItemResponse(
            Long historyId,
            Long petId,
            String petName,
            Long clientId,
            String clientName,
            String type,
            String title,
            LocalDate nextDueAt,
            Integer doseNumber) {
        this.historyId = historyId;
        this.petId = petId;
        this.petName = petName;
        this.clientId = clientId;
        this.clientName = clientName;
        this.type = type;
        this.title = title;
        this.nextDueAt = nextDueAt;
        this.doseNumber = doseNumber;
    }

    public Long getHistoryId() { return historyId; }
    public Long getPetId() { return petId; }
    public String getPetName() { return petName; }
    public Long getClientId() { return clientId; }
    public String getClientName() { return clientName; }
    public String getType() { return type; }
    public String getTitle() { return title; }
    public LocalDate getNextDueAt() { return nextDueAt; }
    public Integer getDoseNumber() { return doseNumber; }
}
