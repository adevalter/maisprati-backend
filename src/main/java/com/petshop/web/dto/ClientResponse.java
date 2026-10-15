package com.petshop.web.dto;

import java.time.Instant;

public class ClientResponse {

    private Long id;
    private String name;
    private String phone;
    private String mobile;
    private String email;
    private String document;
    private String address;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;

    public ClientResponse(
            Long id,
            String name,
            String phone,
            String mobile,
            String email,
            String document,
            String address,
            String notes,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.mobile = mobile;
        this.email = email;
        this.document = document;
        this.address = address;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getMobile() { return mobile; }
    public String getEmail() { return email; }
    public String getDocument() { return document; }
    public String getAddress() { return address; }
    public String getNotes() { return notes; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
