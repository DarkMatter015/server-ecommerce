package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.iCategory;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.ICRUD.ICrudResponseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryResponseService extends ICrudResponseService<Category, Long> {
    Page<Category> findByCriteria(String name, Pageable pageable);
}
