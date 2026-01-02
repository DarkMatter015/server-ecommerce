package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order.IOrder.IOrderResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.BaseSoftDeleteResponseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderResponseServiceImpl extends BaseSoftDeleteResponseServiceImpl<Order, Long> implements IOrderResponseService {

    public OrderResponseServiceImpl(OrderRepository orderRepository,
                                    AuthService authService) {
        super(orderRepository, authService);
    }
}
