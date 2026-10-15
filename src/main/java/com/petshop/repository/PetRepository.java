package com.petshop.repository;

import com.petshop.domain.Pet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findAllByOrderByNameAsc();

    List<Pet> findByClientIdOrderByNameAsc(Long clientId);

    @Query("""
            select p from Pet p
            join fetch p.client c
            where lower(p.name) like lower(concat('%', :q, '%'))
               or lower(c.name) like lower(concat('%', :q, '%'))
               or lower(coalesce(p.breed, '')) like lower(concat('%', :q, '%'))
            order by p.name asc
            """)
    List<Pet> search(@Param("q") String q);

    @Query("""
            select p from Pet p
            join fetch p.client
            where p.id = :id
            """)
    Optional<Pet> findByIdWithClient(@Param("id") Long id);
}
