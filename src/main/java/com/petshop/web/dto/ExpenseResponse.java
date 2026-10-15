package com.petshop.web.dto;

import com.petshop.domain.ExpenseCategory;
import com.petshop.domain.ExpenseStatus;
import com.petshop.domain.PaymentMethod;
import java.time.Instant;
import java.time.LocalDate;

public class ExpenseResponse {

    private Long id;
    private String description;
    private ExpenseCategory category;
    private int amountCents;
    private LocalDate dueDate;
    private ExpenseStatus status;
    private PaymentMethod paymentMethod;
    private Instant paidAt;
    private String notes;
    private boolean overdue;

    public ExpenseResponse(
            Long id,
            String description,
            ExpenseCategory category,
            int amountCents,
            LocalDate dueDate,
            ExpenseStatus status,
            PaymentMethod paymentMethod,
            Instant paidAt,
            String notes,
            boolean overdue) {
        this.id = id;
        this.description = description;
        this.category = category;
        this.amountCents = amountCents;
        this.dueDate = dueDate;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.paidAt = paidAt;
        this.notes = notes;
        this.overdue = overdue;
    }

    public Long getId() { return id; }
    public String getDescription() { return description; }
    public ExpenseCategory getCategory() { return category; }
    public int getAmountCents() { return amountCents; }
    public LocalDate getDueDate() { return dueDate; }
    public ExpenseStatus getStatus() { return status; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public Instant getPaidAt() { return paidAt; }
    public String getNotes() { return notes; }
    public boolean isOverdue() { return overdue; }
}
