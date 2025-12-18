package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.OrderNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.OrderStatusNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.ProductNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.publisher.order.OrderPublisher;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.OrderMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.embedded.EmbeddedAddress;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderStatusRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IOrder.IOrderRequestService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudRequestServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.MapperUtils.map;
import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.ValidationUtils.validateQuantityOfProduct;

@Service
public class OrderRequestServiceImpl extends CrudRequestServiceImpl<Order, OrderUpdateDTO, Long> implements IOrderRequestService {

    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final ModelMapper modelMapper;
    private final OrderMapper orderMapper;
    private final OrderPublisher orderPublisher;
    private final OrderStatusRepository orderStatusRepository;
    private final ProductRepository productRepository;

    public OrderRequestServiceImpl(OrderRepository orderRepository, OrderResponseServiceImpl orderResponseService, AuthService authService, ModelMapper modelMapper, OrderMapper orderMapper, OrderPublisher orderPublisher, OrderStatusRepository orderStatusRepository, ProductRepository productRepository) {
        super(orderRepository, orderResponseService);
        this.orderRepository = orderRepository;
        this.authService = authService;
        this.modelMapper = modelMapper;
        this.orderMapper = orderMapper;
        this.orderPublisher = orderPublisher;
        this.orderStatusRepository = orderStatusRepository;
        this.productRepository = productRepository;
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

        if (updateDTO.getAddress() != null) {
            EmbeddedAddress newAddress = map(updateDTO.getAddress(), EmbeddedAddress.class, modelMapper);
            order.setAddress(newAddress);
        }

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order createOrder(OrderRequestDTO request) {
        User user = authService.getAuthenticatedUser();
        Order order = create(request, user);
        orderPublisher.send(
                orderMapper.toEventDTO(order, user.getCpf())
        );
        return order;
    }

    private Order create(OrderRequestDTO request, User user) {
        Map<Long, Product> productMap = findAndValidateProducts(request.getOrderItems());
        validateStock(request.getOrderItems(), productMap);

        Order order = orderMapper.toEntity(request);
        order.setUser(user);
        order.setStatus(orderStatusRepository.findByName("PROCESSANDO").orElseThrow(() -> new OrderStatusNotFoundException("Error: Order status is not found.")));

        return orderRepository.save(order);
    }

    private Map <Long, Product> findAndValidateProducts(List<OrderItemRequestDTO> orderItems) {
        Set<Long> productIds = orderItems.stream()
                .map(OrderItemRequestDTO::getProductId)
                .collect(Collectors.toSet());

        List<Product> products = productRepository.findAllById(productIds);

        if (products.size() != productIds.size()) {
            Set<Long> foundProductIds = products.stream()
                    .map(Product::getId)
                    .collect(Collectors.toSet());
            productIds.removeAll(foundProductIds);
            throw new ProductNotFoundException("Products not found with ids: " + productIds);
        }

        return products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    private void validateStock(List<OrderItemRequestDTO> orderItems, Map<Long, Product> productMap) {
        for (OrderItemRequestDTO item : orderItems) {
            Product product = productMap.get(item.getProductId());
            validateQuantityOfProduct(item.getQuantity(), product);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = authService.getAuthenticatedUser();
        findAndValidateOrder(id, user);
        super.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(Iterable<? extends Order> iterable) {
        User user = authService.getAuthenticatedUser();
        iterable.forEach(order -> {
            if (!order.getUser().getId().equals(user.getId())) {
                throw new AccessDeniedException("You don't have permission to modify this order!");
            }
        });
        super.delete(iterable);
    }
}
