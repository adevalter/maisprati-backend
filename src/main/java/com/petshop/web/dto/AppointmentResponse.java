package com.petshop.web.dto;

import com.petshop.domain.AppointmentStatus;
import com.petshop.domain.PaymentMethod;
import com.petshop.domain.PaymentStatus;
import java.time.Instant;

public class AppointmentResponse {

    private Long id;
    private Long clientId;
    private String clientName;
    private Long petId;
    private String petName;
    private Long serviceId;
    private String serviceName;
    private String serviceCategory;
    private Long professionalId;
    private String professionalName;
    private Instant scheduledAt;
    private AppointmentStatus status;
    private boolean homeVisit;
    private String addressOverride;
    private String notes;
    private Instant checkInAt;
    private Instant completedAt;
    private Integer servicePriceCents;
    private Integer amountCents;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private Instant paidAt;

    public AppointmentResponse(
            Long id,
            Long clientId,
            String clientName,
            Long petId,
            String petName,
            Long serviceId,
            String serviceName,
            String serviceCategory,
            Long professionalId,
            String professionalName,
            Instant scheduledAt,
            AppointmentStatus status,
            boolean homeVisit,
            String addressOverride,
            String notes,
            Instant checkInAt,
            Instant completedAt,
            Integer servicePriceCents,
            Integer amountCents,
            PaymentStatus paymentStatus,
            PaymentMethod paymentMethod,
            Instant paidAt) {
        this.id = id;
        this.clientId = clientId;
        this.clientName = clientName;
        this.petId = petId;
        this.petName = petName;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceCategory = serviceCategory;
        this.professionalId = professionalId;
        this.professionalName = professionalName;
        this.scheduledAt = scheduledAt;
        this.status = status;
        this.homeVisit = homeVisit;
        this.addressOverride = addressOverride;
        this.notes = notes;
        this.checkInAt = checkInAt;
        this.completedAt = completedAt;
        this.servicePriceCents = servicePriceCents;
        this.amountCents = amountCents;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.paidAt = paidAt;
    }

    public Long getId() { return id; }
    public Long getClientId() { return clientId; }
    public String getClientName() { return clientName; }
    public Long getPetId() { return petId; }
    public String getPetName() { return petName; }
    public Long getServiceId() { return serviceId; }
    public String getServiceName() { return serviceName; }
    public String getServiceCategory() { return serviceCategory; }
    public Long getProfessionalId() { return professionalId; }
    public String getProfessionalName() { return professionalName; }
    public Instant getScheduledAt() { return scheduledAt; }
    public AppointmentStatus getStatus() { return status; }
    public boolean isHomeVisit() { return homeVisit; }
    public String getAddressOverride() { return addressOverride; }
    public String getNotes() { return notes; }
    public Instant getCheckInAt() { return checkInAt; }
    public Instant getCompletedAt() { return completedAt; }
    public Integer getServicePriceCents() { return servicePriceCents; }
    public Integer getAmountCents() { return amountCents; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public Instant getPaidAt() { return paidAt; }
}
