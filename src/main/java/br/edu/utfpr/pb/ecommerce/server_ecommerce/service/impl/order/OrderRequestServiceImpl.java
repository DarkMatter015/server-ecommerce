package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.OrderNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.OrderStatusNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.OrderMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.EmbeddedAddress;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.rabbitmq.publisher.OrderPublisher;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderStatusRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IOrder.IOrderRequestService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudRequestServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.MapperUtils.map;

@Service
public class OrderRequestServiceImpl extends CrudRequestServiceImpl<Order, OrderUpdateDTO, Long> implements IOrderRequestService {

    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final ModelMapper modelMapper;
    private final OrderMapper orderMapper;
    private final OrderPublisher orderPublisher;
    private final OrderStatusRepository orderStatusRepository;

    public OrderRequestServiceImpl(OrderRepository orderRepository, AuthService authService, ModelMapper modelMapper, OrderMapper orderMapper, OrderPublisher orderPublisher, br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderStatusRepository orderStatusRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
        this.authService = authService;
        this.modelMapper = modelMapper;
        this.orderMapper = orderMapper;
        this.orderPublisher = orderPublisher;
        this.orderStatusRepository = orderStatusRepository;
    }

    private Order findAndValidateOrder(Long id, User user) {
        return orderRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));
    }

    @Override
    @Transactional
    public Order update(Long id, OrderUpdateDTO updateDTO) {
        User user = authService.getAuthenticatedUser();
        Order order = findAndValidateOrder(id, user);

        if(updateDTO.getAddress() != null) {
            EmbeddedAddress newAddress = map(updateDTO.getAddress(), EmbeddedAddress.class, modelMapper);
            order.setAddress(newAddress);
        }

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order createOrder(OrderRequestDTO request) {
        try {
            User user = authService.getAuthenticatedUser();
            Order order = create(request, user);
            orderPublisher.send(
                    orderMapper.toEventDTO(order, user.getCpf())
            );
            return order;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Order create(OrderRequestDTO request, User user) {
        Order order = orderMapper.toEntity(request);
        order.setUser(user);
        order.setStatus(orderStatusRepository.findByName("PENDENTE").orElseThrow(() -> new OrderStatusNotFoundException("Error: Order status is not found.")));
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteAll() {
        User user = authService.getAuthenticatedUser();
        orderRepository.deleteAllByUser(user);
    }

    @Override
    @Transactional
    public void deleteById(Long aLong) {
        User user = authService.getAuthenticatedUser();
        orderRepository.deleteByIdAndUser(aLong, user);
    }

    @Override
    @Transactional
    public void delete(Iterable<? extends Order> iterable) {
        User user = authService.getAuthenticatedUser();
        orderRepository.deleteAllByUserAndIdIn(user, iterable);
    }
}
