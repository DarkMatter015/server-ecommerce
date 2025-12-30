package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.base.iBaseController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;

@SecurityRequirement(name = "bearer-key")
public interface IBaseWriteController<D, RD, UD, ID> {

    @Operation(summary = "Create entity", description = "Creates a new entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entity created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    ResponseEntity<RD> create(D entityDto);

    @Operation(summary = "Update entity", description = "Updates an existing entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity updated successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    ResponseEntity<RD> update(@Parameter(description = "Entity ID") ID id, UD entityDto);

    @Operation(summary = "Delete entity", description = "Removes an entity by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entity removed successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    ResponseEntity<Void> delete(@Parameter(description = "Entity ID") ID id);
}
