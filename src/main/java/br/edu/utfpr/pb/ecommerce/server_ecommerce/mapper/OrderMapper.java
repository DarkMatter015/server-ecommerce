package br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.dto.AddressCEP;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.service.CepService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.payment.PaymentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.PaymentNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.ProductNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.*;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.PaymentRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.MapperUtils.map;
import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.ValidationUtils.validateQuantityOfProducts;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;
    private final ModelMapper modelMapper;
    private final CepService cepService;
    private final AddressMapper addressMapper;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;

    private List<OrderItem> getOrderItems(Order order, List<OrderItemRequestDTO> orderItems) {
        List<Long> productIds = orderItems.stream()
                .map(OrderItemRequestDTO::getProductId)
                .distinct()
                .toList();

        Map<Long, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        return orderItems.stream()
                .map(itemDTO -> {
                    Product product = productMap.get(itemDTO.getProductId());
                    if (product == null) {
                        throw new ProductNotFoundException("Product not found with id: " + itemDTO.getProductId());
                    }

                    OrderItem item = new OrderItem();
                    item.setProduct(product);
                    validateQuantityOfProducts(itemDTO.getQuantity(), product);
                    item.setQuantity(itemDTO.getQuantity());
                    product.decreaseQuantity(itemDTO.getQuantity());

                    item.setOrder(order);

                    return item;
                }).toList();
    }

    public OrderResponseDTO toDTO(Order order) {
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(order.getId());
        responseDTO.setData(order.getData());
        responseDTO.setUserId(order.getUser().getId());
        responseDTO.setAddress(order.getAddress());

        List<OrderItemResponseDTO> itensResponse = orderItemMapper.toDTOList(order.getOrderItems());
        responseDTO.setOrderItems(itensResponse);

        PaymentResponseDTO paymentResponseDTO = map(order.getPayment(), PaymentResponseDTO.class, modelMapper);
        responseDTO.setPayment(paymentResponseDTO);
        responseDTO.setShipment(order.getShipment());
        return responseDTO;
    }

    public Order toEntity(OrderRequestDTO dto) {
        Order order = new Order();

        AddressCEP addressCEP = cepService.getAddressByCEP(dto.getAddress().getCep());
        EmbeddedAddress embeddedAddress = addressMapper.toEmbeddedAddress(addressCEP, dto.getAddress());
        order.setAddress(embeddedAddress);

        List<OrderItem> itens = getOrderItems(order, dto.getOrderItems());

        order.setOrderItems(itens);

        Payment payment = paymentRepository.findById(dto.getPaymentId())
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with this id: " + dto.getPaymentId()));

        order.setPayment(payment);

        return order;
    }
    
}
