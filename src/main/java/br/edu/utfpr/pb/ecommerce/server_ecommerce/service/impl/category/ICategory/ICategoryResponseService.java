package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.category.ICategory;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseSoftDeleteResponseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryResponseService extends IBaseSoftDeleteResponseService<Category, Long> {
    Page<Category> findByCriteria(String name, Pageable pageable);
}
