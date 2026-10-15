package com.petshop.web;

import com.petshop.service.FinanceReceivableService;
import com.petshop.web.dto.ReceivableItemResponse;
import com.petshop.web.dto.ReceivablePayRequest;
import com.petshop.web.dto.ReceivablesResponse;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/receivables")
public class FinanceReceivableController {

    private final FinanceReceivableService financeReceivableService;

    public FinanceReceivableController(FinanceReceivableService financeReceivableService) {
        this.financeReceivableService = financeReceivableService;
    }

    @GetMapping
    public ReceivablesResponse list(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false, defaultValue = "PENDENTE") String status) {
        return financeReceivableService.list(from, to, status);
    }

    @PatchMapping("/{appointmentId}/pay")
    public ReceivableItemResponse pay(
            @PathVariable Long appointmentId,
            @Valid @RequestBody ReceivablePayRequest request) {
        return financeReceivableService.pay(appointmentId, request);
    }
}
