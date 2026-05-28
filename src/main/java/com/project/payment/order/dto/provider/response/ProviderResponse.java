package com.project.payment.order.dto.provider.response;

import com.project.payment.order.enums.ProviderStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProviderResponse {

    private Long id;
    private String name;
    private String taxId;
    private String email;
    private ProviderStatus status;
}
