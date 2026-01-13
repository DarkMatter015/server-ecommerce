package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderAIDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.ResourceNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.OrderMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order.IOrder.IOrderResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.BaseSoftDeleteResponseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderResponseServiceImpl extends BaseSoftDeleteResponseServiceImpl<Order, Long> implements IOrderResponseService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderResponseServiceImpl(OrderRepository orderRepository,
                                    AuthService authService,
                                    OrderMapper orderMapper) {
        super(orderRepository, authService);
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Order findOrderByOrderIdAndUser(Long orderId, User user) {
        return orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new ResourceNotFoundException(Order.class, orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderAIDTO getOrderForAI(Long orderId) {
        Order order = super.findById(orderId);
        return orderMapper.toAIResponseDTO(order);
    }
}
