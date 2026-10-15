package com.petshop.web.dto;

import com.petshop.domain.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public class ExpensePayRequest {

    @NotNull
    private PaymentMethod paymentMethod;

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
}
