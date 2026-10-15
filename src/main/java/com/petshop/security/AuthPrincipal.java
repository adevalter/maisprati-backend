package com.petshop.security;

public record AuthPrincipal(
        Long userId,
        String email,
        String role
) {
    public boolean isPetShopAdmin() {
        return "PET_SHOP_ADMIN".equals(role);
    }
}
