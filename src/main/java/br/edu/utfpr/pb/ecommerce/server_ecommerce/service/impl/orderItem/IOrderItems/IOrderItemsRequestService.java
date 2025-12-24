package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.orderItem.IOrderItems;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderItem;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseSoftDeleteRequestService;

public interface IOrderItemsRequestService extends IBaseSoftDeleteRequestService<OrderItem, OrderItemUpdateDTO, Long> {
    OrderItem createOrderItem(OrderItemRequestDTO dto);
}
