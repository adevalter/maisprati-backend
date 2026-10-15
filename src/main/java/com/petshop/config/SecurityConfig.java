package com.petshop.config;

import com.petshop.security.JwtAuthFilter;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("PET_SHOP_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/users/**").hasRole("PET_SHOP_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("PET_SHOP_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/services").hasRole("PET_SHOP_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/services/**").hasRole("PET_SHOP_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/professionals").hasRole("PET_SHOP_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/professionals/**").hasRole("PET_SHOP_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/vaccine-products").hasRole("PET_SHOP_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/vaccine-products/**").hasRole("PET_SHOP_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/finance/payables").hasRole("PET_SHOP_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/finance/payables").hasRole("PET_SHOP_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/finance/payables/**").hasRole("PET_SHOP_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/finance/payables/**").hasRole("PET_SHOP_ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://127.0.0.1:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
