package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IProduct;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.ICRUD.ICrudResponseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductResponseService extends ICrudResponseService<Product, Long> {
    Page<Product> findByCriteria(String name, String categoryName, Pageable pageable);
}
