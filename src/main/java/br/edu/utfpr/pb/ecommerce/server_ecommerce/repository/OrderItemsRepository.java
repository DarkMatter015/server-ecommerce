package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface OrderItemsRepository extends BaseRepository<OrderItem,Long> {
    List<OrderItem> findAllByOrder_User_Id(Long orderUserId);

    Optional<OrderItem> findByIdAndOrder_User_Id(Long id, Long orderUserId);
    List<OrderItem> findAllByOrder_User_Id(Long orderUserId, Sort sort);

    Page<OrderItem> findAllByOrder_User_Id(Long orderUserId, Pageable pageable);
    boolean existsByOrder_User_IdAndId(Long orderUserId, Long id);

    long countByOrder_User_Id(Long orderUserId);
    
}
