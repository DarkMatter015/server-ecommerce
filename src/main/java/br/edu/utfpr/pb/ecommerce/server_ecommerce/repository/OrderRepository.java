package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseSoftDeleteRepository;

import java.util.Optional;

public interface OrderRepository extends BaseSoftDeleteRepository<Order,Long> {
    Optional<Order> findByIdAndUser(Long id, User user);
    Order findByUserAndId(User user, Long id);
}
