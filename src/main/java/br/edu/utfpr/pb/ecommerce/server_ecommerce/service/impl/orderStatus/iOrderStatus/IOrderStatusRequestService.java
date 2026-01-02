package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.orderStatus.iOrderStatus;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderStatus.OrderStatusUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderStatus;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseRequestService;

public interface IOrderStatusRequestService extends IBaseRequestService<OrderStatus, OrderStatusUpdateDTO, Long> {
}
