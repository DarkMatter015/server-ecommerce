package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.orderStatus;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.ResourceNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderStatus;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderStatusRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.BaseResponseServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.orderStatus.iOrderStatus.IOrderStatusResponseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderStatusResponseServiceImpl extends BaseResponseServiceImpl<OrderStatus, Long> implements IOrderStatusResponseService {
    private final OrderStatusRepository repository;

    public OrderStatusResponseServiceImpl(OrderStatusRepository repository,
                                          AuthService authService) {
        super(repository, authService);
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderStatus findByName(String name) {
        return repository.findByName(name).orElseThrow(() -> new ResourceNotFoundException(OrderStatus.class, name));
    }
}
