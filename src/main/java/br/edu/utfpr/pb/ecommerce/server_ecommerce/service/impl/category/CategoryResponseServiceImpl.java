package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.category;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.CategoryRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.category.ICategory.ICategoryResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.BaseSoftDeleteResponseServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.specification.CategorySpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CategoryResponseServiceImpl extends BaseSoftDeleteResponseServiceImpl<Category, Long> implements ICategoryResponseService {

    private final CategoryRepository categoryRepository;

    public CategoryResponseServiceImpl(CategoryRepository categoryRepository, AuthService authService) {
        super(categoryRepository, authService);
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<Category> findByCriteria(String name, Pageable pageable) {
        Specification<Category> spec = Specification.anyOf();

        if (name != null && !name.isBlank()) {
            spec = spec.and(CategorySpecification.hasNameLike(name));
        }

        return categoryRepository.findAll(spec, pageable);
    }
}
