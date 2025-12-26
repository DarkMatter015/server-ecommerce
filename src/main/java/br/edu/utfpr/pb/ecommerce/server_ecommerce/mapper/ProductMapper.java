package br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentProductRequest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.ResourceNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final CategoryRepository categoryRepository;

    public Product toEntity(ProductRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setUrlImage(dto.getUrlImage());
        product.setQuantityAvailableInStock(dto.getQuantityAvailableInStock());

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, dto.getCategoryId()));

        product.setCategory(category);

        return product;
    }

    public ShipmentProductRequest toShipmentProductRequest(Product product, Integer quantity) {
        if (product == null || quantity == null) {
            return null;
        }
        return new ShipmentProductRequest(
                product.getId().toString(),
                null, // height
                null, // width
                null, // length
                product.getPrice(),
                quantity
        );
    }

    public List<ShipmentProductRequest> toShipmentProductRequestList(Map<Product, Integer> productQuantityMap) {
        if (productQuantityMap == null || productQuantityMap.isEmpty()) {
            return new ArrayList<>();
        }

        return productQuantityMap.entrySet().stream()
                .map(entry -> toShipmentProductRequest(entry.getKey(), entry.getValue()))
                .filter(Objects::nonNull) // Remove nulos caso o método unitário retorne null
                .collect(Collectors.toList());
    }

}
