package com.petshop.repository;

import com.petshop.domain.User;
import com.petshop.domain.UserRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where lower(u.email) = lower(:email)")
    Optional<User> findByEmailIgnoreCase(@Param("email") String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByRole(UserRole role);

    List<User> findAllByOrderByNameAsc();
}
