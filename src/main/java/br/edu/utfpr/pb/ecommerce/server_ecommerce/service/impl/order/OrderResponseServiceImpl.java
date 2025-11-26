package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.OrderNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IOrder.IOrderResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudResponseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderResponseServiceImpl extends CrudResponseServiceImpl<Order, Long> implements IOrderResponseService {

    private final OrderRepository orderRepository;
    private final AuthService authService;

    public OrderResponseServiceImpl(OrderRepository orderRepository, AuthService authService) {
        super(orderRepository);
        this.orderRepository = orderRepository;
        this.authService = authService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        User user = authService.getAuthenticatedUser();
        return orderRepository.findAllByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll(Sort sort) {
        User user = authService.getAuthenticatedUser();
        return orderRepository.findAllByUser(user, sort);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> findAll(Pageable pageable) {
        User user = authService.getAuthenticatedUser();
        return orderRepository.findAllByUser(user, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Order findById(Long id) {
        User user = authService.getAuthenticatedUser();
        return orderRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        User user = authService.getAuthenticatedUser();
        return orderRepository.existsByUserAndId(user, id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        User user = authService.getAuthenticatedUser();
        return orderRepository.countByUser(user);
    }
}
