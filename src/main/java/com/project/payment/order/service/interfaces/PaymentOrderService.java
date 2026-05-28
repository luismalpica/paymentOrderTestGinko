package com.project.payment.order.service.interfaces;

import org.springframework.data.domain.Page;

import com.project.payment.order.dto.order.request.CreatePaymentOrderRequest;
import com.project.payment.order.dto.order.response.PaymentOrderResponse;
import com.project.payment.order.enums.PaymentOrderStatus;

public interface PaymentOrderService {

    PaymentOrderResponse create(CreatePaymentOrderRequest request, String idempotencyKey);

    Page<PaymentOrderResponse> findAll(
            int page,
            int size,
            PaymentOrderStatus status,
            Long providerId
    );

    PaymentOrderResponse findById(Long id);

    PaymentOrderResponse changeStatus(
            Long id,
            PaymentOrderStatus status
    );
}
