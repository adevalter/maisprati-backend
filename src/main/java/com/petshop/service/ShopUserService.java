package com.petshop.service;

import com.petshop.domain.User;
import com.petshop.domain.UserRole;
import com.petshop.repository.UserRepository;
import com.petshop.web.dto.CreateShopUserRequest;
import com.petshop.web.dto.UserResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ShopUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ShopUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> listUsers() {
        return userRepository.findAllByOrderByNameAsc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public UserResponse createAttendant(CreateShopUserRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.ATENDENTE);
        user.setActive(true);
        userRepository.save(user);
        return toResponse(user);
    }

    @Transactional
    public UserResponse setActive(Long userId, boolean active) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        if (user.getRole() == UserRole.PET_SHOP_ADMIN && !active) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível desativar o admin da loja por aqui");
        }
        user.setActive(active);
        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isActive());
    }
}
