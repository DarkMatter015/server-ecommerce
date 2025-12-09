package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.category;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.category.CategoryUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.CategoryRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.iCategory.ICategoryRequestService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudRequestServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CategoryRequestServiceImpl extends CrudRequestServiceImpl<Category, CategoryUpdateDTO, Long> implements ICategoryRequestService {


    public CategoryRequestServiceImpl(CategoryRepository categoryRepository, CategoryResponseServiceImpl categoryResponseService) {
        super(categoryRepository, categoryResponseService);
    }
}
