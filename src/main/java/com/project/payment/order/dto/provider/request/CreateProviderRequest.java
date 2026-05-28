package com.project.payment.order.dto.provider.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateProviderRequest {

    @NotBlank
    private String businessName;

    @NotBlank
    private String taxId;

    @Email
    @NotBlank
    private String email;
}
