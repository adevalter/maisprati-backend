package com.petshop.web.dto;

import com.petshop.domain.AppointmentStatus;
import com.petshop.domain.PaymentMethod;
import com.petshop.domain.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AppointmentStatusRequest {

    @NotNull
    private AppointmentStatus status;

    private String notes;

    private LocalDate nextDueAt;

    private Long vaccineProductId;

    private Integer doseNumber;

    private PaymentStatus paymentStatus;

    private PaymentMethod paymentMethod;

    private Integer amountCents;

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDate getNextDueAt() { return nextDueAt; }
    public void setNextDueAt(LocalDate nextDueAt) { this.nextDueAt = nextDueAt; }
    public Long getVaccineProductId() { return vaccineProductId; }
    public void setVaccineProductId(Long vaccineProductId) { this.vaccineProductId = vaccineProductId; }
    public Integer getDoseNumber() { return doseNumber; }
    public void setDoseNumber(Integer doseNumber) { this.doseNumber = doseNumber; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public Integer getAmountCents() { return amountCents; }
    public void setAmountCents(Integer amountCents) { this.amountCents = amountCents; }
}
