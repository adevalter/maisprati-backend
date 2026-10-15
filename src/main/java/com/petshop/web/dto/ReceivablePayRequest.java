package com.petshop.web.dto;

import com.petshop.domain.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public class ReceivablePayRequest {

    @NotNull
    private PaymentMethod paymentMethod;

    private Integer amountCents;

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public Integer getAmountCents() { return amountCents; }
    public void setAmountCents(Integer amountCents) { this.amountCents = amountCents; }
}
