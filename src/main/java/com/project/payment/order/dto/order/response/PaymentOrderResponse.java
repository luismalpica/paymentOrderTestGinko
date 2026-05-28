package com.project.payment.order.dto.order.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.payment.order.enums.PaymentOrderStatus;

@Data
@Builder
public class PaymentOrderResponse {

    private Long id;
    private Long providerId;
    private String providerName;
    private BigDecimal amount;
    private String concept;
    private PaymentOrderStatus status;
    private LocalDateTime createdAt;
}
