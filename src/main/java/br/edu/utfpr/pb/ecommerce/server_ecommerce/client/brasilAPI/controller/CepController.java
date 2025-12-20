package br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.controller;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.dto.AddressCEP;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.service.CepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cep")
@RequiredArgsConstructor
@Tag(name = "CEP", description = "Endpoint for search Address by CEP")
public class CepController {

    private final CepService cepService;

    @Operation(summary = "Get address by cep", description = "Returns a address by cep")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found address"),
            @ApiResponse(responseCode = "404", description = "CEP not found or invalid"),
            @ApiResponse(responseCode = "500", description = "Internal server error or external API error")
    })
    @GetMapping("/validate/{cep}")
    public ResponseEntity<AddressCEP> getAddressByCEP(@Parameter(description = "CEP") @PathVariable String cep) {
        AddressCEP address = cepService.getAddressByCEP(cep);
        return ResponseEntity.ok(address);
    }
}
