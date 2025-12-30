package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.alertProduct;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.alertProduct.iAlertProductController.IWriteAlertProductController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.alertProduct.AlertProductRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.alertProduct.AlertProductResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.alertProduct.IAlertProduct.IAlertProductRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.ControllerUtils.createUri;

@RestController
@RequiredArgsConstructor
@RequestMapping("alerts")
public class WriteAlertProductController implements IWriteAlertProductController {

    private final IAlertProductRequestService alertProductRequestService;

    @Override
    @PostMapping
    public ResponseEntity<AlertProductResponseDTO> createAlert(@RequestBody @Valid AlertProductRequestDTO dto) {
        AlertProductResponseDTO alert = alertProductRequestService.createAlert(dto);
        return ResponseEntity.created(createUri(alert)).body(alert);
    }

    @Override
    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<AlertProductResponseDTO> cancelAlert(@PathVariable Long id) {
        AlertProductResponseDTO alert = alertProductRequestService.cancelAlert(id);
        return ResponseEntity.ok(alert);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<AlertProductResponseDTO> deleteAlert(@PathVariable Long id) {
        alertProductRequestService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/{id}/activate")
    public ResponseEntity<AlertProductResponseDTO> activateAlert(@PathVariable Long id) {
        AlertProductResponseDTO alert = alertProductRequestService.activateAlert(id);
        return ResponseEntity.ok(alert);
    }
}
