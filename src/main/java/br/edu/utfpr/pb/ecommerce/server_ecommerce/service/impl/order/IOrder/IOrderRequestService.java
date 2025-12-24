package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order.IOrder;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseSoftDeleteRequestService;

public interface IOrderRequestService extends IBaseSoftDeleteRequestService<Order, OrderUpdateDTO, Long> {
    Order createOrder(OrderRequestDTO dto);
}
