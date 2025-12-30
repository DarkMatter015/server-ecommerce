package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.softDeleteController.iSoftDeleteController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface IWriteSoftDeleteController<RD> {
    @Operation(summary = "Activate entity", description = "Activates an entity by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity activated successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    ResponseEntity<RD> activate(@Parameter(description = "Entity ID") Long id);
}
