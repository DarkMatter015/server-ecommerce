package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.product;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.softDeleteController.BaseSoftDeleteWriteController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.product.iProductController.IWriteProductController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.ProductMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.category.ICategory.ICategoryResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.product.IProduct.IProductRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("products")
public class WriteProductController extends BaseSoftDeleteWriteController<Product, ProductRequestDTO, ProductResponseDTO, ProductUpdateDTO> implements IWriteProductController {

    private final ProductMapper productMapper;
    private final ICategoryResponseService categoryResponseService;
    private final IProductRequestService productRequestService;

    public WriteProductController(IProductRequestService service, ModelMapper modelMapper, ProductMapper productMapper, ICategoryResponseService categoryResponseService) {
        super(service, modelMapper, Product.class, ProductResponseDTO.class);
        this.productMapper = productMapper;
        this.categoryResponseService = categoryResponseService;
        this.productRequestService = service;

    }

    @Override
    protected Product convertToEntity(ProductRequestDTO createDto) {
        Category category = categoryResponseService.findById(createDto.getCategoryId());
        return productMapper.toEntity(createDto, category);
    }

    @Override
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(ProductRequestDTO productRequestDTO) {
        Product createdProduct = productRequestService.create(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.convertToResponseDto(createdProduct));
    }
}
