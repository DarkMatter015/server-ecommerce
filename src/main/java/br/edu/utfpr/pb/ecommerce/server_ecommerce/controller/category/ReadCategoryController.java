package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.category;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.ReadController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.category.CategoryResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.iCategory.ICategoryResponseService;
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
@RequestMapping("categories")
@Tag(name = "Category Read", description = "Endpoints for reading categories")
public class ReadCategoryController extends ReadController<Category, CategoryResponseDTO, Long> {

    private final ICategoryResponseService categoryResponseService;

    public ReadCategoryController(ICategoryResponseService categoryResponseService, ModelMapper modelMapper, ICategoryResponseService categoryResponseService1) {
        super(CategoryResponseDTO.class, categoryResponseService, modelMapper);
        this.categoryResponseService = categoryResponseService1;
    }

    @Operation(summary = "Filter categories", description = "Returns a paginated list of categories filtered by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully filtered categories")
    })
    @GetMapping("/filter")
    public ResponseEntity<Page<CategoryResponseDTO>> findAllByCategory(@Parameter(description = "Category name") @RequestParam(required = false) String name,
                                                                      @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(categoryResponseService.findByCriteria(name, pageable).map(this::convertToDto));
    }
}
