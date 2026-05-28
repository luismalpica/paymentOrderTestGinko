package com.project.payment.order.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.payment.order.dto.provider.request.CreateProviderRequest;
import com.project.payment.order.dto.provider.response.ProviderResponse;
import com.project.payment.order.enums.ProviderStatus;
import com.project.payment.order.service.interfaces.ProviderService;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProviderResponse> create(
            @Valid @RequestBody
            CreateProviderRequest request
    ) {
        ProviderResponse response = providerService.create(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @GetMapping
    public ResponseEntity<Page<ProviderResponse>> findAll(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(required = false)
            ProviderStatus status
    ) {

        Page<ProviderResponse> response = providerService.findAll(
                page,
                size,
                status
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderResponse> findById(
            @PathVariable Long id
    ) {

        ProviderResponse response = providerService.findById(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody
            CreateProviderRequest request
    ) {

        ProviderResponse response = providerService.update(id, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProviderResponse> changeStatus(
            @PathVariable Long id,
            @RequestParam ProviderStatus status
    ) {

        ProviderResponse response = providerService.changeStatus(
                id,
                status
        );

        return ResponseEntity.ok(response);
    }
}
