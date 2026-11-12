package com.petshop.web;

import com.petshop.service.PetBirthCertificateService;
import com.petshop.service.PetBirthCertificateService.CertificatePdf;
import com.petshop.service.PetService;
import com.petshop.web.dto.PetHistoryResponse;
import com.petshop.web.dto.PetRequest;
import com.petshop.web.dto.PetResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;
    private final PetBirthCertificateService petBirthCertificateService;

    public PetController(
            PetService petService,
            PetBirthCertificateService petBirthCertificateService) {
        this.petService = petService;
        this.petBirthCertificateService = petBirthCertificateService;
    }

    @GetMapping
    public List<PetResponse> list(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) String q) {
        return petService.list(clientId, q);
    }

    @GetMapping("/{id}")
    public PetResponse get(@PathVariable Long id) {
        return petService.get(id);
    }

    @GetMapping("/{id}/history")
    public List<PetHistoryResponse> history(@PathVariable Long id) {
        return petService.history(id);
    }

    @GetMapping("/{id}/birth-certificate")
    public ResponseEntity<byte[]> birthCertificate(@PathVariable Long id) {
        CertificatePdf pdf = petBirthCertificateService.generate(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pdf.filename() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf.content());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetResponse create(@Valid @RequestBody PetRequest request) {
        return petService.create(request);
    }

    @PutMapping("/{id}")
    public PetResponse update(@PathVariable Long id, @Valid @RequestBody PetRequest request) {
        return petService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        petService.delete(id);
    }
}
