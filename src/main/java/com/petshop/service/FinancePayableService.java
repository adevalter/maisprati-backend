package com.petshop.service;

import com.petshop.domain.Expense;
import com.petshop.domain.ExpenseStatus;
import com.petshop.repository.ExpenseRepository;
import com.petshop.web.dto.ExpensePayRequest;
import com.petshop.web.dto.ExpenseRequest;
import com.petshop.web.dto.ExpenseResponse;
import com.petshop.web.dto.PayablesResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FinancePayableService {

    private static final ZoneId ZONE = ZoneId.of("America/Sao_Paulo");

    private final ExpenseRepository expenseRepository;

    public FinancePayableService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Transactional(readOnly = true)
    public PayablesResponse list(LocalDate from, LocalDate to, String statusFilter) {
        LocalDate fromDate = from != null ? from : LocalDate.now(ZONE).withDayOfMonth(1);
        LocalDate toDate = to != null ? to : LocalDate.now(ZONE);
        LocalDate today = LocalDate.now(ZONE);

        List<Expense> all = expenseRepository.findByDueDateRange(fromDate, toDate);

        long openCents = 0;
        long paidCents = 0;
        for (Expense e : all) {
            if (e.getStatus() == ExpenseStatus.ABERTA) {
                openCents += e.getAmountCents();
            } else if (e.getStatus() == ExpenseStatus.PAGA) {
                paidCents += e.getAmountCents();
            }
        }

        long overdueCount = expenseRepository.countByStatusAndDueDateBefore(ExpenseStatus.ABERTA, today);

        String filter = statusFilter == null || statusFilter.isBlank() ? "ABERTA" : statusFilter.trim().toUpperCase();
        List<ExpenseResponse> items = all.stream()
                .filter(e -> matchesStatus(e.getStatus(), filter))
                .map(e -> toResponse(e, today))
                .toList();

        return new PayablesResponse(openCents, paidCents, overdueCount, items);
    }

    @Transactional
    public ExpenseResponse create(ExpenseRequest request) {
        Expense expense = new Expense();
        apply(expense, request);
        expense.setStatus(ExpenseStatus.ABERTA);
        expenseRepository.save(expense);
        return toResponse(expense, LocalDate.now(ZONE));
    }

    @Transactional
    public ExpenseResponse update(Long id, ExpenseRequest request) {
        Expense expense = findOpen(id);
        apply(expense, request);
        return toResponse(expense, LocalDate.now(ZONE));
    }

    @Transactional
    public ExpenseResponse pay(Long id, ExpensePayRequest request) {
        Expense expense = findOpen(id);
        expense.setStatus(ExpenseStatus.PAGA);
        expense.setPaymentMethod(request.getPaymentMethod());
        expense.setPaidAt(Instant.now());
        return toResponse(expense, LocalDate.now(ZONE));
    }

    @Transactional
    public ExpenseResponse cancel(Long id) {
        Expense expense = findOpen(id);
        expense.setStatus(ExpenseStatus.CANCELADA);
        expense.setPaymentMethod(null);
        expense.setPaidAt(null);
        return toResponse(expense, LocalDate.now(ZONE));
    }

    private Expense findOpen(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
        if (expense.getStatus() != ExpenseStatus.ABERTA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Somente contas abertas podem ser alteradas");
        }
        return expense;
    }

    private void apply(Expense expense, ExpenseRequest request) {
        expense.setDescription(request.getDescription().trim());
        expense.setCategory(request.getCategory());
        expense.setAmountCents(request.getAmountCents());
        expense.setDueDate(request.getDueDate());
        expense.setNotes(blankToNull(request.getNotes()));
    }

    private boolean matchesStatus(ExpenseStatus status, String filter) {
        if ("ALL".equals(filter)) {
            return true;
        }
        return status.name().equals(filter);
    }

    private ExpenseResponse toResponse(Expense e, LocalDate today) {
        boolean overdue = e.getStatus() == ExpenseStatus.ABERTA && e.getDueDate().isBefore(today);
        return new ExpenseResponse(
                e.getId(),
                e.getDescription(),
                e.getCategory(),
                e.getAmountCents(),
                e.getDueDate(),
                e.getStatus(),
                e.getPaymentMethod(),
                e.getPaidAt(),
                e.getNotes(),
                overdue);
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
