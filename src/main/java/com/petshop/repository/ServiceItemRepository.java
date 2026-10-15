package com.petshop.repository;

import com.petshop.domain.ServiceItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {
    List<ServiceItem> findByActiveTrueOrderByNameAsc();
    List<ServiceItem> findAllByOrderByNameAsc();
}
