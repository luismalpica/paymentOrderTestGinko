package com.project.payment.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.payment.order.entity.ProviderEntity;
import com.project.payment.order.enums.ProviderStatus;

@Repository
public interface ProviderRepository extends JpaRepository<ProviderEntity, Long> {

    boolean existsByTaxId(String taxId);

    Page<ProviderEntity> findByStatus(
            ProviderStatus status,
            Pageable pageable
    );
}
