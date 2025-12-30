package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.alertProduct.iAlertProductController;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.alertProduct.AlertProductRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.alertProduct.AlertProductResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Alerts Write", description = "Endpoints for writing alerts of available products")
public interface IWriteAlertProductController {
    @Operation(summary = "Create Alert", description = "Creates a new alert for product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alert created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
    })
    @PostMapping
    ResponseEntity<AlertProductResponseDTO> createAlert(AlertProductRequestDTO dto);

    @Operation(summary = "Cancel alert", description = "Cancel an alert by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Alert cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Alert not found")
    })
    @DeleteMapping("/{id}/cancel")
    ResponseEntity<AlertProductResponseDTO> cancelAlert(@PathVariable Long id);

    @Operation(summary = "Delete alert", description = "Deletes an alert by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Alert deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Alert not found")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<AlertProductResponseDTO> deleteAlert(@PathVariable Long id);

    @Operation(summary = "Activate alert", description = "Activate an alert by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Alert activate successfully"),
            @ApiResponse(responseCode = "404", description = "Alert not found")
    })
    @PostMapping("/{id}/activate")
    ResponseEntity<AlertProductResponseDTO> activateAlert(@PathVariable Long id);
}
