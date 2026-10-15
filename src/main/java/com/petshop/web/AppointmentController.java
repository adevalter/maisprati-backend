package com.petshop.web;

import com.petshop.service.AppointmentService;
import com.petshop.web.dto.AppointmentRequest;
import com.petshop.web.dto.AppointmentResponse;
import com.petshop.web.dto.AppointmentStatusRequest;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public List<AppointmentResponse> list(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        return appointmentService.listByDay(day);
    }

    @GetMapping("/{id}")
    public AppointmentResponse get(@PathVariable Long id) {
        return appointmentService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse create(@Valid @RequestBody AppointmentRequest request) {
        return appointmentService.create(request);
    }

    @PutMapping("/{id}")
    public AppointmentResponse update(@PathVariable Long id, @Valid @RequestBody AppointmentRequest request) {
        return appointmentService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public AppointmentResponse updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentStatusRequest request) {
        return appointmentService.updateStatus(id, request);
    }
}
