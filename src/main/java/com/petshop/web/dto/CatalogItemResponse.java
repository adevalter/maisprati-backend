package com.petshop.web.dto;

public class CatalogItemResponse {

    private Long id;
    private String name;
    private String extra;
    private boolean active;
    private Integer priceCents;

    public CatalogItemResponse(Long id, String name, String extra, boolean active) {
        this(id, name, extra, active, null);
    }

    public CatalogItemResponse(Long id, String name, String extra, boolean active, Integer priceCents) {
        this.id = id;
        this.name = name;
        this.extra = extra;
        this.active = active;
        this.priceCents = priceCents;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getExtra() { return extra; }
    public boolean isActive() { return active; }
    public Integer getPriceCents() { return priceCents; }
}
