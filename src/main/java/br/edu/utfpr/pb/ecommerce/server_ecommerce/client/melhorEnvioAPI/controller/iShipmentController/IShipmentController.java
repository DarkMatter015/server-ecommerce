package br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.controller.iShipmentController;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.response.ShipmentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Tag(name = "Shipment Controller", description = "Controller responsible for shipment calculations via Melhor Envio API")
public interface IShipmentController {
    @Operation(summary = "Calculate shipment by products", description = "Calculates shipping costs and delivery times based on a list of products and destination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipment calculated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error or external API error")
    })
    @PostMapping("/products")
    ResponseEntity<List<ShipmentResponseDTO>> calculateShipmentByProducts(ShipmentRequestDTO shipmentRequestDto);
}
