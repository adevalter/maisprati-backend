package com.petshop.web.dto;

import java.util.List;

public class ReceivablesResponse {

    private long pendingCents;
    private long paidCents;
    private long pendingCount;
    private List<ReceivableItemResponse> items;

    public ReceivablesResponse(
            long pendingCents,
            long paidCents,
            long pendingCount,
            List<ReceivableItemResponse> items) {
        this.pendingCents = pendingCents;
        this.paidCents = paidCents;
        this.pendingCount = pendingCount;
        this.items = items;
    }

    public long getPendingCents() { return pendingCents; }
    public long getPaidCents() { return paidCents; }
    public long getPendingCount() { return pendingCount; }
    public List<ReceivableItemResponse> getItems() { return items; }
}
