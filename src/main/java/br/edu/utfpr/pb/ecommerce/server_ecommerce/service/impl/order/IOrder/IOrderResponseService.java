package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order.IOrder;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseSoftDeleteResponseService;

public interface IOrderResponseService extends IBaseSoftDeleteResponseService<Order, Long> {
    Order findOrderByOrderIdAndUser(Long orderId, User user);
}
