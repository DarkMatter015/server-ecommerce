package br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentRequestDTO {

    @Valid
    private PostalCodeRequest from;

    @NotNull
    @Valid
    private PostalCodeRequest to;

    @NotEmpty
    @Valid
    private List<ShipmentProductRequest> products;
}