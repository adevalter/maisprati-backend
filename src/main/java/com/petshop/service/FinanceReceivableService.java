package com.petshop.service;

import com.petshop.domain.Appointment;
import com.petshop.domain.AppointmentStatus;
import com.petshop.domain.PaymentStatus;
import com.petshop.repository.AppointmentRepository;
import com.petshop.web.dto.ReceivableItemResponse;
import com.petshop.web.dto.ReceivablePayRequest;
import com.petshop.web.dto.ReceivablesResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FinanceReceivableService {

    private static final ZoneId ZONE = ZoneId.of("America/Sao_Paulo");

    private final AppointmentRepository appointmentRepository;

    public FinanceReceivableService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional(readOnly = true)
    public ReceivablesResponse list(LocalDate from, LocalDate to, String statusFilter) {
        LocalDate fromDate = from != null ? from : LocalDate.now(ZONE).withDayOfMonth(1);
        LocalDate toDate = to != null ? to : LocalDate.now(ZONE);
        Instant fromInstant = fromDate.atStartOfDay(ZONE).toInstant();
        Instant toInstant = toDate.plusDays(1).atStartOfDay(ZONE).toInstant();

        List<Appointment> all = appointmentRepository.findFinalizedByCompletedAt(fromInstant, toInstant);

        long pendingCents = 0;
        long paidCents = 0;
        long pendingCount = 0;
        for (Appointment a : all) {
            int amount = a.getAmountCents() != null ? a.getAmountCents() : 0;
            if (a.getPaymentStatus() == PaymentStatus.PENDENTE) {
                pendingCents += amount;
                pendingCount++;
            } else if (a.getPaymentStatus() == PaymentStatus.PAGO) {
                paidCents += amount;
            }
        }

        String filter = statusFilter == null || statusFilter.isBlank() ? "PENDENTE" : statusFilter.trim().toUpperCase();
        List<ReceivableItemResponse> items = all.stream()
                .filter(a -> matchesPaymentFilter(a.getPaymentStatus(), filter))
                .map(this::toItem)
                .toList();

        return new ReceivablesResponse(pendingCents, paidCents, pendingCount, items);
    }

    @Transactional
    public ReceivableItemResponse pay(Long appointmentId, ReceivablePayRequest request) {
        Appointment appointment = appointmentRepository.findByIdWithDetails(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));

        if (appointment.getStatus() != AppointmentStatus.FINALIZADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Somente atendimentos finalizados");
        }
        if (appointment.getPaymentStatus() != PaymentStatus.PENDENTE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pagamento não está pendente");
        }
        if (request.getAmountCents() != null) {
            if (request.getAmountCents() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor inválido");
            }
            appointment.setAmountCents(request.getAmountCents());
        }
        appointment.setPaymentStatus(PaymentStatus.PAGO);
        appointment.setPaymentMethod(request.getPaymentMethod());
        appointment.setPaidAt(Instant.now());
        return toItem(appointment);
    }

    private boolean matchesPaymentFilter(PaymentStatus status, String filter) {
        if ("ALL".equals(filter)) {
            return true;
        }
        if (status == null) {
            return "PENDENTE".equals(filter);
        }
        return status.name().equals(filter);
    }

    private ReceivableItemResponse toItem(Appointment a) {
        return new ReceivableItemResponse(
                a.getId(),
                a.getCompletedAt(),
                a.getClient().getId(),
                a.getClient().getName(),
                a.getPet().getId(),
                a.getPet().getName(),
                a.getService().getName(),
                a.getAmountCents(),
                a.getPaymentStatus(),
                a.getPaymentMethod(),
                a.getPaidAt());
    }
}
