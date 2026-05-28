package com.project.payment.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.payment.order.entity.PaymentOrderEntity;
import com.project.payment.order.enums.PaymentOrderStatus;

@Repository
public interface PaymentOrderRepository extends JpaRepository<PaymentOrderEntity, Long> {

    Page<PaymentOrderEntity> findByStatus(
            PaymentOrderStatus status,
            Pageable pageable
    );

    Page<PaymentOrderEntity> findByProviderId(
            Long providerId,
            Pageable pageable
    );

    Page<PaymentOrderEntity> findByStatusAndProviderId(
        PaymentOrderStatus status,
        Long providerId,
        Pageable pageable
);
}
