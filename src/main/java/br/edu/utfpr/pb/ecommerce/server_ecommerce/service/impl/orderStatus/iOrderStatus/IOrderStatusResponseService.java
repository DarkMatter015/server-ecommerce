package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.orderStatus.iOrderStatus;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderStatus;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseResponseService;

public interface IOrderStatusResponseService extends IBaseResponseService<OrderStatus, Long> {
    OrderStatus findByName(String name);
}
