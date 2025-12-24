package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderStatus;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseSoftDeleteRepository;

import java.util.Optional;

public interface OrderStatusRepository extends BaseSoftDeleteRepository<OrderStatus, Long> {
    Optional<OrderStatus> findByName(String name);
}
