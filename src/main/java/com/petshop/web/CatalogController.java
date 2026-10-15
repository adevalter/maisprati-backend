package com.petshop.web;

import com.petshop.service.CatalogService;
import com.petshop.web.dto.CatalogItemResponse;
import com.petshop.web.dto.ProfessionalRequest;
import com.petshop.web.dto.ProfessionalResponse;
import com.petshop.web.dto.ServiceRequest;
import com.petshop.web.dto.ServiceResponse;
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
@RequestMapping("/api")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/services")
    public Object services(@RequestParam(name = "all", defaultValue = "false") boolean all) {
        if (all) {
            return catalogService.listServicesAdmin(true);
        }
        return catalogService.listServices();
    }

    @PostMapping("/services")
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse createService(@Valid @RequestBody ServiceRequest request) {
        return catalogService.createService(request);
    }

    @PutMapping("/services/{id}")
    public ServiceResponse updateService(@PathVariable Long id, @RequestBody ServiceRequest request) {
        return catalogService.updateService(id, request);
    }

    @GetMapping("/professionals")
    public Object professionals(@RequestParam(name = "all", defaultValue = "false") boolean all) {
        if (all) {
            return catalogService.listProfessionalsAdmin(true);
        }
        return catalogService.listProfessionals();
    }

    @PostMapping("/professionals")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfessionalResponse createProfessional(@Valid @RequestBody ProfessionalRequest request) {
        return catalogService.createProfessional(request);
    }

    @PutMapping("/professionals/{id}")
    public ProfessionalResponse updateProfessional(
            @PathVariable Long id,
            @RequestBody ProfessionalRequest request) {
        return catalogService.updateProfessional(id, request);
    }
}
