package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.product;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.BusinessException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.ProductNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IProduct.IProductRequestService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudRequestServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.category.CategoryResponseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.ValidationUtils.validateStringNullOrBlank;

@Service
public class ProductRequestServiceImpl extends CrudRequestServiceImpl<Product, ProductUpdateDTO, Long> implements IProductRequestService {

    private final ProductRepository productRepository;
    private final ProductResponseServiceImpl productResponseService;
    private final CategoryResponseServiceImpl categoryResponseService;

    public ProductRequestServiceImpl(ProductRepository productRepository, ProductResponseServiceImpl productResponseService, CategoryResponseServiceImpl categoryResponseService) {
        super(productRepository, productResponseService);
        this.productRepository = productRepository;
        this.productResponseService = productResponseService;
        this.categoryResponseService = categoryResponseService;
    }

    @Override
    @Transactional
    public Product activate(Long id) {
        Product product = productResponseService.findById(id);
        if (product.isActive()) return product;
        Category category = categoryResponseService.findById(product.getCategory().getId());
        if (!category.isActive())
            throw new BusinessException("Activate the category first. Category: " + category.getName() + ", ID: " + category.getId());
        product.setDeletedAt(null);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(Long id, ProductUpdateDTO updateDTO) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found."));

        if (updateDTO.getName() != null) {
            validateStringNullOrBlank(updateDTO.getName());
            existingProduct.setName(updateDTO.getName());
        }

        if (updateDTO.getDescription() != null) {
            validateStringNullOrBlank(updateDTO.getDescription());
            existingProduct.setDescription(updateDTO.getDescription());
        }

        if (updateDTO.getPrice() != null) {
            existingProduct.setPrice(updateDTO.getPrice());
        }

        if (updateDTO.getUrlImage() != null) {
            validateStringNullOrBlank(updateDTO.getUrlImage());
            existingProduct.setUrlImage(updateDTO.getUrlImage());
        }

        if (updateDTO.getCategoryId() != null) {
            Category category = categoryResponseService.findById(updateDTO.getCategoryId());

            existingProduct.setCategory(category);
        }

        if (updateDTO.getQuantityAvailableInStock() != null) {
            existingProduct.setQuantityAvailableInStock(updateDTO.getQuantityAvailableInStock());
        }

        return productRepository.save(existingProduct);
    }

}
