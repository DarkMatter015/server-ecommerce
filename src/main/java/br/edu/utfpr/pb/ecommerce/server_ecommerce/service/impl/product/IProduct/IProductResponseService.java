package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.product.IProduct;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseSoftDeleteResponseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductResponseService extends IBaseSoftDeleteResponseService<Product, Long> {
    Page<Product> findByCriteria(String name, String categoryName, Pageable pageable);
}
