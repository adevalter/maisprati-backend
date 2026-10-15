package com.petshop.web.dto;

import com.petshop.domain.ExpenseCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class ExpenseRequest {

    @NotBlank
    @Size(max = 200)
    private String description;

    @NotNull
    private ExpenseCategory category;

    @NotNull
    @Min(0)
    private Integer amountCents;

    @NotNull
    private LocalDate dueDate;

    private String notes;

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public ExpenseCategory getCategory() { return category; }
    public void setCategory(ExpenseCategory category) { this.category = category; }
    public Integer getAmountCents() { return amountCents; }
    public void setAmountCents(Integer amountCents) { this.amountCents = amountCents; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
