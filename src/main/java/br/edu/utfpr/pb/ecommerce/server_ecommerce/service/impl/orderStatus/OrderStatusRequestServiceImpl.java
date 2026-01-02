package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.orderStatus;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderStatus.OrderStatusUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderStatus;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderStatusRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.BaseRequestServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.orderStatus.iOrderStatus.IOrderStatusRequestService;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusRequestServiceImpl extends BaseRequestServiceImpl<OrderStatus, OrderStatusUpdateDTO, Long> implements IOrderStatusRequestService {
    public OrderStatusRequestServiceImpl(OrderStatusRepository repository,
                                         OrderStatusResponseServiceImpl crudResponseService) {
        super(repository, crudResponseService);
    }
}
