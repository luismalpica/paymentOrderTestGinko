package com.project.payment.order.mapper;

import com.project.payment.order.dto.provider.response.ProviderResponse;
import com.project.payment.order.entity.ProviderEntity;

public class ProviderMapper {
    private ProviderMapper() {
    }

    public static ProviderResponse toResponse(
            ProviderEntity entity
    ) {

        return ProviderResponse.builder()
                .id(entity.getId())
                .name(entity.getBusinessName())
                .taxId(entity.getTaxId())
                .email(entity.getEmail())
                .status(entity.getStatus())
                .build();
    }
}
