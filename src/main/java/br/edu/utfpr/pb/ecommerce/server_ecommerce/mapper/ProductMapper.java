package br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentProductRequest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductImageResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.syncProducts.SyncProductEventDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.ProductImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    @Value("${app.backend.url}")
    private String backendUrl;

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

    public SyncProductEventDTO toEventDTO(Product product, Category category) {
        if (product == null || category == null)
            return null;
        return new SyncProductEventDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantityAvailableInStock(),
                category.getName()
        );
    }

    public String buildImageUrl(Long productId, Long imageId) {
        return backendUrl + "/products/" + productId + "/images/" + imageId;
    }

    public ProductImageResponseDTO toImageDTO(ProductImage image) {
        if (image == null) return null;
        return ProductImageResponseDTO.builder()
                .id(image.getId())
                .url(buildImageUrl(image.getProduct().getId(), image.getId()))
                .position(image.getPosition())
                .build();
    }

    public void enrichResponse(Product product, ProductResponseDTO dto) {
        if (product == null || dto == null || product.getImages() == null) {
            return;
        }
        List<ProductImageResponseDTO> images = product.getImages().stream()
                .filter(image -> image.getDeletedAt() == null)
                .sorted(Comparator.comparing(ProductImage::getPosition)
                        .thenComparing(ProductImage::getId))
                .map(this::toImageDTO)
                .collect(Collectors.toList());
        dto.setImages(images);
        if (!images.isEmpty()) {
            dto.setUrlImage(images.get(0).getUrl());
        }
    }
}
