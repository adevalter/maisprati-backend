package com.petshop.web;

import com.petshop.service.FinancePayableService;
import com.petshop.web.dto.ExpensePayRequest;
import com.petshop.web.dto.ExpenseRequest;
import com.petshop.web.dto.ExpenseResponse;
import com.petshop.web.dto.PayablesResponse;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/payables")
public class FinancePayableController {

    private final FinancePayableService financePayableService;

    public FinancePayableController(FinancePayableService financePayableService) {
        this.financePayableService = financePayableService;
    }

    @GetMapping
    public PayablesResponse list(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false, defaultValue = "ABERTA") String status) {
        return financePayableService.list(from, to, status);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse create(@Valid @RequestBody ExpenseRequest request) {
        return financePayableService.create(request);
    }

    @PutMapping("/{id}")
    public ExpenseResponse update(@PathVariable Long id, @Valid @RequestBody ExpenseRequest request) {
        return financePayableService.update(id, request);
    }

    @PatchMapping("/{id}/pay")
    public ExpenseResponse pay(@PathVariable Long id, @Valid @RequestBody ExpensePayRequest request) {
        return financePayableService.pay(id, request);
    }

    @PatchMapping("/{id}/cancel")
    public ExpenseResponse cancel(@PathVariable Long id) {
        return financePayableService.cancel(id);
    }
}
