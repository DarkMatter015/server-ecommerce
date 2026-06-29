package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.ProductImage;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseSoftDeleteRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends BaseSoftDeleteRepository<ProductImage, Long> {

    @EntityGraph(attributePaths = {"product"})
    List<ProductImage> findByProductIdAndDeletedAtIsNullOrderByPositionAscIdAsc(Long productId);

    @EntityGraph(attributePaths = {"product"})
    Optional<ProductImage> findByIdAndDeletedAtIsNull(Long id);

    long countByProductIdAndDeletedAtIsNull(Long productId);
}
