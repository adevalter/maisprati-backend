package com.petshop.web.dto;

public class ProfessionalResponse {

    private Long id;
    private String name;
    private String type;
    private boolean active;

    public ProfessionalResponse(Long id, String name, String type, boolean active) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.active = active;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public boolean isActive() { return active; }
}
