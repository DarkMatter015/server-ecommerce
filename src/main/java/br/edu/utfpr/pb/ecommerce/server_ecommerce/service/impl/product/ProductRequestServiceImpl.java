package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.product;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.CategoryNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.ProductNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.CategoryRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IProduct.IProductRequestService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudRequestServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.ValidationUtils.validateStringNullOrBlank;

@Service
public class ProductRequestServiceImpl extends CrudRequestServiceImpl<Product, ProductUpdateDTO, Long> implements IProductRequestService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductRequestServiceImpl(ProductRepository productRepository, ProductResponseServiceImpl productResponseService, CategoryRepository categoryRepository) {
        super(productRepository, productResponseService);
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Product update(Long id, ProductUpdateDTO updateDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        if (updateDTO.getName() != null) {
            validateStringNullOrBlank(updateDTO.getName());
            existingProduct.setName(updateDTO.getName());
        }

        if (updateDTO.getDescription() != null){
            validateStringNullOrBlank(updateDTO.getDescription());
            existingProduct.setDescription(updateDTO.getDescription());
        }

        if (updateDTO.getPrice() != null){
            existingProduct.setPrice(updateDTO.getPrice());
        }

        if (updateDTO.getUrlImage() != null) {
            validateStringNullOrBlank(updateDTO.getUrlImage());
            existingProduct.setUrlImage(updateDTO.getUrlImage());
        }

        if (updateDTO.getCategoryId() != null){
            Category category = categoryRepository.findById(updateDTO.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found with this id: " + updateDTO.getCategoryId()));

            existingProduct.setCategory(category);
        }

        if (updateDTO.getQuantityAvailableInStock() != null){
            existingProduct.setQuantityAvailableInStock(updateDTO.getQuantityAvailableInStock());
        }

        return productRepository.save(existingProduct);
    }

}
