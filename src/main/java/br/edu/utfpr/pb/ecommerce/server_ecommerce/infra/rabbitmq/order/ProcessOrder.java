package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.dto.AddressCEP;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.service.CepService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.PostalCodeRequest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentProductRequest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.response.ShipmentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.service.MelhorEnvioService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.BusinessException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.ResourceNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.ProductMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.ShipmentMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderStatus;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.embedded.address.EmbeddedAddress;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderStatusRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.MapperUtils.map;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProcessOrder {

    private final AuthService authService;
    private final ProductMapper productMapper;
    private final OrderStatusRepository orderStatusRepository;
    private final OrderRepository orderRepository;
    private final ShipmentMapper shipmentMapper;
    private final MelhorEnvioService melhorEnvioService;
    private final CepService cepService;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void processOrder(OrderEventDTO orderEventDTO) {
        log.info("Processing Order Event: {}", orderEventDTO);
        User user = authService.loadUserByCpf(orderEventDTO.userCpf());
        Optional<Order> optOrder = orderRepository.findByIdAndUser(orderEventDTO.orderId(), user);

        if (optOrder.isEmpty())
            throw new ResourceNotFoundException(Order.class, orderEventDTO.orderId());

        Order order = optOrder.get();
        try {
            log.info("Validating Order Event: {}", orderEventDTO);
            Map<Product, Integer> productQuantityMap = validateOrder(orderEventDTO);
            validateAddress(order, orderEventDTO);
            validateShipment(order, orderEventDTO, productQuantityMap);

            order.setStatus(orderStatusRepository.findByName("PENDENTE").orElseThrow(() -> new ResourceNotFoundException(OrderStatus.class, "PENDENTE")));
            orderRepository.save(order);
        } catch (Exception e) {
            log.error("Error processing Order with ID: {}", order.getId(), e);
            order.setStatus(orderStatusRepository.findByName("CANCELADO").orElseThrow(() -> new ResourceNotFoundException(OrderStatus.class, "CANCELADO")));
            orderRepository.save(order);
        }
    }

    private Map<Product, Integer> validateOrder(OrderEventDTO orderEventDTO) {
        Map<Long, Integer> quantityByIdMap = orderEventDTO.orderItems().stream()
                .collect(Collectors.toMap(OrderItemRequestDTO::getProductId, OrderItemRequestDTO::getQuantity));

        List<Product> products = productRepository.findAllById(quantityByIdMap.keySet());

        if (products.size() != quantityByIdMap.size()) {
            throw new BusinessException(ErrorCode.PRODUCT_DISCREPANCY);
        }

        Map<Product, Integer> productQuantityMap = products.stream()
                .collect(Collectors.toMap(
                        product -> product,
                        product -> quantityByIdMap.get(product.getId())
                ));

        if (products.size() != productQuantityMap.size()) {
            throw new ResourceNotFoundException(Product.class, productQuantityMap.keySet());
        }

        return productQuantityMap;
    }

    private void validateShipment(Order order, OrderEventDTO orderEventDTO, Map<Product, Integer> productQuantityMap) {
        ShipmentRequestDTO shipmentRequestDTO = new ShipmentRequestDTO();
        shipmentRequestDTO.setTo(new PostalCodeRequest(orderEventDTO.address().getCep()));

        List<ShipmentProductRequest> shipmentProductRequests = productMapper.toShipmentProductRequestList(productQuantityMap);
        shipmentRequestDTO.setProducts(shipmentProductRequests);

        List<ShipmentResponseDTO> shipmentOptions = melhorEnvioService.calculateShipmentByProducts(shipmentRequestDTO);

        ShipmentResponseDTO selectedShipment = shipmentOptions.stream()
                .filter(s -> s.id().equals(orderEventDTO.shipmentId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.SHIPMENT_INVALID));

        order.setShipment(shipmentMapper.toEmbedded(selectedShipment));
    }

    private void validateAddress(Order order, OrderEventDTO orderEventDTO) {
        AddressCEP addressCEP = cepService.getAddressByCEP(orderEventDTO.address().getCep());
        order.setAddress(map(addressCEP, EmbeddedAddress.class, modelMapper));
    }
}
