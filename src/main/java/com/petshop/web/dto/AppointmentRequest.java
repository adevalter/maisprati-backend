package com.petshop.web.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class AppointmentRequest {

    @NotNull
    private Long clientId;

    @NotNull
    private Long petId;

    @NotNull
    private Long serviceId;

    private Long professionalId;

    @NotNull
    private Instant scheduledAt;

    private boolean homeVisit;
    private String addressOverride;
    private String notes;

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public Long getPetId() { return petId; }
    public void setPetId(Long petId) { this.petId = petId; }
    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }
    public Long getProfessionalId() { return professionalId; }
    public void setProfessionalId(Long professionalId) { this.professionalId = professionalId; }
    public Instant getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(Instant scheduledAt) { this.scheduledAt = scheduledAt; }
    public boolean isHomeVisit() { return homeVisit; }
    public void setHomeVisit(boolean homeVisit) { this.homeVisit = homeVisit; }
    public String getAddressOverride() { return addressOverride; }
    public void setAddressOverride(String addressOverride) { this.addressOverride = addressOverride; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
