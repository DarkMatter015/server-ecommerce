package br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.controller.iCepController;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.dto.AddressCEP;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "CEP", description = "Endpoint for search Address by CEP")
public interface ICepController {
    @Operation(summary = "Get address by cep", description = "Returns a address by cep")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found address"),
            @ApiResponse(responseCode = "404", description = "CEP not found or invalid"),
            @ApiResponse(responseCode = "500", description = "Internal server error or external API error")
    })
    @GetMapping("/validate/{cep}")
    ResponseEntity<AddressCEP> getAddressByCEP(@Parameter(description = "CEP") @PathVariable String cep);
}
