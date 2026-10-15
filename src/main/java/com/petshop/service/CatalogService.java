package com.petshop.service;

import com.petshop.domain.Professional;
import com.petshop.domain.ServiceItem;
import com.petshop.repository.ProfessionalRepository;
import com.petshop.repository.ServiceItemRepository;
import com.petshop.web.dto.CatalogItemResponse;
import com.petshop.web.dto.ProfessionalRequest;
import com.petshop.web.dto.ProfessionalResponse;
import com.petshop.web.dto.ServiceRequest;
import com.petshop.web.dto.ServiceResponse;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CatalogService {

    private static final Set<String> SERVICE_CATEGORIES = Set.of(
            "BANHO", "TOSA", "CONSULTA", "VACINA", "VERMIFUGO", "DOMICILIAR", "OUTRO");
    private static final Set<String> PROFESSIONAL_TYPES = Set.of("BANHO_TOSA", "VETERINARIO", "OUTRO");

    private final ServiceItemRepository serviceItemRepository;
    private final ProfessionalRepository professionalRepository;

    public CatalogService(
            ServiceItemRepository serviceItemRepository,
            ProfessionalRepository professionalRepository) {
        this.serviceItemRepository = serviceItemRepository;
        this.professionalRepository = professionalRepository;
    }

    @Transactional(readOnly = true)
    public List<CatalogItemResponse> listServices() {
        return serviceItemRepository.findByActiveTrueOrderByNameAsc().stream()
                .map(s -> new CatalogItemResponse(
                        s.getId(), s.getName(), s.getCategory(), s.isActive(), s.getPriceCents()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ServiceResponse> listServicesAdmin(boolean includeInactive) {
        var list = includeInactive
                ? serviceItemRepository.findAllByOrderByNameAsc()
                : serviceItemRepository.findByActiveTrueOrderByNameAsc();
        return list.stream().map(this::toServiceResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<CatalogItemResponse> listProfessionals() {
        return professionalRepository.findByActiveTrueOrderByNameAsc().stream()
                .map(p -> new CatalogItemResponse(p.getId(), p.getName(), p.getType(), p.isActive()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProfessionalResponse> listProfessionalsAdmin(boolean includeInactive) {
        var list = includeInactive
                ? professionalRepository.findAllByOrderByNameAsc()
                : professionalRepository.findByActiveTrueOrderByNameAsc();
        return list.stream()
                .map(p -> new ProfessionalResponse(p.getId(), p.getName(), p.getType(), p.isActive()))
                .toList();
    }

    @Transactional
    public ServiceResponse createService(ServiceRequest request) {
        ServiceItem item = new ServiceItem();
        item.setName(request.getName().trim());
        item.setCategory(normalizeCategory(request.getCategory()));
        item.setDurationMin(request.getDurationMin());
        item.setPriceCents(normalizePrice(request.getPriceCents()));
        item.setActive(request.getActive() == null || request.getActive());
        serviceItemRepository.save(item);
        return toServiceResponse(item);
    }

    @Transactional
    public ServiceResponse updateService(Long id, ServiceRequest request) {
        ServiceItem item = serviceItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));

        if (request.getName() != null && !request.getName().isBlank()) {
            item.setName(request.getName().trim());
        }
        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            item.setCategory(normalizeCategory(request.getCategory()));
        }
        if (request.getDurationMin() != null) {
            item.setDurationMin(request.getDurationMin());
        }
        if (request.getPriceCents() != null) {
            item.setPriceCents(normalizePrice(request.getPriceCents()));
        }
        if (request.getActive() != null) {
            item.setActive(request.getActive());
        }
        return toServiceResponse(item);
    }

    @Transactional
    public ProfessionalResponse createProfessional(ProfessionalRequest request) {
        Professional professional = new Professional();
        professional.setName(request.getName().trim());
        professional.setType(normalizeProfessionalType(request.getType()));
        professional.setActive(request.getActive() == null || request.getActive());
        professionalRepository.save(professional);
        return new ProfessionalResponse(
                professional.getId(), professional.getName(), professional.getType(), professional.isActive());
    }

    @Transactional
    public ProfessionalResponse updateProfessional(Long id, ProfessionalRequest request) {
        Professional professional = professionalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissional não encontrado"));

        if (request.getName() != null && !request.getName().isBlank()) {
            professional.setName(request.getName().trim());
        }
        if (request.getType() != null && !request.getType().isBlank()) {
            professional.setType(normalizeProfessionalType(request.getType()));
        }
        if (request.getActive() != null) {
            professional.setActive(request.getActive());
        }
        return new ProfessionalResponse(
                professional.getId(), professional.getName(), professional.getType(), professional.isActive());
    }

    private ServiceResponse toServiceResponse(ServiceItem item) {
        return new ServiceResponse(
                item.getId(),
                item.getName(),
                item.getCategory(),
                item.getDurationMin(),
                item.getPriceCents(),
                item.isActive());
    }

    private int normalizePrice(Integer priceCents) {
        int value = priceCents == null ? 0 : priceCents;
        if (value < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Preço não pode ser negativo");
        }
        return value;
    }

    private String normalizeCategory(String category) {
        String value = category == null || category.isBlank() ? "OUTRO" : category.trim().toUpperCase();
        if (!SERVICE_CATEGORIES.contains(value)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria inválida");
        }
        return value;
    }

    private String normalizeProfessionalType(String type) {
        String value = type == null || type.isBlank() ? "OUTRO" : type.trim().toUpperCase();
        if (!PROFESSIONAL_TYPES.contains(value)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de profissional inválido");
        }
        return value;
    }
}
