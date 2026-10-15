package com.petshop.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClientRequest {

    @NotBlank
    @Size(max = 150)
    private String name;

    @Size(max = 30)
    private String phone;

    @Size(max = 30)
    private String mobile;

    @Size(max = 180)
    private String email;

    @Size(max = 20)
    private String document;

    private String address;
    private String notes;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
