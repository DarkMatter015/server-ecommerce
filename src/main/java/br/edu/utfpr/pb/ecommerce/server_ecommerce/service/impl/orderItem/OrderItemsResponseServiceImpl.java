package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.orderItem;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.OrderItemNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderItem;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderItemsRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IOrderItems.IOrderItemsResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudResponseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderItemsResponseServiceImpl extends CrudResponseServiceImpl<OrderItem, Long> implements IOrderItemsResponseService {

    private final OrderItemsRepository orderItemsRepository;
    private final AuthService authService;

    public OrderItemsResponseServiceImpl(OrderItemsRepository orderItemsRepository, AuthService authService) {
        super(orderItemsRepository);
        this.orderItemsRepository = orderItemsRepository;
        this.authService = authService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> findAll() {
        User user = authService.getAuthenticatedUser();
        return orderItemsRepository.findAllByOrder_User_Id(user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> findAll(Sort sort) {
        User user = authService.getAuthenticatedUser();
        return orderItemsRepository.findAllByOrder_User_Id(user.getId(), sort);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderItem> findAll(Pageable pageable) {
        User user = authService.getAuthenticatedUser();
        return orderItemsRepository.findAllByOrder_User_Id(user.getId(), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItem findById(Long id) {
        User user = authService.getAuthenticatedUser();
        return orderItemsRepository.findByIdAndOrder_User_Id(id, user.getId())
                .orElseThrow(() -> new OrderItemNotFoundException("Order Item not found."));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        User user = authService.getAuthenticatedUser();
        return orderItemsRepository.existsByOrder_User_IdAndId(user.getId(), id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        User user = authService.getAuthenticatedUser();
        return orderItemsRepository.countByOrder_User_Id(user.getId());
    }
}
