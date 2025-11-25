package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.product;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.ReadController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IProduct.IProductResponseService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("products")
public class ReadProductController extends ReadController<Product, ProductResponseDTO, Long> {

    private final IProductResponseService productResponseService;

    public ReadProductController(IProductResponseService productResponseService, ModelMapper modelMapper, IProductResponseService productResponseService1) {
        super(ProductResponseDTO.class, productResponseService, modelMapper);
        this.productResponseService = productResponseService1;
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ProductResponseDTO>> findAllByCategory(@RequestParam(required = false) String name,
                                                                      @RequestParam(required = false) String category,
                                                                      @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(productResponseService.findByCriteria(name, category, pageable).map(this::convertToDto));
    }
}
