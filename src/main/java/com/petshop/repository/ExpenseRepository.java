package com.petshop.repository;

import com.petshop.domain.Expense;
import com.petshop.domain.ExpenseStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("""
            select e from Expense e
            where e.dueDate >= :from
              and e.dueDate <= :to
            order by e.dueDate asc, e.id asc
            """)
    List<Expense> findByDueDateRange(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);

    long countByStatusAndDueDateBefore(ExpenseStatus status, LocalDate date);
}
