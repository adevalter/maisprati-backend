package com.petshop.web.dto;

import com.petshop.domain.PaymentMethod;
import com.petshop.domain.PaymentStatus;
import java.time.Instant;
import java.util.List;

public class ReceivableItemResponse {

    private Long appointmentId;
    private Instant completedAt;
    private Long clientId;
    private String clientName;
    private Long petId;
    private String petName;
    private String serviceName;
    private Integer amountCents;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private Instant paidAt;

    public ReceivableItemResponse(
            Long appointmentId,
            Instant completedAt,
            Long clientId,
            String clientName,
            Long petId,
            String petName,
            String serviceName,
            Integer amountCents,
            PaymentStatus paymentStatus,
            PaymentMethod paymentMethod,
            Instant paidAt) {
        this.appointmentId = appointmentId;
        this.completedAt = completedAt;
        this.clientId = clientId;
        this.clientName = clientName;
        this.petId = petId;
        this.petName = petName;
        this.serviceName = serviceName;
        this.amountCents = amountCents;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.paidAt = paidAt;
    }

    public Long getAppointmentId() { return appointmentId; }
    public Instant getCompletedAt() { return completedAt; }
    public Long getClientId() { return clientId; }
    public String getClientName() { return clientName; }
    public Long getPetId() { return petId; }
    public String getPetName() { return petName; }
    public String getServiceName() { return serviceName; }
    public Integer getAmountCents() { return amountCents; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public Instant getPaidAt() { return paidAt; }
}
