package com.petshop.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProfessionalRequest {

    @NotBlank
    @Size(max = 120)
    private String name;

    @Size(max = 30)
    private String type;

    private Boolean active;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
