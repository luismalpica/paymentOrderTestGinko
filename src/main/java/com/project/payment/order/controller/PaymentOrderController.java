package com.project.payment.order.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.payment.order.dto.order.request.CreatePaymentOrderRequest;
import com.project.payment.order.dto.order.response.PaymentOrderResponse;
import com.project.payment.order.enums.PaymentOrderStatus;
import com.project.payment.order.service.interfaces.PaymentOrderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class PaymentOrderController {

    private final PaymentOrderService paymentOrderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PaymentOrderResponse> create(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody CreatePaymentOrderRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
        .body(
                paymentOrderService.create(
                        request,
                        idempotencyKey
                )
        );
    }

    @GetMapping
    public ResponseEntity<Page<PaymentOrderResponse>> findAll(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(required = false)
            PaymentOrderStatus status,

            @RequestParam(required = false)
            Long providerId
    ) {

        return ResponseEntity.ok(
                paymentOrderService.findAll(
                        page,
                        size,
                        status,
                        providerId
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentOrderResponse> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                paymentOrderService.findById(id)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentOrderResponse> changeStatus(

            @PathVariable Long id,

            @RequestParam
            PaymentOrderStatus status
    ) {

        return ResponseEntity.ok(
                paymentOrderService.changeStatus(
                        id,
                        status
                )
        );
    }
}
