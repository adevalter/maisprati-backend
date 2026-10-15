package com.petshop.service;

import com.petshop.domain.VaccineProduct;
import com.petshop.repository.VaccineProductRepository;
import com.petshop.web.dto.VaccineProductRequest;
import com.petshop.web.dto.VaccineProductResponse;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VaccineProductService {

    private static final Set<String> SPECIES = Set.of("CAO", "GATO", "TODOS");

    private final VaccineProductRepository vaccineProductRepository;

    public VaccineProductService(VaccineProductRepository vaccineProductRepository) {
        this.vaccineProductRepository = vaccineProductRepository;
    }

    @Transactional(readOnly = true)
    public List<VaccineProductResponse> list(boolean includeInactive) {
        var list = includeInactive
                ? vaccineProductRepository.findAllByOrderByNameAsc()
                : vaccineProductRepository.findByActiveTrueOrderByNameAsc();
        return list.stream().map(this::toResponse).toList();
    }

    @Transactional
    public VaccineProductResponse create(VaccineProductRequest request) {
        String name = request.getName().trim();
        if (vaccineProductRepository.existsByNameIgnoreCase(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe vacina com este nome");
        }

        VaccineProduct product = new VaccineProduct();
        product.setName(name);
        product.setSpecies(normalizeSpecies(request.getSpecies()));
        product.setDefaultIntervalDays(
                request.getDefaultIntervalDays() != null ? request.getDefaultIntervalDays() : 365);
        product.setRecommendedDoses(
                request.getRecommendedDoses() != null ? request.getRecommendedDoses() : 1);
        product.setActive(request.getActive() == null || request.getActive());
        vaccineProductRepository.save(product);
        return toResponse(product);
    }

    @Transactional
    public VaccineProductResponse update(Long id, VaccineProductRequest request) {
        VaccineProduct product = vaccineProductRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vacina não encontrada"));

        if (request.getName() != null && !request.getName().isBlank()) {
            product.setName(request.getName().trim());
        }
        if (request.getSpecies() != null) {
            product.setSpecies(normalizeSpecies(request.getSpecies()));
        }
        if (request.getDefaultIntervalDays() != null) {
            product.setDefaultIntervalDays(request.getDefaultIntervalDays());
        }
        if (request.getRecommendedDoses() != null) {
            product.setRecommendedDoses(request.getRecommendedDoses());
        }
        if (request.getActive() != null) {
            product.setActive(request.getActive());
        }
        return toResponse(product);
    }

    public VaccineProductResponse toResponse(VaccineProduct p) {
        return new VaccineProductResponse(
                p.getId(),
                p.getName(),
                p.getSpecies(),
                p.getDefaultIntervalDays(),
                p.getRecommendedDoses(),
                p.isActive());
    }

    private String normalizeSpecies(String species) {
        if (species == null || species.isBlank()) {
            return "TODOS";
        }
        String value = species.trim().toUpperCase();
        if (!SPECIES.contains(value)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Espécie inválida");
        }
        return value;
    }
}
