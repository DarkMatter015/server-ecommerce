package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderStatus;

import java.util.Optional;

public interface OrderStatusRepository extends BaseRepository<OrderStatus, Long> {
    Optional<OrderStatus> findByName(String name);
}
