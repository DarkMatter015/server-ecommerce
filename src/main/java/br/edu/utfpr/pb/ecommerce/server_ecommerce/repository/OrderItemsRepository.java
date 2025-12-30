package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderItem;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseSoftDeleteRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface OrderItemsRepository extends BaseSoftDeleteRepository<OrderItem,Long> {
    Optional<OrderItem> findByIdAndOrder_User_Id(Long id, Long orderUserId);

    @Override
    @EntityGraph(attributePaths = {"order", "product", "product.category", "order.user"})
    @NonNull
    List<OrderItem> findAll(Specification<OrderItem> spec);

    @Override
    @EntityGraph(attributePaths = {"order", "product", "product.category"})
    @NonNull
    Optional<OrderItem> findOne(@Nullable Specification<OrderItem> spec);
}
