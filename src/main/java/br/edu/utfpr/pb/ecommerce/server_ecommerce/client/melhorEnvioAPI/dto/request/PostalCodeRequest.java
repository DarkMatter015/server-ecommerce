package br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record PostalCodeRequest(
        @NotEmpty
        String postal_code
) {}
