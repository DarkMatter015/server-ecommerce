package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.product;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.BaseSoftDeleteWriteController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.ProductMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.product.IProduct.IProductRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("products")
@Tag(name = "Product Write", description = "Endpoints for writing products")
public class WriteProductController extends BaseSoftDeleteWriteController<Product, ProductRequestDTO, ProductResponseDTO, ProductUpdateDTO> {

    private final ProductMapper productMapper;

    public WriteProductController(IProductRequestService service, ModelMapper modelMapper, ProductMapper productMapper) {
        super(service, modelMapper, Product.class, ProductResponseDTO.class);
        this.productMapper = productMapper;
    }

    @Override
    protected Product convertToEntity(ProductRequestDTO createDto) {
        return productMapper.toEntity(createDto);
    }
}
