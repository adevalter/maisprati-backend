package com.petshop.web.dto;

public class ServiceResponse {

    private Long id;
    private String name;
    private String category;
    private Integer durationMin;
    private int priceCents;
    private boolean active;

    public ServiceResponse(
            Long id, String name, String category, Integer durationMin, int priceCents, boolean active) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.durationMin = durationMin;
        this.priceCents = priceCents;
        this.active = active;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public Integer getDurationMin() { return durationMin; }
    public int getPriceCents() { return priceCents; }
    public boolean isActive() { return active; }
}
