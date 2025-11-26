package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.category;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.category.CategoryUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.CategoryNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.CategoryRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.iCategory.ICategoryRequestService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudRequestServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.ValidationUtils.validateStringNullOrBlank;

@Service
public class CategoryRequestServiceImpl extends CrudRequestServiceImpl<Category, CategoryUpdateDTO, Long> implements ICategoryRequestService {

    private final CategoryRepository categoryRepository;

    public CategoryRequestServiceImpl(CategoryRepository categoryRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Category update(Long id, CategoryUpdateDTO updateDTO) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found."));

        if (updateDTO.getName() != null) {
            validateStringNullOrBlank(updateDTO.getName());
            existingCategory.setName(updateDTO.getName());
        }

        return categoryRepository.save(existingCategory);
    }

}
