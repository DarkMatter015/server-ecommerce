package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.product;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.BaseSoftDeleteReadController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.product.IProduct.IProductResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("products")
@Tag(name = "Product Read", description = "Endpoints for reading products")
public class ReadProductController extends BaseSoftDeleteReadController<Product, ProductResponseDTO> {

    private final IProductResponseService productResponseService;

    public ReadProductController(IProductResponseService productResponseService, ModelMapper modelMapper, IProductResponseService productResponseService1) {
        super(ProductResponseDTO.class, productResponseService, modelMapper);
        this.productResponseService = productResponseService1;
    }

    @Operation(summary = "Filter products", description = "Returns a paginated list of products filtered by name and category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully filtered products")
    })
    @GetMapping("/filter")
    public ResponseEntity<Page<ProductResponseDTO>> findAllByCategory(@Parameter(description = "Product name") @RequestParam(required = false) String name,
                                                                      @Parameter(description = "Category name") @RequestParam(required = false) String category,
                                                                      @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(productResponseService.findByCriteria(name, category, pageable).map(this::convertToDto));
    }
}
