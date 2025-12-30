package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.category.iCategoryController;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.category.CategoryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Category Read", description = "Endpoints for reading categories")
public interface IReadCategoryController {

    @Operation(summary = "Filter categories", description = "Returns a paginated list of categories filtered by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully filtered categories")
    })
    @GetMapping("/filter")
    ResponseEntity<Page<CategoryResponseDTO>> findAllByCategory(@Parameter(description = "Category name") String name, Pageable pageable);
}
