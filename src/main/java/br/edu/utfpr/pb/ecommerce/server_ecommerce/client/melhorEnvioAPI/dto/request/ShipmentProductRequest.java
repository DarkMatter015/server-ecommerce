package br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ShipmentProductRequest(
        String id,
        BigDecimal width,
        BigDecimal height,
        BigDecimal length,

        @NotNull
        BigDecimal insurance_value,

        @NotNull
        Integer quantity
) {}