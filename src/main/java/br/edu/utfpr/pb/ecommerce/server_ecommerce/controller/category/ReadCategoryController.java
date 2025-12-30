package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.category;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.softDeleteController.BaseSoftDeleteReadController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.category.iCategoryController.IReadCategoryController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.category.CategoryResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.category.ICategory.ICategoryResponseService;
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
public class ReadCategoryController extends BaseSoftDeleteReadController<Category, CategoryResponseDTO> implements IReadCategoryController {

    private final ICategoryResponseService categoryResponseService;

    public ReadCategoryController(ICategoryResponseService categoryResponseService, ModelMapper modelMapper, ICategoryResponseService categoryResponseService1) {
        super(CategoryResponseDTO.class, categoryResponseService, modelMapper);
        this.categoryResponseService = categoryResponseService1;
    }

    @Override
    @GetMapping("/filter")
    public ResponseEntity<Page<CategoryResponseDTO>> findAllByCategory(@RequestParam(required = false) String name,
                                                                      @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(categoryResponseService.findByCriteria(name, pageable).map(this::convertToDto));
    }
}
