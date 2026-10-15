package com.petshop.web;

import com.petshop.service.ShopUserService;
import com.petshop.web.dto.CreateShopUserRequest;
import com.petshop.web.dto.ShopResponse;
import com.petshop.web.dto.UserResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ShopController {

    private final ShopUserService shopUserService;
    private final String shopName;

    public ShopController(
            ShopUserService shopUserService,
            @Value("${app.shop-name:Pet Shop}") String shopName) {
        this.shopUserService = shopUserService;
        this.shopName = shopName;
    }

    @GetMapping("/shop")
    public ShopResponse shop() {
        return new ShopResponse(shopName);
    }

    @GetMapping("/users")
    public List<UserResponse> listUsers() {
        return shopUserService.listUsers();
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateShopUserRequest request) {
        return shopUserService.createAttendant(request);
    }

    @PatchMapping("/users/{id}/active")
    public UserResponse setActive(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        Boolean active = body.get("active");
        if (active == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Campo active é obrigatório");
        }
        return shopUserService.setActive(id, active);
    }
}
