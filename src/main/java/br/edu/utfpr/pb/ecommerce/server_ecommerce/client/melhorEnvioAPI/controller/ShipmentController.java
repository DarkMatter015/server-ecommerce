package br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.controller;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.controller.iShipmentController.IShipmentController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.response.ShipmentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.service.MelhorEnvioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shipment")
@RequiredArgsConstructor
public class ShipmentController implements IShipmentController {
    private final MelhorEnvioService melhorEnvioService;

    @PostMapping("/products")
    public ResponseEntity<List<ShipmentResponseDTO>> calculateShipmentByProducts(@RequestBody @Valid ShipmentRequestDTO shipmentRequestDto) {
        List<ShipmentResponseDTO> responses = melhorEnvioService.calculateShipmentByProducts(shipmentRequestDto);
        return ResponseEntity.ok(responses);
    }
}
