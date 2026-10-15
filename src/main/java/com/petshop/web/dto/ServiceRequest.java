package com.petshop.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ServiceRequest {

    @NotBlank
    @Size(max = 120)
    private String name;

    @Size(max = 30)
    private String category;

    private Integer durationMin;

    private Integer priceCents;

    private Boolean active;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getDurationMin() { return durationMin; }
    public void setDurationMin(Integer durationMin) { this.durationMin = durationMin; }
    public Integer getPriceCents() { return priceCents; }
    public void setPriceCents(Integer priceCents) { this.priceCents = priceCents; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
