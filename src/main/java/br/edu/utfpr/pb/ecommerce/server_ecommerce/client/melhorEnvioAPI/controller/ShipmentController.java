package br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.controller;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.response.ShipmentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.service.MelhorEnvioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shipment")
@RequiredArgsConstructor
@Tag(name = "Shipment Controller", description = "Controller responsible for shipment calculations via Melhor Envio API")
public class ShipmentController {
    private final MelhorEnvioService melhorEnvioService;

    @Operation(summary = "Calculate shipment by products", description = "Calculates shipping costs and delivery times based on a list of products and destination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipment calculated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error or external API error")
    })
    @PostMapping("/products")
    public ResponseEntity<List<ShipmentResponseDTO>> calculateShipmentByProducts(@RequestBody ShipmentRequestDTO shipmentRequestDto) {
        List<ShipmentResponseDTO> responses = melhorEnvioService.calculateShipmentByProducts(shipmentRequestDto);
        return ResponseEntity.ok(responses);
    }
}
