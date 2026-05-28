package com.project.payment.order.mapper;

import com.project.payment.order.dto.order.response.PaymentOrderResponse;
import com.project.payment.order.entity.PaymentOrderEntity;

public class PaymentOrderMapper {
    private PaymentOrderMapper() {
    }

    public static PaymentOrderResponse toResponse(
            PaymentOrderEntity entity
    ) {

        return PaymentOrderResponse.builder()
                .id(entity.getId())
                .providerId(entity.getProvider().getId())
                .providerName(entity.getProvider().getBusinessName())
                .amount(entity.getAmount())
                .concept(entity.getConcept())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
