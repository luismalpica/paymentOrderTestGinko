package com.project.payment.order.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.project.payment.order.entity.ProviderEntity;
import com.project.payment.order.enums.ProviderStatus;
import com.project.payment.order.repository.ProviderRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentOrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProviderRepository providerRepository;

    @Test
    void shouldCreatePaymentOrderSuccessfully() throws Exception {

        ProviderEntity provider =
                ProviderEntity.builder()
                        .businessName("Proveedor Test")
                        .taxId("900999")
                        .email("provider@test.com")
                        .status(ProviderStatus.ACTIVO)
                        .build();

        ProviderEntity savedProvider =
                providerRepository.save(provider);

        String request = """
                {
                    "providerId": %d,
                    "amount": 50000,
                    "concept": "Pago de prueba"
                }
                """.formatted(savedProvider.getId());

        mockMvc.perform(
                        post("/api/orders")
                                .header(
                                        "Idempotency-Key",
                                        "abc-123"
                                )
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(request)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status")
                        .value("BORRADOR"))
                .andExpect(jsonPath("$.amount")
                        .value(50000));
    }
}
