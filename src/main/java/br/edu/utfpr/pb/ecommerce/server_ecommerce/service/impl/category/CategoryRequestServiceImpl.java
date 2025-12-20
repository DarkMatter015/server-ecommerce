package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.category;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.category.CategoryUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.CategoryRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.iCategory.ICategoryRequestService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudRequestServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryRequestServiceImpl extends CrudRequestServiceImpl<Category, CategoryUpdateDTO, Long> implements ICategoryRequestService {

    private final CategoryRepository categoryRepository;
    private final CategoryResponseServiceImpl categoryResponseService;
    private final ProductRepository productRepository;

    public CategoryRequestServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, CategoryResponseServiceImpl categoryResponseService) {
        super(categoryRepository, categoryResponseService);
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.categoryResponseService = categoryResponseService;
    }

    @Override
    @Transactional
    public Category activate(Long id) {
        Category category = categoryResponseService.findById(id);
        if (category.isActive()) return category;
        productRepository.activateByCategoryId(category.getId());
        category.setDeletedAt(null);
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        productRepository.softDeleteByCategoryId(id);
        categoryRepository.softDeleteById(id);
    }
}
