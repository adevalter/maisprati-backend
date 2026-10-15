package com.petshop.repository;

import com.petshop.domain.Client;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findAllByOrderByNameAsc();

    @Query("""
            select c from Client c
            where lower(c.name) like lower(concat('%', :q, '%'))
               or lower(coalesce(c.mobile, '')) like lower(concat('%', :q, '%'))
               or lower(coalesce(c.phone, '')) like lower(concat('%', :q, '%'))
               or lower(coalesce(c.email, '')) like lower(concat('%', :q, '%'))
               or lower(coalesce(c.document, '')) like lower(concat('%', :q, '%'))
            order by c.name asc
            """)
    List<Client> search(@Param("q") String q);
}
