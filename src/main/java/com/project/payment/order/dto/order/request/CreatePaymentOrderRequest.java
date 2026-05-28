package com.project.payment.order.dto.order.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreatePaymentOrderRequest {

    @NotNull
    private Long providerId;

    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @Size(max = 250)
    private String concept;
}
