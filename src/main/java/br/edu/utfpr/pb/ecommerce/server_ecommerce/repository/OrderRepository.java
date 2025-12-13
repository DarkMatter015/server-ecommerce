package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseRepository;

import java.util.Optional;

public interface OrderRepository extends BaseRepository<Order,Long> {
    Optional<Order> findByIdAndUser(Long id, User user);
    Order findByUserAndId(User user, Long id);
}
