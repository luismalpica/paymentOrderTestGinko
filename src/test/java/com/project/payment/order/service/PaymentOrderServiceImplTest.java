package com.project.payment.order.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.payment.order.dto.order.request.CreatePaymentOrderRequest;
import com.project.payment.order.dto.order.response.PaymentOrderResponse;
import com.project.payment.order.entity.IdempotencyItem;
import com.project.payment.order.entity.PaymentOrderEntity;
import com.project.payment.order.entity.ProviderEntity;
import com.project.payment.order.enums.PaymentOrderStatus;
import com.project.payment.order.enums.ProviderStatus;
import com.project.payment.order.exception.BusinessException;
import com.project.payment.order.repository.IdempotencyRepository;
import com.project.payment.order.repository.PaymentOrderRepository;
import com.project.payment.order.repository.ProviderRepository;
import com.project.payment.order.service.impl.PaymentOrderServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentOrderServiceImplTest {

    @Mock
    private PaymentOrderRepository paymentOrderRepository;

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private IdempotencyRepository idempotencyRepository;

    @InjectMocks
    private PaymentOrderServiceImpl paymentOrderService;

    @Test
    void shouldCreatePaymentOrderSuccessfully() {

        CreatePaymentOrderRequest request =
                CreatePaymentOrderRequest.builder()
                        .providerId(1L)
                        .amount(BigDecimal.valueOf(50000))
                        .concept("Pago factura")
                        .build();

        ProviderEntity provider =
                ProviderEntity.builder()
                        .id(1L)
                        .status(ProviderStatus.ACTIVO)
                        .build();

        PaymentOrderEntity savedOrder =
                PaymentOrderEntity.builder()
                        .id(1L)
                        .provider(provider)
                        .amount(BigDecimal.valueOf(50000))
                        .concept("Pago factura")
                        .status(PaymentOrderStatus.BORRADOR)
                        .build();

        when(idempotencyRepository.findByIdempotencyKey("abc"))
                .thenReturn(Optional.empty());

        when(providerRepository.findById(1L))
                .thenReturn(Optional.of(provider));

        when(paymentOrderRepository.save(any()))
                .thenReturn(savedOrder);

        PaymentOrderResponse response =
                paymentOrderService.create(request, "abc");

        assertNotNull(response);
        assertEquals(
                PaymentOrderStatus.BORRADOR,
                response.getStatus()
        );

        verify(paymentOrderRepository)
                .save(any());
    }

    @Test
    void shouldFailWhenProviderInactive() {

        CreatePaymentOrderRequest request =
                CreatePaymentOrderRequest.builder()
                        .providerId(1L)
                        .amount(BigDecimal.valueOf(1000))
                        .concept("Pago")
                        .build();

        ProviderEntity provider =
                ProviderEntity.builder()
                        .id(1L)
                        .status(ProviderStatus.INACTIVO)
                        .build();

        when(idempotencyRepository.findByIdempotencyKey("abc"))
                .thenReturn(Optional.empty());

        when(providerRepository.findById(1L))
                .thenReturn(Optional.of(provider));

        assertThrows(
                BusinessException.class,
                () -> paymentOrderService.create(request, "abc")
        );
    }

    @Test
    void shouldReturnExistingOrderWhenIdempotencyKeyExists() {

        IdempotencyItem item =
                IdempotencyItem.builder()
                        .idempotencyKey("abc")
                        .orderId(1L)
                        .build();

        ProviderEntity provider =
                ProviderEntity.builder()
                        .id(1L)
                        .status(ProviderStatus.ACTIVO)
                        .build();

        PaymentOrderEntity order =
                PaymentOrderEntity.builder()
                        .id(1L)
                        .provider(provider)
                        .status(PaymentOrderStatus.BORRADOR)
                        .build();

        when(idempotencyRepository.findByIdempotencyKey("abc"))
                .thenReturn(Optional.of(item));

        when(paymentOrderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        PaymentOrderResponse response =
        paymentOrderService.create(
                CreatePaymentOrderRequest.builder()
                        .providerId(1L)
                        .amount(BigDecimal.valueOf(150000))
                        .concept("Pago proveedor tecnología")
                        .build(),
                "abc"
        );

        assertEquals(1L, response.getId());

        verify(paymentOrderRepository, never())
                .save(any());
    }

    @Test
    void shouldChangeStatusSuccessfully() {

        ProviderEntity provider =
                ProviderEntity.builder()
                        .id(1L)
                        .status(ProviderStatus.ACTIVO)
                        .build();

        PaymentOrderEntity order =
                PaymentOrderEntity.builder()
                        .id(1L)
                        .provider(provider)
                        .status(PaymentOrderStatus.BORRADOR)
                        .build();

        when(paymentOrderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(paymentOrderRepository.save(any()))
                .thenReturn(order);

        PaymentOrderResponse response =
                paymentOrderService.changeStatus(
                        1L,
                        PaymentOrderStatus.APROBADA
                );

        assertEquals(
                PaymentOrderStatus.APROBADA,
                response.getStatus()
        );
    }

    @Test
    void shouldFailWhenTransitionIsInvalid() {

        ProviderEntity provider =
                ProviderEntity.builder()
                        .id(1L)
                        .status(ProviderStatus.ACTIVO)
                        .build();

        PaymentOrderEntity order =
                PaymentOrderEntity.builder()
                        .id(1L)
                        .provider(provider)
                        .status(PaymentOrderStatus.PAGADA)
                        .build();

        when(paymentOrderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(
                BusinessException.class,
                () -> paymentOrderService.changeStatus(
                        1L,
                        PaymentOrderStatus.BORRADOR
                )
        );
    }
}
