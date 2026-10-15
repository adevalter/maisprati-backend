package com.petshop.service;

import com.petshop.domain.Appointment;
import com.petshop.domain.AppointmentStatus;
import com.petshop.domain.PaymentStatus;
import com.petshop.domain.PetHistory;
import com.petshop.repository.AppointmentRepository;
import com.petshop.repository.PetHistoryRepository;
import com.petshop.web.dto.AppointmentResponse;
import com.petshop.web.dto.DashboardResponse;
import com.petshop.web.dto.UpcomingItemResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private static final ZoneId ZONE = ZoneId.of("America/Sao_Paulo");

    private final AppointmentRepository appointmentRepository;
    private final PetHistoryRepository petHistoryRepository;
    private final AppointmentService appointmentService;

    public DashboardService(
            AppointmentRepository appointmentRepository,
            PetHistoryRepository petHistoryRepository,
            AppointmentService appointmentService) {
        this.appointmentRepository = appointmentRepository;
        this.petHistoryRepository = petHistoryRepository;
        this.appointmentService = appointmentService;
    }

    @Transactional(readOnly = true)
    public DashboardResponse today() {
        LocalDate day = LocalDate.now(ZONE);
        Instant from = day.atStartOfDay(ZONE).toInstant();
        Instant to = day.plusDays(1).atStartOfDay(ZONE).toInstant();

        List<Appointment> today = appointmentRepository.findByRange(from, to);
        List<AppointmentResponse> todayResponses = today.stream()
                .map(appointmentService::toResponsePublic)
                .toList();

        List<AppointmentResponse> inProgress = today.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.EM_ATENDIMENTO)
                .map(appointmentService::toResponsePublic)
                .toList();

        long waiting = today.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.AGENDADO
                        || a.getStatus() == AppointmentStatus.CONFIRMADO)
                .count();

        long homeVisits = today.stream()
                .filter(Appointment::isHomeVisit)
                .filter(a -> a.getStatus() != AppointmentStatus.CANCELADO)
                .count();

        long paidTodayCents = today.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.FINALIZADO)
                .filter(a -> a.getPaymentStatus() == PaymentStatus.PAGO)
                .mapToLong(a -> a.getAmountCents() != null ? a.getAmountCents() : 0)
                .sum();

        long pendingPaymentCount = today.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.FINALIZADO)
                .filter(a -> a.getPaymentStatus() == PaymentStatus.PENDENTE)
                .count();

        LocalDate dueFrom = day;
        LocalDate dueTo = day.plusDays(14);
        List<UpcomingItemResponse> upcoming = petHistoryRepository
                .findUpcomingDue(dueFrom, dueTo)
                .stream()
                .map(this::toUpcoming)
                .toList();

        return new DashboardResponse(
                todayResponses.size(),
                inProgress.size(),
                waiting,
                homeVisits,
                paidTodayCents,
                pendingPaymentCount,
                todayResponses,
                inProgress,
                upcoming);
    }

    private UpcomingItemResponse toUpcoming(PetHistory h) {
        return new UpcomingItemResponse(
                h.getId(),
                h.getPet().getId(),
                h.getPet().getName(),
                h.getPet().getClient().getId(),
                h.getPet().getClient().getName(),
                h.getType(),
                h.getTitle(),
                h.getNextDueAt(),
                h.getDoseNumber());
    }
}
