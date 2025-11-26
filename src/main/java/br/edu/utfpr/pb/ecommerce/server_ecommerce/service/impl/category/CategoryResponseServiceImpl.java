package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.category;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.CategoryRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.iCategory.ICategoryResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudResponseServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.specification.CategorySpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CategoryResponseServiceImpl extends CrudResponseServiceImpl<Category, Long> implements ICategoryResponseService {

    private final CategoryRepository categoryRepository;

    public CategoryResponseServiceImpl(CategoryRepository categoryRepository, CategoryRepository categoryRepository1) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository1;
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
