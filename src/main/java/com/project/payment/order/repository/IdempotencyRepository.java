package com.project.payment.order.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.payment.order.entity.IdempotencyItem;

@Repository
public interface IdempotencyRepository
        extends JpaRepository<IdempotencyItem, Long> {

    Optional<IdempotencyItem> findByIdempotencyKey(String idempotencyKey);
}
