package com.petshop.web.dto;

import com.petshop.domain.UserRole;

public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private UserRole role;
    private boolean active;

    public UserResponse(Long id, String name, String email, UserRole role, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.active = active;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public UserRole getRole() { return role; }
    public boolean isActive() { return active; }
}
