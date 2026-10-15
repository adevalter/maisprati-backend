package com.petshop.repository;

import com.petshop.domain.Appointment;
import com.petshop.domain.AppointmentStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
            select a from Appointment a
            join fetch a.client
            join fetch a.pet
            join fetch a.service
            left join fetch a.professional
            where a.scheduledAt >= :from
              and a.scheduledAt < :to
            order by a.scheduledAt asc
            """)
    List<Appointment> findByRange(
            @Param("from") Instant from,
            @Param("to") Instant to);

    @Query("""
            select a from Appointment a
            join fetch a.client
            join fetch a.pet
            join fetch a.service
            left join fetch a.professional
            where a.id = :id
            """)
    Optional<Appointment> findByIdWithDetails(@Param("id") Long id);

    List<Appointment> findByStatusOrderByScheduledAtAsc(AppointmentStatus status);

    @Query("""
            select a from Appointment a
            join fetch a.client
            join fetch a.pet
            join fetch a.service
            left join fetch a.professional
            where a.status = com.petshop.domain.AppointmentStatus.FINALIZADO
              and a.completedAt is not null
              and a.completedAt >= :from
              and a.completedAt < :to
            order by a.completedAt desc
            """)
    List<Appointment> findFinalizedByCompletedAt(
            @Param("from") Instant from,
            @Param("to") Instant to);
}
