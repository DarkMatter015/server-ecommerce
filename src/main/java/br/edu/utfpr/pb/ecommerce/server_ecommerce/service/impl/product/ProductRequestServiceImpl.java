package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.product;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.BusinessException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.alertProduct.AlertProductUpdatedEventDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.alertProduct.AlertProductUpdatedPublisher;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.BaseSoftDeleteRequestServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.category.CategoryResponseServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.product.IProduct.IProductRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductRequestServiceImpl extends BaseSoftDeleteRequestServiceImpl<Product, ProductUpdateDTO> implements IProductRequestService {

    private final ProductRepository productRepository;
    private final ProductResponseServiceImpl productResponseService;
    private final CategoryResponseServiceImpl categoryResponseService;
    private final AlertProductUpdatedPublisher alertProductUpdatedPublisher;

    public ProductRequestServiceImpl(ProductRepository productRepository,
                                     ProductResponseServiceImpl productResponseService,
                                     CategoryResponseServiceImpl categoryResponseService,
                                     AlertProductUpdatedPublisher alertProductUpdatedPublisher) {
        super(productRepository, productResponseService);
        this.productRepository = productRepository;
        this.productResponseService = productResponseService;
        this.categoryResponseService = categoryResponseService;
        this.alertProductUpdatedPublisher = alertProductUpdatedPublisher;
    }

    @Override
    @Transactional
    public Product activate(Long id) {
        Product product = productResponseService.findById(id);
        if (product.isActive()) return product;
        Category category = categoryResponseService.findById(product.getCategory().getId());
        if (!category.isActive())
            throw new BusinessException(ErrorCode.CATEGORY_ACTIVATE_REQUIRED, category.getName(), category.getId());
        product.setDeletedAt(null);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(Long id, ProductUpdateDTO updateDTO) {
        Product existingProduct = productResponseService.findById(id);
        int oldQuantity = existingProduct.getQuantityAvailableInStock();
        applyPartialUpdate(updateDTO, existingProduct);

        if (updateDTO.getCategoryId() != null) {
            Category category = categoryResponseService.findById(updateDTO.getCategoryId());
            existingProduct.setCategory(category);
        }

        Product savedProduct = productRepository.save(existingProduct);

        int newQuantity = savedProduct.getQuantityAvailableInStock();

        boolean stockBecameAvailable = (newQuantity > oldQuantity) && (newQuantity > 0);

        if (stockBecameAvailable)
            alertProductUpdatedPublisher.send(
                    new AlertProductUpdatedEventDTO(savedProduct.getId(), savedProduct.getName(), newQuantity)
            );

        return savedProduct;
    }

}
