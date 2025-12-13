package br.edu.utfpr.pb.ecommerce.server_ecommerce.rabbitmq.consumer;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.PostalCodeRequest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentProductRequest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.response.ShipmentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.service.MelhorEnvioService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderEventDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.BusinessException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.OrderNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.OrderStatusNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.ProductNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.ProductMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.ShipmentMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderStatusRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ProcessOrder {

    private final AuthService authService;
    private final ProductMapper productMapper;
    private final OrderStatusRepository orderStatusRepository;
    private final OrderRepository orderRepository;
    private final ShipmentMapper shipmentMapper;
    private final MelhorEnvioService melhorEnvioService;
    private final ProductRepository productRepository;


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
}
