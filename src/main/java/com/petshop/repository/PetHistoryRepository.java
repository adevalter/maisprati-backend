package com.petshop.repository;

import com.petshop.domain.PetHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetHistoryRepository extends JpaRepository<PetHistory, Long> {

    @Query("""
            select h from PetHistory h
            left join fetch h.professional
            left join fetch h.vaccineProduct
            where h.pet.id = :petId
            order by h.occurredAt desc
            """)
    List<PetHistory> findByPetIdOrderByOccurredAtDesc(@Param("petId") Long petId);

    boolean existsByAppointmentId(Long appointmentId);

    @Query("""
            select h from PetHistory h
            join fetch h.pet p
            join fetch p.client
            left join fetch h.professional
            left join fetch h.vaccineProduct
            where h.nextDueAt is not null
              and h.nextDueAt >= :from
              and h.nextDueAt <= :to
            order by h.nextDueAt asc
            """)
    List<PetHistory> findUpcomingDue(
            @Param("from") java.time.LocalDate from,
            @Param("to") java.time.LocalDate to);
}
