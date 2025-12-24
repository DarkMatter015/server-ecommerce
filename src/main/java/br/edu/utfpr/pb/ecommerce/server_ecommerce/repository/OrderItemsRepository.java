package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderItem;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseSoftDeleteRepository;

import java.util.Optional;

public interface OrderItemsRepository extends BaseSoftDeleteRepository<OrderItem,Long> {
    Optional<OrderItem> findByIdAndOrder_User_Id(Long id, Long orderUserId);

}
