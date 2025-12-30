package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderItem;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseSoftDeleteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface OrderItemsRepository extends BaseSoftDeleteRepository<OrderItem,Long> {
    @EntityGraph(attributePaths = {"order", "product", "product.category"})
    Optional<OrderItem> findByIdAndOrder_User_Id(Long id, Long orderUserId);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"order", "product", "product.category"})
    List<OrderItem> findAll(Specification<OrderItem> spec);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"order", "product", "product.category"})
    Page<OrderItem> findAll(Specification<OrderItem> spec, @Nullable Pageable pageable);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"order", "product", "product.category"})
    Optional<OrderItem> findOne(@Nullable Specification<OrderItem> spec);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"order", "product", "product.category"})
    Optional<OrderItem> findById(@Nullable Long id);
}
