package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.product.iProductController;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Product Read", description = "Endpoints for reading products")
public interface IReadProductController {
    @Operation(summary = "Filter products", description = "Returns a paginated list of products filtered by name and category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully filtered products")
    })
    @GetMapping("/filter")
    ResponseEntity<Page<ProductResponseDTO>> findAllByCategory(@Parameter(description = "Product name") String name,
                                                               @Parameter(description = "Category name") String category,
                                                               Pageable pageable);
}
