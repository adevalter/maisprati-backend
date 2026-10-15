package com.petshop.repository;

import com.petshop.domain.Professional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
    List<Professional> findByActiveTrueOrderByNameAsc();
    List<Professional> findAllByOrderByNameAsc();
}
