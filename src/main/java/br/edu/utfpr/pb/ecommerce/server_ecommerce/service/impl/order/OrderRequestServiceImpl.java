package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.PostalCodeRequest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentProductRequest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.response.ShipmentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.service.MelhorEnvioService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderEventDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.BusinessException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.OrderNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.OrderStatusNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.ProductNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.OrderMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.ProductMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.ShipmentMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.EmbeddedAddress;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.rabbitmq.publisher.OrderPublisher;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderStatusRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IOrder.IOrderRequestService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudRequestServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.MapperUtils.map;

@Service
public class OrderRequestServiceImpl extends CrudRequestServiceImpl<Order, OrderUpdateDTO, Long> implements IOrderRequestService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AuthService authService;
    private final ModelMapper modelMapper;
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;
    private final ShipmentMapper shipmentMapper;
    private final MelhorEnvioService melhorEnvioService;
    private final OrderPublisher orderPublisher;
    private final OrderStatusRepository orderStatusRepository;

    public OrderRequestServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, AuthService authService, ModelMapper modelMapper, OrderMapper orderMapper, ProductMapper productMapper, ShipmentMapper shipmentMapper, MelhorEnvioService melhorEnvioService, OrderPublisher orderPublisher, br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderStatusRepository orderStatusRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.authService = authService;
        this.modelMapper = modelMapper;
        this.orderMapper = orderMapper;
        this.productMapper = productMapper;
        this.shipmentMapper = shipmentMapper;
        this.melhorEnvioService = melhorEnvioService;
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

    @Transactional
    public void processOrder(OrderEventDTO orderEventDTO) {
        User user = authService.loadUserByCpf(orderEventDTO.userCpf());
        Order order = orderRepository.findByUserAndId(user, orderEventDTO.orderId());

        if (order == null) {
            throw new OrderNotFoundException("Order not found.");
        }

        try {
            order.setStatus(orderStatusRepository.findByName("PROCESSANDO").orElseThrow(() -> new OrderStatusNotFoundException("Error: Order status is not found.")));
            orderRepository.save(order);

            Map<Long, Integer> quantityByIdMap = orderEventDTO.orderItems().stream()
                    .collect(Collectors.toMap(OrderItemRequestDTO::getProductId, OrderItemRequestDTO::getQuantity));

            List<Product> products = productRepository.findAllById(quantityByIdMap.keySet());

            if (products.size() != quantityByIdMap.size()) {
                throw new ProductNotFoundException("Product discrepancy: One or more IDs do not exist.");
            }

            Map<Product, Integer> productQuantityMap = products.stream()
                    .collect(Collectors.toMap(
                            product -> product,
                            product -> quantityByIdMap.get(product.getId())
                    ));

            if (products.size() != productQuantityMap.size()) {
                throw new ProductNotFoundException("One or more products were not found.");
            }

            ShipmentRequestDTO shipmentRequestDTO = new ShipmentRequestDTO();
            shipmentRequestDTO.setTo(new PostalCodeRequest(orderEventDTO.address().getCep()));

            List<ShipmentProductRequest> shipmentProductRequests = productMapper.toShipmentProductRequestList(productQuantityMap);
            shipmentRequestDTO.setProducts(shipmentProductRequests);

            List<ShipmentResponseDTO> shipmentOptions = melhorEnvioService.calculateShipmentByProducts(shipmentRequestDTO);

            ShipmentResponseDTO selectedShipment = shipmentOptions.stream()
                    .filter(s -> s.id().equals(orderEventDTO.shipmentId()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException("The selected shipping method is no longer available or is invalid for this order."));

            order.setShipment(shipmentMapper.toEmbedded(selectedShipment));
            order.setStatus(orderStatusRepository.findByName("PENDENTE").orElseThrow(() -> new OrderStatusNotFoundException("Error: Order status is not found.")));
            orderRepository.save(order);
        } catch (Exception e) {
            order.setStatus(orderStatusRepository.findByName("CANCELADO").orElseThrow(() -> new OrderStatusNotFoundException("Error: Order status is not found.")));
            orderRepository.save(order);
            throw new RuntimeException("Error processing order", e);
        }
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
