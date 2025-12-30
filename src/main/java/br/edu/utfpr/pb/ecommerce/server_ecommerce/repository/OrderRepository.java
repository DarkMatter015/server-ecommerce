package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseSoftDeleteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends BaseSoftDeleteRepository<Order,Long> {
    @EntityGraph(attributePaths = {"user", "orderItems", "orderItems.product", "orderItems.product.category", "payment", "status"})
    Optional<Order> findByIdAndUser(Long id, User user);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"user", "orderItems", "orderItems.product", "orderItems.product.category", "payment", "status"})
    List<Order> findAll(@Nullable Specification<Order> spec);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"user", "payment", "status"})
    Page<Order> findAll(Specification<Order> spec, @Nullable Pageable pageable);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"user", "orderItems", "orderItems.product", "orderItems.product.category", "payment", "status"})
    Optional<Order> findOne(@Nullable Specification<Order> spec);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"user", "orderItems", "orderItems.product", "orderItems.product.category", "payment", "status"})
    Optional<Order> findById(@Nullable Long id);

}
