package com.petshop.web.dto;

import com.petshop.domain.UserRole;

public class AuthResponse {

    private String token;
    private Long id;
    private String name;
    private String email;
    private UserRole role;
    private String shopName;

    public AuthResponse(
            String token,
            Long id,
            String name,
            String email,
            UserRole role,
            String shopName) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.shopName = shopName;
    }

    public String getToken() { return token; }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public UserRole getRole() { return role; }
    public String getShopName() { return shopName; }
}
