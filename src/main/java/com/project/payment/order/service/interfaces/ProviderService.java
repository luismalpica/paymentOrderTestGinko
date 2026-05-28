package com.project.payment.order.service.interfaces;

import org.springframework.data.domain.Page;

import com.project.payment.order.dto.provider.request.CreateProviderRequest;
import com.project.payment.order.dto.provider.response.ProviderResponse;
import com.project.payment.order.enums.ProviderStatus;

public interface ProviderService {

    ProviderResponse create(CreateProviderRequest request);

    Page<ProviderResponse> findAll(
            int page,
            int size,
            ProviderStatus status
    );

    ProviderResponse findById(Long id);

    ProviderResponse update(
            Long id,
            CreateProviderRequest request
    );

    ProviderResponse changeStatus(
            Long id,
            ProviderStatus status
    );
}
