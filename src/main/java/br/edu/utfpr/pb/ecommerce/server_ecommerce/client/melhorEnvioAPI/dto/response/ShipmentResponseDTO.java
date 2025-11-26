package br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.response;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.util.BigDecimalDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;
import java.util.List;

public record ShipmentResponseDTO(

        Long id,

        String name,

        @JsonDeserialize(using = BigDecimalDeserializer.class)
        BigDecimal price,

        @JsonDeserialize(using = BigDecimalDeserializer.class)
        BigDecimal custom_price,

        @JsonDeserialize(using = BigDecimalDeserializer.class)
        BigDecimal discount,

        String currency,

        Integer delivery_time,

        List<PackageResponse> packages,

        CompanyResponse company,

        String error
) {
}
