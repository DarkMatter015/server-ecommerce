package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.base.iBaseController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface IBaseReadController<RD, ID> {
    @Operation(summary = "List all", description = "Returns a list of all entities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully listed entities")
    })
    @GetMapping
    ResponseEntity<List<RD>> findAll();

    @Operation(summary = "List all paginated", description = "Returns a paginated list of entities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully listed entities")
    })
    @GetMapping("page")
    ResponseEntity<Page<RD>> findAll(@Parameter(description = "Page number (0..N)") int page,
                                            @Parameter(description = "Page size") int size,
                                            @Parameter(description = "Sort field") String order,
                                            @Parameter(description = "Sort ascending (true) or descending (false)") Boolean asc);

    @Operation(summary = "Get entity by ID", description = "Returns a specific entity by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found entity"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    @GetMapping("{id}")
    ResponseEntity<RD> findOne(@Parameter(description = "Entity ID") @PathVariable ID id);

    @Operation(summary = "Check existence", description = "Checks if an entity exists by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification successful")
    })
    @GetMapping("exists/{id}")
    ResponseEntity<Boolean> exists(@Parameter(description = "Entity ID") @PathVariable ID id);

    @Operation(summary = "Count entities", description = "Returns the total number of entities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count successful")
    })
    @GetMapping("count")
    ResponseEntity<Long> count();
}
