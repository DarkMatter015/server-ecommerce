package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderItemDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.BusinessException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.ResourceNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.order.OrderPublisher;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.OrderMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.*;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.embedded.address.EmbeddedAddress;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderStatusRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order.IOrder.IOrderRequestService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.BaseSoftDeleteRequestServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.payment.IPayment.IPaymentResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.validation.orderItem.IValidationOrderItem;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.MapperUtils.map;
import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.ValidationUtils.*;

@Slf4j
@Service
public class OrderRequestServiceImpl extends BaseSoftDeleteRequestServiceImpl<Order, OrderUpdateDTO> implements IOrderRequestService {

    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final ModelMapper modelMapper;
    private final OrderMapper orderMapper;
    private final OrderPublisher orderPublisher;
    private final OrderStatusRepository orderStatusRepository;
    private final ProductRepository productRepository;
    private final List<IValidationOrderItem> iValidationOrderItems;
    private final IPaymentResponseService paymentResponseService;

    public OrderRequestServiceImpl(OrderRepository orderRepository, OrderResponseServiceImpl orderResponseService, AuthService authService, ModelMapper modelMapper, OrderMapper orderMapper, OrderPublisher orderPublisher, OrderStatusRepository orderStatusRepository, ProductRepository productRepository, List<IValidationOrderItem> iValidationOrderItems, IPaymentResponseService paymentResponseService) {
        super(orderRepository, orderResponseService);
        this.orderRepository = orderRepository;
        this.authService = authService;
        this.modelMapper = modelMapper;
        this.orderMapper = orderMapper;
        this.orderPublisher = orderPublisher;
        this.orderStatusRepository = orderStatusRepository;
        this.productRepository = productRepository;
        this.iValidationOrderItems = iValidationOrderItems;
        this.paymentResponseService = paymentResponseService;
    }

    private void validateOrderOwnership(Order order) {
        User user = authService.getAuthenticatedUser();
        if (!order.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.ORDER_PERMISSION_MODIFY_DENIED);
        }
    }

    @Override
    @Transactional
    public Order update(Long id, OrderUpdateDTO updateDTO) {
        User user = authService.getAuthenticatedUser();
        Order order = findAndValidateOrder(id, user, orderRepository);

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
        Order order = validateAndCreateOrder(request, user);
        orderPublisher.send(orderMapper.toEventDTO(order, user.getCpf()));
        return order;
    }

    @Transactional
    public Order validateAndCreateOrder(OrderRequestDTO request, User user) {
        log.info("Starting the validation adn creation of New Order ...");
        Map<Long, Product> productMap = findAndValidateProductsExistence(request.getOrderItems());
        iValidationOrderItems.forEach(validation -> validation.validate(request.getOrderItems(), productMap));

        log.info("Searching and validating payment");
        Payment payment = paymentResponseService.findById(request.getPaymentId());
        OrderStatus processingStatus = orderStatusRepository.findByName("PROCESSANDO").orElseThrow(
                () -> new ResourceNotFoundException(OrderStatus.class, "PROCESSANDO"));

        Order order = orderMapper.toEntity(request, payment);
        order.setUser(user);
        order.setStatus(processingStatus);

        List<OrderItem> items = buildItemsAndReserveStock(order, request.getOrderItems(), productMap);
        order.setOrderItems(items);

        log.info("Saving the new order ...");
        return orderRepository.save(order);
    }

    private Map<Long, Product> findAndValidateProductsExistence(List<OrderItemDTO> itemsDto) {
        log.info("Finding and Validating all the products of the Order Items");
        Set<Long> productIds = itemsDto.stream()
                .map(OrderItemDTO::getProductId)
                .collect(Collectors.toSet());

        log.info("Searching products IDs: {}", productIds);

        List<Product> products = productRepository.findAllById(productIds);

        // Validação de Integridade: Pediu 5 produtos distintos, tem que vir 5.
        if (products.size() != productIds.size()) {
            Set<Long> foundIds = products.stream().map(Product::getId).collect(Collectors.toSet());

            Set<Long> missingIds = new HashSet<>(productIds);
            missingIds.removeAll(foundIds);

            log.error("Products not found. Requested IDs: {}. Found IDs: {}. Missing IDs: {}", productIds, foundIds, missingIds);
            throw new ResourceNotFoundException(Product.class, missingIds);
        }

        return products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    private List<OrderItem> buildItemsAndReserveStock(Order order, List<OrderItemDTO> itemsDto, Map<Long, Product> productMap) {
        return itemsDto.stream().map(dto -> {
            Product product = productMap.get(dto.getProductId());

            log.info("Decreasing the quantity of the product: {} - {} = {}", dto.getQuantity(), product.getQuantityAvailableInStock(), product.getQuantityAvailableInStock() - dto.getQuantity());
            product.decreaseQuantity(dto.getQuantity());

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(dto.getQuantity());

            return item;
        }).toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = authService.getAuthenticatedUser();
        findAndValidateOrder(id, user, orderRepository);
        super.deleteById(id);
    }

    @Override
    @Transactional
    public void softDeleteById(Long id) {
        User user = authService.getAuthenticatedUser();
        Order order = findAndValidateOrder(id, user, orderRepository);
        if (!order.isActive()) return;
        orderRepository.softDeleteById(id);
    }

    @Override
    @Transactional
    public void delete(Iterable<? extends Order> iterable) {
        iterable.forEach(this::validateOrderOwnership);
        super.delete(iterable);
    }
}
