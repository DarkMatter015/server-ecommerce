package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.order.process;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.dto.AddressCEP;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.exception.CepException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.service.CepService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.PostalCodeRequest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentProductRequest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.response.ShipmentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.exception.ShipmentException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.service.MelhorEnvioService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.order.InvalidAddressException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.order.InvalidProductException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.order.InvalidShipmentException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.order.OrderProcessingException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.order.OrderEventDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.OrderMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.ProductMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.ShipmentMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderItem;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.embedded.address.EmbeddedAddress;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.TranslationService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.email.EmailService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order.OrderRequestServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order.OrderResponseServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.orderStatus.OrderStatusResponseServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.product.ProductRequestServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.product.ProductResponseServiceImpl;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.internal.util.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.MapperUtils.map;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProcessOrder {

    private final AuthService authService;
    private final ProductMapper productMapper;
    private final OrderStatusResponseServiceImpl orderStatusResponseService;
    private final OrderRequestServiceImpl orderRequestServiceImpl;
    private final OrderResponseServiceImpl orderResponseServiceImpl;
    private final ShipmentMapper shipmentMapper;
    private final MelhorEnvioService melhorEnvioService;
    private final CepService cepService;
    private final ProductRequestServiceImpl productRequestService;
    private final ProductResponseServiceImpl productResponseService;
    private final ModelMapper modelMapper;
    private final TranslationService translator;
    private final EmailService emailService;
    private final OrderMapper orderMapper;

    @Transactional
    public void processOrder(OrderEventDTO orderEventDTO) {
        log.info("Processing Order Event: {}", orderEventDTO);
        Locale userLocale = StringUtils.hasText(orderEventDTO.locale())
                ? Locale.forLanguageTag(orderEventDTO.locale())
                : Locale.getDefault();
        User user = authService.loadUserByCpf(orderEventDTO.userCpf());
        Order order = orderResponseServiceImpl.findOrderByOrderIdAndUser(orderEventDTO.orderId(), user);

        try {
            log.info("Validating Order Event: {}", orderEventDTO);

            Map<Product, Integer> productQuantityMap = validateOrder(orderEventDTO);

            validateAddress(order, orderEventDTO);

            validateShipment(order, orderEventDTO, productQuantityMap);

            updateOrderStatus(order, "PENDENTE", "order.status.message.pending", userLocale);

            sendSuccessEmail(order);
        } catch (OrderProcessingException e) {
            String specificMessage = translator.getMessageLocale(e.getSpecificKey(), userLocale);
            String finalMessage = translator.getMessageLocale(
                    e.getGenericKey(),
                    userLocale,
                    specificMessage
            );
            log.warn("Business validation failed for Order {}: {}", order.getId(), finalMessage);
            performCancellation(order, finalMessage);

        } catch (Exception e) {
            log.error("Unexpected error processing Order {}", order.getId(), e);
            String finalMessage = translator.getMessageLocale("order.status.message.cancelled.generic", userLocale);
            performCancellation(order, finalMessage);
        }
    }

    private Map<Product, Integer> validateOrder(OrderEventDTO orderEventDTO) {
        Map<Long, Integer> quantityByIdMap = orderEventDTO.orderItems().stream()
                .collect(Collectors.toMap(OrderItemRequestDTO::getProductId, OrderItemRequestDTO::getQuantity));

        List<Product> products = productResponseService.findAll(quantityByIdMap.keySet());

        if (products.size() != quantityByIdMap.size()) {
            throw new InvalidProductException(ErrorCode.PRODUCT_DISCREPANCY);
        }

        Map<Product, Integer> productQuantityMap = products.stream()
                .collect(Collectors.toMap(
                        product -> product,
                        product -> quantityByIdMap.get(product.getId())
                ));

        if (products.size() != productQuantityMap.size()) {
            throw new InvalidProductException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        return productQuantityMap;
    }

    private void validateShipment(Order order, OrderEventDTO orderEventDTO, Map<Product, Integer> productQuantityMap) {
        try {
            ShipmentRequestDTO shipmentRequestDTO = new ShipmentRequestDTO();
            shipmentRequestDTO.setTo(new PostalCodeRequest(orderEventDTO.address().getCep()));

            List<ShipmentProductRequest> shipmentProductRequests = productMapper.toShipmentProductRequestList(productQuantityMap);
            shipmentRequestDTO.setProducts(shipmentProductRequests);

            List<ShipmentResponseDTO> shipmentOptions = melhorEnvioService.calculateShipmentByProducts(shipmentRequestDTO);

            ShipmentResponseDTO selectedShipment = shipmentOptions.stream()
                    .filter(s -> s.id().equals(orderEventDTO.shipmentId()))
                    .findFirst()
                    .orElseThrow(() -> new ShipmentException(ErrorCode.SHIPMENT_INVALID));

            order.setShipment(shipmentMapper.toEmbedded(selectedShipment));
        } catch (ShipmentException e) {
            log.warn("Error validating Shipment: {}", e.getMessage());
            throw new InvalidShipmentException(e.getCode());
        } catch (FeignException e) {
            log.error("External Shipment Service failed", e);
            throw new InvalidShipmentException(ErrorCode.SHIPMENT_SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            log.error("Unexpected error processing Shipment for Order {}", order.getId(), e);
            throw new RuntimeException("Error validating shipment", e);
        }
    }

    private void validateAddress(Order order, OrderEventDTO orderEventDTO) {
        try {
            order.getAddress().setNumber(orderEventDTO.address().getNumber());
            order.getAddress().setComplement(orderEventDTO.address().getComplement());
            AddressCEP addressCEP = cepService.getAddressByCEP(orderEventDTO.address().getCep());
            order.setAddress(map(addressCEP, EmbeddedAddress.class, modelMapper));
        } catch (CepException e) {
            log.warn("Error validating CEP: {}", e.getMessage());
            throw new InvalidAddressException(e.getCode());
        } catch (FeignException e) {
            log.error("External CEP Service failed", e);
            throw new InvalidAddressException(ErrorCode.CEP_NOT_FOUND_OR_INVALID);
        } catch (Exception e) {
            log.error("Unexpected error processing CEP for Order {}", order.getId(), e);
            throw new RuntimeException("Error validating address", e);
        }
    }

    private void updateOrderStatus(Order order, String statusName, String messageKey, Locale locale) {
        order.setStatus(orderStatusResponseService.findByName(statusName));
        order.setStatusMessage(translator.getMessageLocale(messageKey, locale));
        orderRequestServiceImpl.saveAndFlush(order);
    }

    private void sendSuccessEmail(Order order) {
        try {
            emailService.sendOrderSuccessEmail(order.getUser(), orderMapper.toDTO(order));
        } catch (Exception e) {
            log.error("Error sending Order Success Email for Order ID: {}", order.getId(), e);
        }
    }

    private void sendCancellingEmail(Order order) {
        try {
            emailService.sendOrderCancellationEmail(order.getUser(), orderMapper.toDTO(order));
        } catch (Exception e) {
            log.error("Error sending Order Cancelling Email for Order ID: {}", order.getId(), e);
        }
    }

    private void restoreStock(List<OrderItem> orderItems) {
        if (orderItems == null) return;

        List<Product> productsToUpdate = new ArrayList<>();
        for (OrderItem item : orderItems) {
            Product product = item.getProduct();
            product.setQuantityAvailableInStock(product.getQuantityAvailableInStock() + item.getQuantity());
            productsToUpdate.add(product);
        }
        productRequestService.save(productsToUpdate);
    }

    private void performCancellation(Order order, String finalMessage) {
        log.warn("Performing cancellation for Order {}: {}", order.getId(), finalMessage);
        order.setStatus(orderStatusResponseService.findByName("CANCELADO"));
        order.setStatusMessage(finalMessage);

        orderRequestServiceImpl.saveAndFlush(order);
        sendCancellingEmail(order);

        restoreStock(order.getOrderItems());
    }


}
