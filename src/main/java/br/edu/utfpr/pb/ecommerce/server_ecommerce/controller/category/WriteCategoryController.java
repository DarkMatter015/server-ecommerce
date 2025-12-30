package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.category;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.softDeleteController.BaseSoftDeleteWriteController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.category.iCategoryController.IWriteCategoryController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.category.CategoryRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.category.CategoryResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.category.CategoryUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.category.ICategory.ICategoryRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categories")
public class WriteCategoryController extends BaseSoftDeleteWriteController<Category, CategoryRequestDTO, CategoryResponseDTO, CategoryUpdateDTO> implements IWriteCategoryController {
    public WriteCategoryController(ICategoryRequestService service, ModelMapper modelMapper) {
        super(service, modelMapper, Category.class, CategoryResponseDTO.class);
    }
}
