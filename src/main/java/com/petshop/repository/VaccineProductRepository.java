package com.petshop.repository;

import com.petshop.domain.VaccineProduct;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccineProductRepository extends JpaRepository<VaccineProduct, Long> {
    List<VaccineProduct> findByActiveTrueOrderByNameAsc();
    List<VaccineProduct> findAllByOrderByNameAsc();
    boolean existsByNameIgnoreCase(String name);
}
