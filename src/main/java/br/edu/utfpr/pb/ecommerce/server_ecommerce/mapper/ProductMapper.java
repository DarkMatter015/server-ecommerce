package br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentProductRequest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDTO dto, Category category) {
        if (dto == null) return null;

        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .urlImage(dto.getUrlImage())
                .quantityAvailableInStock(dto.getQuantityAvailableInStock())
                .category(category)
                .build();
    }

    public ShipmentProductRequest toShipmentProductRequest(Product product, Integer quantity) {
        if (product == null || quantity == null)
            return null;
        return new ShipmentProductRequest(
                product.getId().toString(),
                null,
                null,
                null,
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
