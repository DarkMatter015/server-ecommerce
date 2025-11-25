package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.product;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductRepository;
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

    public ProductResponseServiceImpl(ProductRepository productRepository, ProductRepository productRepository1) {
        super(productRepository);
        this.productRepository = productRepository1;
    }

    @Override
    public Page<Product> findByCriteria(String name, String categoryName, Pageable pageable) {
        // 1. Inicia uma Spec nula (base para encadear)
        Specification<Product> spec = Specification.anyOf();

        // 2. Adiciona o filtro de nome APENAS se o usuário enviou algo
        if (name != null && !name.isBlank()) {
            spec = spec.and(ProductSpecification.hasNameLike(name));
        }

        // 3. Adiciona o filtro de categoria APENAS se o usuário enviou algo
        if (categoryName != null && !categoryName.isBlank()) {
            spec = spec.and(ProductSpecification.hasCategoryNameLike(categoryName));
        }

        // 4. O Repository executa a query composta final
        return productRepository.findAll(spec, pageable);
    }
}
