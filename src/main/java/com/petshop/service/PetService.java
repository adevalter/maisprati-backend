package com.petshop.service;

import com.petshop.domain.Client;
import com.petshop.domain.Pet;
import com.petshop.repository.ClientRepository;
import com.petshop.repository.PetHistoryRepository;
import com.petshop.repository.PetRepository;
import com.petshop.web.dto.PetHistoryResponse;
import com.petshop.web.dto.PetRequest;
import com.petshop.web.dto.PetResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PetService {

    private static final Set<String> SPECIES = Set.of("CAO", "GATO", "OUTRO");
    private static final Set<String> SEXES = Set.of("MACHO", "FEMEA", "INDEFINIDO");

    private final PetRepository petRepository;
    private final ClientRepository clientRepository;
    private final PetHistoryRepository petHistoryRepository;

    public PetService(
            PetRepository petRepository,
            ClientRepository clientRepository,
            PetHistoryRepository petHistoryRepository) {
        this.petRepository = petRepository;
        this.clientRepository = clientRepository;
        this.petHistoryRepository = petHistoryRepository;
    }

    @Transactional(readOnly = true)
    public List<PetResponse> list(Long clientId, String q) {
        List<Pet> pets;
        if (q != null && !q.isBlank()) {
            pets = petRepository.search(q.trim());
            if (clientId != null) {
                pets = pets.stream().filter(p -> p.getClient().getId().equals(clientId)).toList();
            }
        } else if (clientId != null) {
            pets = petRepository.findByClientIdOrderByNameAsc(clientId);
        } else {
            pets = petRepository.findAllByOrderByNameAsc();
        }
        return pets.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PetResponse get(Long id) {
        return toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public List<PetHistoryResponse> history(Long petId) {
        Pet pet = findById(petId);
        return petHistoryRepository
                .findByPetIdOrderByOccurredAtDesc(pet.getId())
                .stream()
                .map(h -> new PetHistoryResponse(
                        h.getId(),
                        h.getType(),
                        h.getTitle(),
                        h.getBody(),
                        h.getOccurredAt(),
                        h.getNextDueAt(),
                        h.getWeightKg(),
                        h.getProfessional() != null ? h.getProfessional().getName() : null,
                        h.getVaccineProduct() != null ? h.getVaccineProduct().getId() : null,
                        h.getVaccineProduct() != null ? h.getVaccineProduct().getName() : null,
                        h.getDoseNumber()))
                .toList();
    }

    @Transactional
    public PetResponse create(PetRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tutor inválido"));

        Pet pet = new Pet();
        pet.setClient(client);
        apply(pet, request, client);
        petRepository.save(pet);
        return toResponse(pet);
    }

    @Transactional
    public PetResponse update(Long id, PetRequest request) {
        Pet pet = findById(id);
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tutor inválido"));
        pet.setClient(client);
        apply(pet, request, client);
        return toResponse(pet);
    }

    @Transactional
    public void delete(Long id) {
        Pet pet = findById(id);
        petRepository.delete(pet);
    }

    private Pet findById(Long id) {
        return petRepository.findByIdWithClient(id)
                .or(() -> petRepository.findById(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet não encontrado"));
    }

    private void apply(Pet pet, PetRequest request, Client client) {
        pet.setClient(client);
        pet.setName(request.getName().trim());

        String species = request.getSpecies().trim().toUpperCase();
        if (!SPECIES.contains(species)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Espécie inválida (CAO, GATO, OUTRO)");
        }
        pet.setSpecies(species);

        pet.setBreed(blankToNull(request.getBreed()));

        String sex = blankToNull(request.getSex());
        if (sex != null) {
            sex = sex.toUpperCase();
            if (!SEXES.contains(sex)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sexo inválido");
            }
        }
        pet.setSex(sex);

        pet.setBirthDate(request.getBirthDate());

        BigDecimal weight = request.getWeightKg();
        if (weight != null && weight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Peso deve ser maior que zero");
        }
        pet.setWeightKg(weight);

        pet.setAllergies(blankToNull(request.getAllergies()));
        pet.setNotes(blankToNull(request.getNotes()));
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private PetResponse toResponse(Pet pet) {
        return new PetResponse(
                pet.getId(),
                pet.getClient().getId(),
                pet.getClient().getName(),
                pet.getName(),
                pet.getSpecies(),
                pet.getBreed(),
                pet.getSex(),
                pet.getBirthDate(),
                pet.getWeightKg(),
                pet.getAllergies(),
                pet.getNotes(),
                pet.getCreatedAt(),
                pet.getUpdatedAt());
    }
}
