package br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PostalCodeRequest(
        @NotBlank
        @Pattern(regexp = "\\d{8}", message = "{field.cep.pattern}")
        String postal_code
) {}
