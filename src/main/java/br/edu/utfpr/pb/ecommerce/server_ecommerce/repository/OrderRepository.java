package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findAllByUser(User user);
    Optional<Order> findByIdAndUser(Long id, User user);

    Order findByUserAndId(User user, Long id);
    List<Order> findAllByUser(User user, Sort sort);
    Page<Order> findAllByUser(User user, Pageable pageable);
    boolean existsByUserAndId(User user, Long id);
    long countByUser(User user);

    void deleteAllByUser(User user);
    void deleteByIdAndUser(Long id, User user);
    void deleteAllByUserAndIdIn(User user, Iterable<? extends Order> ids);
}
