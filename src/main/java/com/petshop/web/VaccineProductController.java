package com.petshop.web;

import com.petshop.service.VaccineProductService;
import com.petshop.web.dto.VaccineProductRequest;
import com.petshop.web.dto.VaccineProductResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/vaccine-products")
public class VaccineProductController {

    private final VaccineProductService vaccineProductService;

    public VaccineProductController(VaccineProductService vaccineProductService) {
        this.vaccineProductService = vaccineProductService;
    }

    @GetMapping
    public List<VaccineProductResponse> list(
            @RequestParam(name = "all", defaultValue = "false") boolean all) {
        return vaccineProductService.list(all);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VaccineProductResponse create(@Valid @RequestBody VaccineProductRequest request) {
        return vaccineProductService.create(request);
    }

    @PutMapping("/{id}")
    public VaccineProductResponse update(
            @PathVariable Long id,
            @RequestBody VaccineProductRequest request) {
        return vaccineProductService.update(id, request);
    }
}
