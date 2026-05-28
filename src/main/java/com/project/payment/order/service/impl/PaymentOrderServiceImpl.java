package com.project.payment.order.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.payment.order.dto.order.request.CreatePaymentOrderRequest;
import com.project.payment.order.dto.order.response.PaymentOrderResponse;
import com.project.payment.order.entity.IdempotencyItem;
import com.project.payment.order.entity.PaymentOrderEntity;
import com.project.payment.order.entity.ProviderEntity;
import com.project.payment.order.enums.PaymentOrderStatus;
import com.project.payment.order.enums.ProviderStatus;
import com.project.payment.order.exception.BusinessException;
import com.project.payment.order.exception.NotFoundException;
import com.project.payment.order.mapper.PaymentOrderMapper;
import com.project.payment.order.repository.IdempotencyRepository;
import com.project.payment.order.repository.PaymentOrderRepository;
import com.project.payment.order.repository.ProviderRepository;
import com.project.payment.order.service.interfaces.PaymentOrderService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentOrderServiceImpl implements PaymentOrderService {

    private final PaymentOrderRepository paymentOrderRepository;

    private final ProviderRepository providerRepository;

    private final IdempotencyRepository idempotencyRepository;

    @Transactional
    @Override
    public PaymentOrderResponse create(
            CreatePaymentOrderRequest request,
            String idempotencyKey
    ) {
    
        Optional<IdempotencyItem> existing =
                idempotencyRepository
                        .findByIdempotencyKey(idempotencyKey);
    
        if (existing.isPresent()) {
    
            PaymentOrderEntity existingOrder =
                    paymentOrderRepository.findById(
                            existing.get().getOrderId()
                    ).orElseThrow();
    
            return PaymentOrderMapper.toResponse(
                    existingOrder
            );
        }
    
        ProviderEntity provider =
                providerRepository.findById(
                        request.getProviderId()
                ).orElseThrow(() ->
                        new NotFoundException(
                                "Proveedor no encontrado"
                        ));
    
        if (provider.getStatus() != ProviderStatus.ACTIVO) {
    
            throw new BusinessException(
                    "El proveedor está inactivo"
            );
        }
    
        PaymentOrderEntity order =
                PaymentOrderEntity.builder()
                        .provider(provider)
                        .amount(request.getAmount())
                        .concept(request.getConcept())
                        .createdAt(LocalDateTime.now())
                        .status(PaymentOrderStatus.BORRADOR)
                        .build();
    
        PaymentOrderEntity saved =
                paymentOrderRepository.save(order);
    
        IdempotencyItem record =
                IdempotencyItem.builder()
                        .idempotencyKey(idempotencyKey)
                        .orderId(saved.getId())
                        .build();
    
        idempotencyRepository.save(record);
    
        return PaymentOrderMapper.toResponse(saved);
    }

    @Override
    public Page<PaymentOrderResponse> findAll(
            int page,
            int size,
            PaymentOrderStatus status,
            Long providerId
    ) {

        PageRequest pageable = PageRequest.of(page, size);

        Page<PaymentOrderEntity> orders;

        if (status != null && providerId != null) {
    
            orders = paymentOrderRepository
                            .findByStatusAndProviderId(
                                    status,
                                    providerId,
                                    pageable
                            );
    
        } else if (status != null) {
    
            orders = paymentOrderRepository
                            .findByStatus(
                                    status,
                                    pageable
                            );
    
        } else if (providerId != null) {
    
            orders = paymentOrderRepository
                            .findByProviderId(
                                    providerId,
                                    pageable
                            );
    
        } else {
    
            orders = paymentOrderRepository.findAll(pageable);
        }
    
        return orders.map(
                PaymentOrderMapper::toResponse
        );
    }

    @Override
    public PaymentOrderResponse findById(Long id) {

        PaymentOrderEntity order = paymentOrderRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Orden no encontrada"
                        ));
        
        return PaymentOrderMapper.toResponse(order);
    }

    @Override
    public PaymentOrderResponse changeStatus(
            Long id,
            PaymentOrderStatus newStatus
    ) {

        PaymentOrderEntity order = paymentOrderRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Orden no encontrada"
                        ));

        validateTransition(
                order.getStatus(),
                newStatus
        );

        order.setStatus(newStatus);

        PaymentOrderEntity updated = paymentOrderRepository.save(order);

        return PaymentOrderMapper.toResponse(updated);
    }

    private void validateTransition(
            PaymentOrderStatus current,
            PaymentOrderStatus next
    ) {

        boolean valid = switch (current) {

            case BORRADOR ->
                    next == PaymentOrderStatus.APROBADA
                    || next == PaymentOrderStatus.RECHAZADA;
    
            case APROBADA ->
                    next == PaymentOrderStatus.PAGADA;
    
            default -> false;
        };

        if (!valid) {
            throw new BusinessException(
                    "Transición de estado inválida"
            );
        }
    }
}
