package com.project.payment.order.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.project.payment.order.dto.provider.request.CreateProviderRequest;
import com.project.payment.order.dto.provider.response.ProviderResponse;
import com.project.payment.order.entity.ProviderEntity;
import com.project.payment.order.enums.ProviderStatus;
import com.project.payment.order.exception.BusinessException;
import com.project.payment.order.exception.NotFoundException;
import com.project.payment.order.mapper.ProviderMapper;
import com.project.payment.order.repository.ProviderRepository;
import com.project.payment.order.service.interfaces.ProviderService;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;

    @Override
    public ProviderResponse create(CreateProviderRequest request) {

        if (providerRepository.existsByTaxId(request.getTaxId())) {
            throw new BusinessException(
                    "El número tributario ya existe"
            );
        }

        ProviderEntity provider = ProviderEntity.builder()
                .businessName(request.getBusinessName())
                .taxId(request.getTaxId())
                .email(request.getEmail())
                .status(ProviderStatus.ACTIVO)
                .build();

        ProviderEntity savedProvider =
                providerRepository.save(provider);

        return ProviderMapper.toResponse(savedProvider);
    }

    @Override
    public Page<ProviderResponse> findAll(
            int page,
            int size,
            ProviderStatus status
    ) {

        PageRequest pageable =
                PageRequest.of(page, size);

        Page<ProviderEntity> providers;

        if (status != null) {

            providers = providerRepository.findByStatus(
                    status,
                    pageable
            );

        } else {

            providers = providerRepository.findAll(pageable);
        }

        return providers.map(ProviderMapper::toResponse);
    }

    @Override
    public ProviderResponse findById(Long id) {

        ProviderEntity provider =
                providerRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Proveedor no encontrado"
                                ));

        return ProviderMapper.toResponse(provider);
    }

    @Override
    public ProviderResponse update(
            Long id,
            CreateProviderRequest request
    ) {

        ProviderEntity provider =
                providerRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Proveedor no encontrado"
                                ));

        provider.setBusinessName(
                request.getBusinessName()
        );

        provider.setEmail(
                request.getEmail()
        );

        ProviderEntity updatedProvider =
                providerRepository.save(provider);

        return ProviderMapper.toResponse(updatedProvider);
    }

    @Override
    public ProviderResponse changeStatus(
            Long id,
            ProviderStatus status
    ) {

        ProviderEntity provider =
                providerRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Proveedor no encontrado"
                                ));

        provider.setStatus(status);

        ProviderEntity updatedProvider =
                providerRepository.save(provider);

        return ProviderMapper.toResponse(updatedProvider);
    }
}

