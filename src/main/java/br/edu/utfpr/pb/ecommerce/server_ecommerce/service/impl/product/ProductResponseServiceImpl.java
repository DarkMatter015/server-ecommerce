package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.product;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IProduct.IProductResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudResponseServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProductResponseServiceImpl extends CrudResponseServiceImpl<Product, Long> implements IProductResponseService {

    private final ProductRepository productRepository;

    public ProductResponseServiceImpl(ProductRepository productRepository, AuthService authService) {
        super(productRepository, authService);
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> findByCriteria(String name, String categoryName, Pageable pageable) {
        Specification<Product> spec = Specification.anyOf();

        if (name != null && !name.isBlank()) {
            spec = spec.and(ProductSpecification.hasNameLike(name));
        }

        if (categoryName != null && !categoryName.isBlank()) {
            spec = spec.and(ProductSpecification.hasCategoryNameLike(categoryName));
        }

        return productRepository.findAll(spec, pageable);
    }
}
