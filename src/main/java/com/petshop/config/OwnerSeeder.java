package com.petshop.config;

import com.petshop.domain.User;
import com.petshop.domain.UserRole;
import com.petshop.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class OwnerSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(OwnerSeeder.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String ownerEmail;
    private final String ownerPassword;
    private final String ownerName;
    private final String attendantEmail;
    private final String attendantPassword;
    private final String attendantName;

    public OwnerSeeder(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.owner.email:dono@petshop.local}") String ownerEmail,
            @Value("${app.owner.password:dono123}") String ownerPassword,
            @Value("${app.owner.name:Dono}") String ownerName,
            @Value("${app.attendant.email:}") String attendantEmail,
            @Value("${app.attendant.password:}") String attendantPassword,
            @Value("${app.attendant.name:Atendente}") String attendantName) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.ownerEmail = ownerEmail;
        this.ownerPassword = ownerPassword;
        this.ownerName = ownerName;
        this.attendantEmail = attendantEmail;
        this.attendantPassword = attendantPassword;
        this.attendantName = attendantName;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!userRepository.existsByRole(UserRole.PET_SHOP_ADMIN)) {
            User owner = new User();
            owner.setName(ownerName.trim());
            owner.setEmail(ownerEmail.trim().toLowerCase());
            owner.setPasswordHash(passwordEncoder.encode(ownerPassword));
            owner.setRole(UserRole.PET_SHOP_ADMIN);
            owner.setActive(true);
            userRepository.save(owner);
            log.info("Owner PET_SHOP_ADMIN criado: {}", owner.getEmail());
        }

        if (StringUtils.hasText(attendantEmail)
                && StringUtils.hasText(attendantPassword)
                && !userRepository.existsByEmailIgnoreCase(attendantEmail.trim())) {
            User attendant = new User();
            attendant.setName(attendantName.trim());
            attendant.setEmail(attendantEmail.trim().toLowerCase());
            attendant.setPasswordHash(passwordEncoder.encode(attendantPassword));
            attendant.setRole(UserRole.ATENDENTE);
            attendant.setActive(true);
            userRepository.save(attendant);
            log.info("Atendente criado: {}", attendant.getEmail());
        }
    }
}
