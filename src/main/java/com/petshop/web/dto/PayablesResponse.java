package com.petshop.web.dto;

import java.util.List;

public class PayablesResponse {

    private long openCents;
    private long paidCents;
    private long overdueCount;
    private List<ExpenseResponse> items;

    public PayablesResponse(
            long openCents,
            long paidCents,
            long overdueCount,
            List<ExpenseResponse> items) {
        this.openCents = openCents;
        this.paidCents = paidCents;
        this.overdueCount = overdueCount;
        this.items = items;
    }

    public long getOpenCents() { return openCents; }
    public long getPaidCents() { return paidCents; }
    public long getOverdueCount() { return overdueCount; }
    public List<ExpenseResponse> getItems() { return items; }
}
