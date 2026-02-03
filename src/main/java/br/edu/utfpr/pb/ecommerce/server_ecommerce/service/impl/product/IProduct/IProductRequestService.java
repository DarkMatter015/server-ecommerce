package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.product.IProduct;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseSoftDeleteRequestService;

public interface IProductRequestService extends IBaseSoftDeleteRequestService<Product, ProductUpdateDTO, Long> {
    Product create(ProductRequestDTO productRequestDTO);
}
