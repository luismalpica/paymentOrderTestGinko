package com.project.payment.order.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.payment.order.dto.provider.request.CreateProviderRequest;
import com.project.payment.order.dto.provider.response.ProviderResponse;
import com.project.payment.order.entity.ProviderEntity;
import com.project.payment.order.enums.ProviderStatus;
import com.project.payment.order.exception.BusinessException;
import com.project.payment.order.repository.ProviderRepository;
import com.project.payment.order.service.impl.ProviderServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProviderServiceImplTest {

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private ProviderServiceImpl providerService;

    @Test
    void shouldCreateProviderSuccessfully() {

        CreateProviderRequest request =
                CreateProviderRequest.builder()
                        .businessName("Proveedor Test")
                        .taxId("900123")
                        .email("test@test.com")
                        .build();

        when(providerRepository.existsByTaxId("900123"))
                .thenReturn(false);

        ProviderEntity saved =
                ProviderEntity.builder()
                        .id(1L)
                        .businessName("Proveedor Test")
                        .taxId("900123")
                        .email("test@test.com")
                        .status(ProviderStatus.ACTIVO)
                        .build();

        when(providerRepository.save(any()))
                .thenReturn(saved);

        ProviderResponse response =
                providerService.create(request);

        assertNotNull(response);
        assertEquals("900123", response.getTaxId());

        verify(providerRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenTaxIdAlreadyExists() {

        CreateProviderRequest request =
                CreateProviderRequest.builder()
                        .taxId("900123")
                        .build();

        when(providerRepository.existsByTaxId("900123"))
                .thenReturn(true);

        assertThrows(
                BusinessException.class,
                () -> providerService.create(request)
        );

        verify(providerRepository, never())
                .save(any());
    }

    @Test
    void shouldFindProviderById() {

        ProviderEntity provider =
                ProviderEntity.builder()
                        .id(1L)
                        .businessName("Proveedor")
                        .taxId("123")
                        .email("mail@test.com")
                        .status(ProviderStatus.ACTIVO)
                        .build();

        when(providerRepository.findById(1L))
                .thenReturn(Optional.of(provider));

        ProviderResponse response =
                providerService.findById(1L);

        assertEquals(1L, response.getId());
    }
}
