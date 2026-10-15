package com.petshop.service;

import com.petshop.domain.Appointment;
import com.petshop.domain.AppointmentStatus;
import com.petshop.domain.Client;
import com.petshop.domain.PaymentMethod;
import com.petshop.domain.PaymentStatus;
import com.petshop.domain.Pet;
import com.petshop.domain.PetHistory;
import com.petshop.domain.Professional;
import com.petshop.domain.ServiceItem;
import com.petshop.domain.VaccineProduct;
import com.petshop.repository.AppointmentRepository;
import com.petshop.repository.ClientRepository;
import com.petshop.repository.PetHistoryRepository;
import com.petshop.repository.PetRepository;
import com.petshop.repository.ProfessionalRepository;
import com.petshop.repository.ServiceItemRepository;
import com.petshop.repository.VaccineProductRepository;
import com.petshop.web.dto.AppointmentRequest;
import com.petshop.web.dto.AppointmentResponse;
import com.petshop.web.dto.AppointmentStatusRequest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AppointmentService {

    private static final ZoneId ZONE = ZoneId.of("America/Sao_Paulo");

    private final AppointmentRepository appointmentRepository;
    private final ClientRepository clientRepository;
    private final PetRepository petRepository;
    private final ServiceItemRepository serviceItemRepository;
    private final ProfessionalRepository professionalRepository;
    private final PetHistoryRepository petHistoryRepository;
    private final VaccineProductRepository vaccineProductRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            ClientRepository clientRepository,
            PetRepository petRepository,
            ServiceItemRepository serviceItemRepository,
            ProfessionalRepository professionalRepository,
            PetHistoryRepository petHistoryRepository,
            VaccineProductRepository vaccineProductRepository) {
        this.appointmentRepository = appointmentRepository;
        this.clientRepository = clientRepository;
        this.petRepository = petRepository;
        this.serviceItemRepository = serviceItemRepository;
        this.professionalRepository = professionalRepository;
        this.petHistoryRepository = petHistoryRepository;
        this.vaccineProductRepository = vaccineProductRepository;
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> listByDay(LocalDate day) {
        LocalDate date = day != null ? day : LocalDate.now(ZONE);
        Instant from = date.atStartOfDay(ZONE).toInstant();
        Instant to = date.plusDays(1).atStartOfDay(ZONE).toInstant();
        return appointmentRepository.findByRange(from, to).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AppointmentResponse get(Long id) {
        return toResponse(findById(id));
    }

    @Transactional
    public AppointmentResponse create(AppointmentRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tutor inválido"));
        Pet pet = petRepository.findById(request.getPetId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet inválido"));
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet não pertence ao tutor informado");
        }
        ServiceItem service = serviceItemRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Serviço inválido"));

        Professional professional = null;
        if (request.getProfessionalId() != null) {
            professional = professionalRepository.findById(request.getProfessionalId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profissional inválido"));
        }

        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setPet(pet);
        appointment.setService(service);
        appointment.setProfessional(professional);
        appointment.setScheduledAt(request.getScheduledAt());
        appointment.setStatus(AppointmentStatus.AGENDADO);
        appointment.setHomeVisit(request.isHomeVisit());
        appointment.setAddressOverride(blankToNull(request.getAddressOverride()));
        appointment.setNotes(blankToNull(request.getNotes()));
        applyPriceSnapshot(appointment, service);
        appointmentRepository.save(appointment);
        return toResponse(appointment);
    }

    @Transactional
    public AppointmentResponse update(Long id, AppointmentRequest request) {
        Appointment appointment = findById(id);
        if (appointment.getStatus() == AppointmentStatus.FINALIZADO
                || appointment.getStatus() == AppointmentStatus.CANCELADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agendamento encerrado não pode ser editado");
        }

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tutor inválido"));
        Pet pet = petRepository.findById(request.getPetId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet inválido"));
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet não pertence ao tutor informado");
        }
        ServiceItem service = serviceItemRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Serviço inválido"));

        Professional professional = null;
        if (request.getProfessionalId() != null) {
            professional = professionalRepository.findById(request.getProfessionalId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profissional inválido"));
        }

        appointment.setClient(client);
        appointment.setPet(pet);
        appointment.setService(service);
        appointment.setProfessional(professional);
        appointment.setScheduledAt(request.getScheduledAt());
        appointment.setHomeVisit(request.isHomeVisit());
        appointment.setAddressOverride(blankToNull(request.getAddressOverride()));
        appointment.setNotes(blankToNull(request.getNotes()));
        applyPriceSnapshot(appointment, service);
        return toResponse(appointment);
    }

    @Transactional
    public AppointmentResponse updateStatus(Long id, AppointmentStatusRequest request) {
        Appointment appointment = findById(id);
        AppointmentStatus current = appointment.getStatus();
        AppointmentStatus next = request.getStatus();

        if (current == AppointmentStatus.FINALIZADO || current == AppointmentStatus.CANCELADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agendamento já encerrado");
        }
        if (next == current) {
            return toResponse(appointment);
        }

        validateTransition(current, next);

        if (request.getNotes() != null && !request.getNotes().isBlank()) {
            appointment.setNotes(request.getNotes().trim());
        }

        if (next == AppointmentStatus.EM_ATENDIMENTO && appointment.getCheckInAt() == null) {
            appointment.setCheckInAt(Instant.now());
        }

        if (next == AppointmentStatus.FINALIZADO) {
            applyPaymentOnFinalize(appointment, request);
            appointment.setCompletedAt(Instant.now());
            createHistory(appointment, request);
        }

        appointment.setStatus(next);
        return toResponse(appointment);
    }

    private void applyPriceSnapshot(Appointment appointment, ServiceItem service) {
        int price = service.getPriceCents();
        appointment.setServicePriceCents(price);
        appointment.setAmountCents(price);
    }

    private void applyPaymentOnFinalize(Appointment appointment, AppointmentStatusRequest request) {
        PaymentStatus status = request.getPaymentStatus();
        if (status == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Informe o status do pagamento (PAGO, PENDENTE ou ISENTO)");
        }

        if (request.getAmountCents() != null) {
            if (request.getAmountCents() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor não pode ser negativo");
            }
            appointment.setAmountCents(request.getAmountCents());
        } else if (appointment.getAmountCents() == null) {
            appointment.setAmountCents(
                    appointment.getServicePriceCents() != null
                            ? appointment.getServicePriceCents()
                            : appointment.getService().getPriceCents());
        }

        appointment.setPaymentStatus(status);
        if (status == PaymentStatus.PAGO) {
            PaymentMethod method = request.getPaymentMethod();
            if (method == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Informe a forma de pagamento");
            }
            appointment.setPaymentMethod(method);
            appointment.setPaidAt(Instant.now());
        } else {
            appointment.setPaymentMethod(null);
            appointment.setPaidAt(null);
            if (status == PaymentStatus.ISENTO) {
                appointment.setAmountCents(0);
            }
        }
    }

    private void validateTransition(AppointmentStatus current, AppointmentStatus next) {
        boolean ok = switch (current) {
            case AGENDADO -> next == AppointmentStatus.CONFIRMADO
                    || next == AppointmentStatus.EM_ATENDIMENTO
                    || next == AppointmentStatus.CANCELADO;
            case CONFIRMADO -> next == AppointmentStatus.EM_ATENDIMENTO
                    || next == AppointmentStatus.CANCELADO;
            case EM_ATENDIMENTO -> next == AppointmentStatus.FINALIZADO
                    || next == AppointmentStatus.CANCELADO;
            default -> false;
        };
        if (!ok) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Transição inválida: " + current + " → " + next);
        }
    }

    private void createHistory(Appointment appointment, AppointmentStatusRequest request) {
        if (petHistoryRepository.existsByAppointmentId(appointment.getId())) {
            return;
        }
        String category = appointment.getService().getCategory();
        String type = switch (category) {
            case "BANHO", "TOSA", "CONSULTA", "VACINA", "VERMIFUGO", "DOMICILIAR" -> category;
            default -> "OUTRO";
        };

        VaccineProduct vaccineProduct = null;
        Integer doseNumber = request.getDoseNumber();
        LocalDate nextDueAt = request.getNextDueAt();
        String title = appointment.getService().getName();

        if ("VACINA".equals(category)) {
            if (request.getVaccineProductId() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Selecione a vacina aplicada");
            }
            vaccineProduct = vaccineProductRepository
                    .findById(request.getVaccineProductId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Vacina inválida"));
            if (!vaccineProduct.isActive()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vacina inativa");
            }
            title = buildVaccineTitle(vaccineProduct.getName(), doseNumber);
            if (nextDueAt == null) {
                nextDueAt = LocalDate.now(ZONE).plusDays(vaccineProduct.getDefaultIntervalDays());
            }
        }

        if (nextDueAt != null && nextDueAt.isBefore(LocalDate.now(ZONE))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Próxima dose/retorno deve ser hoje ou uma data futura");
        }

        PetHistory history = new PetHistory();
        history.setPet(appointment.getPet());
        history.setAppointment(appointment);
        history.setProfessional(appointment.getProfessional());
        history.setType(type);
        history.setTitle(title);
        history.setBody(appointment.getNotes());
        history.setOccurredAt(appointment.getCompletedAt() != null ? appointment.getCompletedAt() : Instant.now());
        history.setNextDueAt(nextDueAt);
        history.setVaccineProduct(vaccineProduct);
        history.setDoseNumber(doseNumber);
        history.setWeightKg(appointment.getPet().getWeightKg());
        petHistoryRepository.save(history);
    }

    private String buildVaccineTitle(String productName, Integer doseNumber) {
        if (doseNumber == null) {
            return productName;
        }
        if (doseNumber == 0) {
            return productName + " — reforço";
        }
        return productName + " — dose " + doseNumber;
    }

    private Appointment findById(Long id) {
        return appointmentRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    public AppointmentResponse toResponsePublic(Appointment a) {
        return toResponse(a);
    }

    private AppointmentResponse toResponse(Appointment a) {
        return new AppointmentResponse(
                a.getId(),
                a.getClient().getId(),
                a.getClient().getName(),
                a.getPet().getId(),
                a.getPet().getName(),
                a.getService().getId(),
                a.getService().getName(),
                a.getService().getCategory(),
                a.getProfessional() != null ? a.getProfessional().getId() : null,
                a.getProfessional() != null ? a.getProfessional().getName() : null,
                a.getScheduledAt(),
                a.getStatus(),
                a.isHomeVisit(),
                a.getAddressOverride(),
                a.getNotes(),
                a.getCheckInAt(),
                a.getCompletedAt(),
                a.getServicePriceCents(),
                a.getAmountCents(),
                a.getPaymentStatus(),
                a.getPaymentMethod(),
                a.getPaidAt());
    }
}
